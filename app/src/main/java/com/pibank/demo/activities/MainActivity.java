package com.pibank.demo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pibank.demo.R;
import com.pibank.demo.fragments.CardsFragment;
import com.pibank.demo.fragments.CryptoFragment;
import com.pibank.demo.fragments.HistoryFragment;
import com.pibank.demo.fragments.HomeFragment;
import com.pibank.demo.fragments.NewsFragment;
import com.pibank.demo.utils.FirebaseHelper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseHelper = FirebaseHelper.getInstance();

        initViews();
        checkAdminStatus();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setOnItemSelectedListener(navListener);
    }

    private void checkAdminStatus() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId != null) {
            firebaseHelper.getUsersRef().child(userId).child("isAdmin")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Boolean isAdmin = snapshot.getValue(Boolean.class);
                            if (isAdmin != null && isAdmin) {
                                bottomNavigation.getMenu().add(0, R.id.nav_admin, 5, "Admin")
                                        .setIcon(R.drawable.ic_admin);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
        }
    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_cards) {
            selectedFragment = new CardsFragment();
        } else if (itemId == R.id.nav_history) {
            selectedFragment = new HistoryFragment();
        } else if (itemId == R.id.nav_news) {
            selectedFragment = new NewsFragment();
        } else if (itemId == R.id.nav_crypto) {
            selectedFragment = new CryptoFragment();
        } else if (itemId == R.id.nav_admin) {
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
            return true;
        }

        if (selectedFragment != null) {
            loadFragment(selectedFragment);
        }

        return true;
    };

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (bottomNavigation.getSelectedItemId() == R.id.nav_home) {
            super.onBackPressed();
        } else {
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }
}
