package com.teemo.ppjoke2.utils;

import android.content.ComponentName;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.teemo.libcommon.AppGloble;
import com.teemo.ppjoke2.model.Destination;

import java.util.HashMap;

public class NavGraphBuilder {
    public static void build(NavController controller, FragmentActivity fragmentActivity, int containerId) {
        NavigatorProvider navigatorProvider = controller.getNavigatorProvider();

//        FragmentNavigator fragmentNavigator = navigatorProvider.getNavigator(FragmentNavigator.class);
        //替换成自定义的Fragment导航器
        FragmentNavigator fragmentNavigator = new FixFragmentNavigator(fragmentActivity, fragmentActivity.getSupportFragmentManager(), containerId);
        navigatorProvider.addNavigator(fragmentNavigator);

        ActivityNavigator activityNavigator = navigatorProvider.getNavigator(ActivityNavigator.class);


        NavGraph navGraph = new NavGraph(new NavGraphNavigator((navigatorProvider)));

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        Log.i("tag", "destConfig: " + destConfig.toString());

        for (Destination value : destConfig.values()) {
            if (value.isIsFragment()) {
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setClassName(value.getClassName());
                destination.setId(value.getId());
                destination.addDeepLink(value.getPageUrl());

                navGraph.addDestination(destination);
            } else {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(value.getId());
                destination.addDeepLink(value.getPageUrl());
                destination.setComponentName(new ComponentName(AppGloble.getApplication().getPackageName(), value.getClassName()));

                navGraph.addDestination(destination);
            }
            Log.i("tag", "不作为起始页面被添加: " + value.toString());
            if (value.isAsStarter()) {
                Log.i("tag", "作为起始页面被添加: " + value.toString());
                navGraph.setStartDestination(value.getId());
            }
        }

        controller.setGraph(navGraph);
    }
}
