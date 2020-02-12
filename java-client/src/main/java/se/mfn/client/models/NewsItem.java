
package se.mfn.client.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NewsItem {

    @SerializedName("news_id")
    @Expose
    private String newsId;

    @SerializedName("group_id")
    @Expose
    private String groupId;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("author")
    @Expose
    private Entity author;

    @SerializedName("subjects")
    @Expose
    private List<Entity> subjects = null;

    @SerializedName("properties")
    @Expose
    private Properties properties;

    @SerializedName("content")
    @Expose
    private Content content;

    @SerializedName("extensions")
    @Expose
    private Map extensions;

    @SerializedName("source")
    @Expose
    private String source;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Entity getAuthor() {
        return author;
    }

    public void setAuthor(Entity author) {
        this.author = author;
    }

    public List<Entity> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Entity> subjects) {
        this.subjects = subjects;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Map getExtensions() {
        return extensions;
    }

    public void setExtensions(Map extensions) {
        this.extensions = extensions;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    @Override
    public String toString() {
        return "NewsItem{" +
                "newsId='" + newsId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", url='" + url + '\'' +
                ", author=" + author +
                ", subjects=" + subjects +
                ", properties=" + properties +
                ", content=" + content +
                ", extensions=" + extensions +
                ", source='" + source + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsItem newsItem = (NewsItem) o;
        return Objects.equals(newsId, newsItem.newsId) &&
                Objects.equals(groupId, newsItem.groupId) &&
                Objects.equals(url, newsItem.url) &&
                Objects.equals(author, newsItem.author) &&
                Objects.equals(subjects, newsItem.subjects) &&
                Objects.equals(properties, newsItem.properties) &&
                Objects.equals(content, newsItem.content) &&
                Objects.equals(extensions, newsItem.extensions) &&
                Objects.equals(source, newsItem.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsId, groupId, url, author, subjects, properties, content, extensions, source);
    }
}
