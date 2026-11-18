package com.pibank.demo.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pibank.demo.R;
import com.pibank.demo.models.News;
import com.pibank.demo.utils.Utils;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.bind(news);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;
        private final TextView descriptionText;
        private final TextView dateText;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            dateText = itemView.findViewById(R.id.dateText);
        }

        public void bind(News news) {
            titleText.setText(news.getTitle());
            descriptionText.setText(news.getDescription());
            dateText.setText(Utils.formatDateShort(news.getTimestamp()));

            itemView.setOnClickListener(v -> {
                if (news.getLink() != null && !news.getLink().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getLink()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
