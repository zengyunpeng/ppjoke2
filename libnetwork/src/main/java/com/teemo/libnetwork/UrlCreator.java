package com.teemo.libnetwork;

import java.net.URLEncoder;
import java.util.Map;

public class UrlCreator {
    public static String createUrlFromParams(String url, Map<String, Object> map) {
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            builder.append("&");
        } else {
            builder.append("?");
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                String value = URLEncoder.encode(String.valueOf(entry.getValue()));
                builder.append(entry.getKey()).append("=").append(value).append("&");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();

    }
}
