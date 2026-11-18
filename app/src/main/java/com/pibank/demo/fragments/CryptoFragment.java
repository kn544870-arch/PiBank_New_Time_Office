package com.pibank.demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pibank.demo.R;
import com.pibank.demo.adapters.CryptoAdapter;
import com.pibank.demo.models.Crypto;
import com.pibank.demo.utils.FirebaseHelper;
import com.pibank.demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CryptoFragment extends Fragment {

    private RecyclerView cryptoRecyclerView;
    private CryptoAdapter cryptoAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView emptyText;
    private TextView totalBalanceText;
    private FirebaseHelper firebaseHelper;
    private List<Crypto> cryptoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crypto, container, false);

        firebaseHelper = FirebaseHelper.getInstance();
        cryptoList = new ArrayList<>();

        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadCrypto();

        return view;
    }

    private void initViews(View view) {
        cryptoRecyclerView = view.findViewById(R.id.cryptoRecyclerView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        emptyText = view.findViewById(R.id.emptyText);
        totalBalanceText = view.findViewById(R.id.totalBalanceText);
    }

    private void setupRecyclerView() {
        cryptoAdapter = new CryptoAdapter(cryptoList);
        cryptoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cryptoRecyclerView.setAdapter(cryptoAdapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(this::loadCrypto);
    }

    private void loadCrypto() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        firebaseHelper.getCryptoRef()
                .orderByChild("userId")
                .equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);

                        cryptoList.clear();
                        double totalValue = 0.0;

                        for (DataSnapshot cryptoSnapshot : snapshot.getChildren()) {
                            Crypto crypto = cryptoSnapshot.getValue(Crypto.class);
                            if (crypto != null) {
                                cryptoList.add(crypto);
                                totalValue += crypto.getTotalValueUSD();
                            }
                        }

                        cryptoAdapter.notifyDataSetChanged();
                        totalBalanceText.setText(Utils.formatCurrency(totalValue));

                        if (cryptoList.isEmpty()) {
                            emptyText.setVisibility(View.VISIBLE);
                            cryptoRecyclerView.setVisibility(View.GONE);
                        } else {
                            emptyText.setVisibility(View.GONE);
                            cryptoRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(getContext(), R.string.error_loading, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
