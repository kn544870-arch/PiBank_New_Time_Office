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
import com.pibank.demo.adapters.NewsAdapter;
import com.pibank.demo.models.News;
import com.pibank.demo.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView emptyText;
    private FirebaseHelper firebaseHelper;
    private List<News> newsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        firebaseHelper = FirebaseHelper.getInstance();
        newsList = new ArrayList<>();

        initViews(view);
        setupRecyclerView();
        setupListeners();
        loadNews();

        return view;
    }

    private void initViews(View view) {
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        progressBar = view.findViewById(R.id.progressBar);
        emptyText = view.findViewById(R.id.emptyText);
    }

    private void setupRecyclerView() {
        newsAdapter = new NewsAdapter(newsList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecyclerView.setAdapter(newsAdapter);
    }

    private void setupListeners() {
        swipeRefresh.setOnRefreshListener(this::loadNews);
    }

    private void loadNews() {
        if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
        }

        firebaseHelper.getNewsRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.GONE);
                        swipeRefresh.setRefreshing(false);

                        newsList.clear();
                        for (DataSnapshot newsSnapshot : snapshot.getChildren()) {
                            News news = newsSnapshot.getValue(News.class);
                            if (news != null) {
                                newsList.add(news);
                            }
                        }

                        Collections.reverse(newsList);
                        newsAdapter.notifyDataSetChanged();

                        if (newsList.isEmpty()) {
                            emptyText.setVisibility(View.VISIBLE);
                            newsRecyclerView.setVisibility(View.GONE);
                        } else {
                            emptyText.setVisibility(View.GONE);
                            newsRecyclerView.setVisibility(View.VISIBLE);
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
