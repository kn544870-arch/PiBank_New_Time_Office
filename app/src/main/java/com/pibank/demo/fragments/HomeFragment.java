package com.pibank.demo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pibank.demo.R;
import com.pibank.demo.activities.LoginActivity;
import com.pibank.demo.models.User;
import com.pibank.demo.utils.FirebaseHelper;
import com.pibank.demo.utils.Utils;

public class HomeFragment extends Fragment {

    private TextView welcomeText;
    private TextView balanceText;
    private TextView emailText;
    private Button logoutButton;
    private ProgressBar progressBar;
    private FirebaseHelper firebaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseHelper = FirebaseHelper.getInstance();

        initViews(view);
        setupListeners();
        loadUserData();

        return view;
    }

    private void initViews(View view) {
        welcomeText = view.findViewById(R.id.welcomeText);
        balanceText = view.findViewById(R.id.balanceText);
        emailText = view.findViewById(R.id.emailText);
        logoutButton = view.findViewById(R.id.logoutButton);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        logoutButton.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) {
            logout();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseHelper.getUsersRef().child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            displayUserData(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayUserData(User user) {
        welcomeText.setText("Привет, " + user.getName() + "!");
        balanceText.setText(Utils.formatCurrency(user.getBalance()));
        emailText.setText(user.getEmail());
    }

    private void logout() {
        firebaseHelper.getAuth().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
