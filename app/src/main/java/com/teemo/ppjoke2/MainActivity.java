package com.teemo.ppjoke2;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teemo.ppjoke2.model.Destination;
import com.teemo.ppjoke2.model.User;
import com.teemo.ppjoke2.utils.AppConfig;
import com.teemo.ppjoke2.utils.NavGraphBuilder;
import com.teemo.ppjoke2.utils.UserManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    NavController navController;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

        NavGraphBuilder.build(navController, this, fragment.getId());
        //关联点击事件和导航
        navView.setOnNavigationItemSelectedListener(this);


//        GetRequest<Object> request = new GetRequest<>("http://www.mooc.com");
//        request.excute();
//        request.excute(new JsonCallBack<Object>() {
//            @Override
//            public void onSuccess(ApiResponse<Object> response) {
//                super.onSuccess(response);
//            }
//        });
        FragmentTransaction transaction = null;


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        Iterator<Map.Entry<String, Destination>> iterator = destConfig.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Destination> next = iterator.next();
            Destination value = next.getValue();
            if (value != null && value.isNeedLogin()) {
                UserManager.get().login(this).observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        navView.setSelectedItemId(menuItem.getItemId());
                    }
                });
            }
        }

        navController.navigate(menuItem.getItemId());
        return TextUtils.isEmpty(menuItem.getTitle());//这里返回true会有选中效果,否则没有选中效果
    }
}
