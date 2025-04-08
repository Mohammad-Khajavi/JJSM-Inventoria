package com.example.jjsminventoria;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jjsminventoria.database.FirebaseConnection;
import com.example.jjsminventoria.ui.dashboard.DashboardFragment;
import com.example.jjsminventoria.ui.inventory.InventoryHomeViewFragment;
import com.example.jjsminventoria.ui.profile.ProfileFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jjsminventoria.databinding.ActivityMainMenuBinding;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFS_NAME = "MyAppPrefs";

    private ActivityMainMenuBinding binding;
    private TextView tvLogout;
    private LinearLayout dashboardTab, inventoryTab, profileTab;
    public FirebaseConnection firebaseConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
// Toolbar is somehow need for the app to work need to take out or can add some styling to it if we gonna keep
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_menu_bottom_tabs);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        initialize();
    }

    private void initialize(){
        tvLogout = findViewById(R.id.tvLogout);
        dashboardTab = findViewById(R.id.dashboardTab);
        inventoryTab = findViewById(R.id.inventoryTab);
        profileTab = findViewById(R.id.profileTab);

        tvLogout.setOnClickListener(this);
        dashboardTab.setOnClickListener(this);
        inventoryTab.setOnClickListener(this);
        profileTab.setOnClickListener(this);
        loadFragment(new DashboardFragment());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvLogout) logout();
        else if (id == R.id.dashboardTab)
            loadFragment(new DashboardFragment());
        else if (id == R.id.inventoryTab)
            loadFragment(new InventoryHomeViewFragment());
        else if (id == R.id.profileTab)
            loadFragment(new ProfileFragment());
    }

    private void logout() {
        FirebaseConnection.getInstance().logout();
        GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main_menu_bottom_tabs, fragment)
                .commit();
    }
}