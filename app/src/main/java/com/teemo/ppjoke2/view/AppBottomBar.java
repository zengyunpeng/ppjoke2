package com.teemo.ppjoke2.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.teemo.ppjoke2.R;
import com.teemo.ppjoke2.model.BottomBar;
import com.teemo.ppjoke2.model.Destination;
import com.teemo.ppjoke2.utils.AppConfig;

import java.util.List;

public class AppBottomBar extends BottomNavigationView {
    private static int[] sIcons = new int[]{R.drawable.icon_tab_home, R.drawable.icon_tab_sofa, R.drawable.icon_tab_publish, R.drawable.icon_tab_find, R.drawable.icon_tab_mine};


    public AppBottomBar(Context context) {
        this(context, null);
        Log.i("tt", "执行AppBottomBar构造方法1");
    }

    public AppBottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.i("tt", "执行AppBottomBar构造方法2");
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BottomBar bottomBar = AppConfig.getBottomBar();
        List<BottomBar.TabsBean> tabs = bottomBar.getTabs();
        Log.i("tt", "执行AppBottomBar构造方法3");

        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = new int[]{Color.parseColor(bottomBar.getActiveColor()), Color.parseColor(bottomBar.getInActiveColor())};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
//        setSelectedItemId(bottomBar.get)
        //数据填充
        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.TabsBean tabsBean = tabs.get(i);
            Log.i("tt", "!tabsBean.isEnable(): " + !tabsBean.isEnable());
            if (!tabsBean.isEnable()) {
                continue;
            }
            int id = getId(tabsBean.getPageUrl());
            Log.i("tt", "id: " + id);
            if (id < 0) {
                continue;
            }
            Log.i("tt", "添加了一个tab: " + tabsBean.toString());
            MenuItem menuItem = getMenu().add(0, id, tabsBean.getIndex(), tabsBean.getTitle());
            menuItem.setIcon(sIcons[tabsBean.getIndex()]);
        }
        //界面渲染
        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.TabsBean tabsBean = tabs.get(i);
            int iconSize = dp2px(tabsBean.getSize());
            BottomNavigationMenuView bottomNavigationMenuView =
                    (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView bottomNavigationItemView =
                    (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(i);
            bottomNavigationItemView.setIconSize(iconSize);

            if (TextUtils.isEmpty(tabsBean.getTitle())) {
                int tintColor = TextUtils.isEmpty(tabsBean.getTintColor()) ? Color.parseColor("#ff678f") : Color.parseColor(tabsBean.getTintColor());
                bottomNavigationItemView.setIconTintList(ColorStateList.valueOf(tintColor));
                bottomNavigationItemView.setShifting(false);
            }

        }
    }

    private int dp2px(int size) {
        return (int) (getContext().getResources().getDisplayMetrics().density * size + 0.5f);
    }

    private int getId(String pageUrl) {
        Destination destination = AppConfig.getDestConfig().get(pageUrl);
        if (destination == null) {
            return -1;
        }
        return destination.getId();
    }


}
