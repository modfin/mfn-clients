package se.mfn.client.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Feed {

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("feed_url")
    @Expose
    private String feedURL;

    @SerializedName("home_page_url")
    @Expose
    private String homePageURL;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("next_url")
    @Expose
    private String nextURL;

    @SerializedName("items")
    @Expose
    private List<NewsItem> items;


    public String getFeedURL() {
        return feedURL;
    }

    public void setFeedURL(String feedURL) {
        this.feedURL = feedURL;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHomePageURL() {
        return homePageURL;
    }

    public void setHomePageURL(String homePageURL) {
        this.homePageURL = homePageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNextURL() {
        return nextURL;
    }

    public void setNextURL(String nextURL) {
        this.nextURL = nextURL;
    }

    public List<NewsItem> getItems() {
        return items;
    }

    public void setItems(List<NewsItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "version='" + version + '\'' +
                ", title='" + title + '\'' +
                ", feedURL='" + feedURL + '\'' +
                ", homePageURL='" + homePageURL + '\'' +
                ", description='" + description + '\'' +
                ", nextURL='" + nextURL + '\'' +
                ", items=" + items +
                '}';
    }
}
