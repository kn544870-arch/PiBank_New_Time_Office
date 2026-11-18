package com.pibank.demo.models;

public class News {
    private String newsId;
    private String title;
    private String description;
    private String link;
    private long timestamp;

    public News() {
    }

    public News(String newsId, String title, String description, String link) {
        this.newsId = newsId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.timestamp = System.currentTimeMillis();
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
