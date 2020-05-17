package com.bot.onlinejob.provider.newWorks;

public class providerwork {

    private String Title;
    private String Category ;
    private int Thumbnail ;

    public providerwork() {
    }

    public providerwork(String title, String category, int thumbnail) {
        Title = title;
        Category = category;
        Thumbnail = thumbnail;
    }


    public String getTitle() {
        return Title;
    }

    public String getCategory() {
        return Category;
    }


    public int getThumbnail() {
        return Thumbnail;
    }


    public void setTitle(String title) {
        Title = title;
    }


    public void setCategory(String category) {
        Category = category;
    }



    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }
}