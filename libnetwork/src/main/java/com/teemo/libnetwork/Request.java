package com.teemo.libnetwork;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.teemo.libcommon.utils.DebugLog;
import com.teemo.libnetwork.cache.Cache;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class Request<T, R extends Request> implements Cloneable {
    protected String mUrl;
    protected HashMap<String, String> headers = new HashMap<>();
    protected HashMap<String, Object> params = new HashMap<>();
    //仅仅只访问本地缓存,即使本地缓存不存在,也不会发起网络请求
    public static final int CACHE_ONLY = 1;
    //先访问缓存,同时发起网络请求
    public static final int CACHE_FIRST = 2;
    //只访问网络,不存任何存储
    public static final int NET_ONLY = 3;
    //先访问网络,成功后缓存到本地
    public static final int NET_CACHE = 4;


    public String cacheKey;
    private Type mType;
    private Class mClazz;
    private int mCacheStrategy;

    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_ONLY, NET_CACHE})
    public @interface CacheStrategy {

    }


    public Request(String mUrl) {
        this.mUrl = mUrl;
    }

    public R addHeader(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    public R addParam(String key, Object value) {//value只能是八种基本类型和String
        try {
            Field field = value.getClass().getField("TYPE");
            Class clazz = (Class) field.get(null);
            if (clazz.isPrimitive()) {
                params.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    public R cacheKey(String key) {
        this.cacheKey = key;
        return (R) this;
    }


    protected Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeader(builder);
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiService.okHttpClient.newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    private void addHeader(okhttp3.Request.Builder builder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    //异步请求
    @SuppressLint("RestrictedApi")
    public void excute(final JsonCallBack<T> callBack) {
        if (mCacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ApiResponse<T> response = readCache();
                    if (callBack != null) {
                        callBack.onCacheSuccess(response);
                    }
                }
            });
        }

        if (mCacheStrategy != CACHE_ONLY) {
            getCall().enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    ApiResponse<T> response = new ApiResponse<>();
                    response.message = e.getMessage();
                    callBack.onError(response);

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    ApiResponse<T> apiResponse = parseResponse(response, callBack);
                    if (apiResponse.success) {
                        callBack.onError(apiResponse);
                    } else {
                        callBack.onSuccess(apiResponse);
                    }
                }
            });
        }
    }

    private ApiResponse<T> readCache() {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        Object cache = CacheManager.getCache(key);
        ApiResponse<T> result = new ApiResponse<>();
        result.status = 304;
        result.message = "缓存获取成功";
        result.body = (T) cache;
        result.success = true;
        return result;
    }

    public ApiResponse<T> excute() {
        if (mCacheStrategy == CACHE_ONLY) {
            return readCache();
        }

        try {
            Response response = getCall().execute();
            ApiResponse<T> apiResponse = parseResponse(response, null);
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public R cacheStrategy(@CacheStrategy int cacheStrategy) {
        mCacheStrategy = cacheStrategy;
        return (R) this;
    }


    private ApiResponse<T> parseResponse(Response response, JsonCallBack<T> callBack) {
        String message = null;
        int status = response.code();
        boolean successful = response.isSuccessful();
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert = ApiService.sConvert;
        try {
            String content = response.body() == null ? "" : new String(response.body().bytes());
            DebugLog.i("content: " + content);
            if (successful) {
                if (callBack != null) {
                    ParameterizedType type = (ParameterizedType) callBack.getClass().getGenericSuperclass();//获取泛型的实际类型
                    Type argument = type.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content, argument);
                } else if (mType != null) {
                    result.body = (T) convert.convert(content, mType);
                } else if (mClazz != null) {
                    result.body = (T) convert.convert(content, mClazz);
                } else {
                    Log.i("Request", "无法解析");
                }

            } else {
                message = content;
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            successful = false;
        }

        result.success = successful;
        result.status = status;
        result.message = message;
        //缓存操作
        if (mCacheStrategy != NET_ONLY && result.success && result.body != null && result.body instanceof Serializable) {
            save(result.body);
        }


        return result;

    }

    protected void save(T body) {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        CacheManager.save(key, body);
    }

    protected String generateCacheKey() {
        String key = UrlCreator.createUrlFromParams(mUrl, params);
        return key;
    }

    public R responseType(Type type) {
        mType = type;
        return (R) this;
    }


    public R responseType(Class clazz) {
        mClazz = clazz;
        return (R) this;
    }

    @NonNull
    @Override
    public Request clone() throws CloneNotSupportedException {
        return (Request<T, R>) super.clone();
    }

}
