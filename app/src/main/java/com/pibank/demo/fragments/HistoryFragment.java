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
import com.pibank.demo.adapters.TransactionsAdapter;
import com.pibank.demo.models.Transaction;
import com.pibank.demo.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView transactionsRecyclerView;
    private TransactionsAdapter transactionsAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView emptyText;
    private FirebaseHelper firebaseHelper;
    private List<Transaction> transactionsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        firebaseHelper = FirebaseHelper.getInstance();
        transactionsList = new ArrayList<>();

        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadTransactions();

        return view;
    }

    private void initViews(View view) {
        transactionsRecyclerView = view.findViewById(R.id.transactionsRecyclerView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        emptyText = view.findViewById(R.id.emptyText);
    }

    private void setupRecyclerView() {
        transactionsAdapter = new TransactionsAdapter(transactionsList);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionsRecyclerView.setAdapter(transactionsAdapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(this::loadTransactions);
    }

    private void loadTransactions() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        firebaseHelper.getTransactionsRef()
                .orderByChild("userId")
                .equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);

                        transactionsList.clear();
                        for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                            Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                            if (transaction != null) {
                                transactionsList.add(transaction);
                            }
                        }

                        Collections.reverse(transactionsList);
                        transactionsAdapter.notifyDataSetChanged();

                        if (transactionsList.isEmpty()) {
                            emptyText.setVisibility(View.VISIBLE);
                            transactionsRecyclerView.setVisibility(View.GONE);
                        } else {
                            emptyText.setVisibility(View.GONE);
                            transactionsRecyclerView.setVisibility(View.VISIBLE);
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
