package com.maxrescuerinc.myandroidapplication.Models;

public class NewsItem {
    public String Title = null;
    public String Description = null;
    public String Link = null;
    public String PubDate =null;
    public String ImageUrl = null;

    public NewsItem(String title, String link, String description, String pubDate, String imageUrl) {
        this.Title = title;
        this.Link = link;
        this.Description = description;
        this.PubDate = pubDate;
        this.ImageUrl = imageUrl;
    }
}
