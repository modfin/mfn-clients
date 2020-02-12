package se.mfn.client.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Content {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("publish_date")
    @Expose
    private Date publishDate;

    @SerializedName("preamble")
    @Expose
    private String preamble;

    @SerializedName("html")
    @Expose
    private String html;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("attachments")
    @Expose
    private List<Attachment> attachments = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPreamble() {
        return preamble;
    }

    public void setPreamble(String preamble) {
        this.preamble = preamble;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }


    @Override
    public String toString() {
        return "Content{" +
                "title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", preamble_len='" + preamble.length() + '\'' +
                ", html_len='" + html.length() + '\'' +
                ", text_len='" + text.length() + '\'' +
                ", attachments=" + attachments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(title, content.title) &&
                Objects.equals(slug, content.slug) &&
                Objects.equals(publishDate, content.publishDate) &&
                Objects.equals(preamble, content.preamble) &&
                Objects.equals(html, content.html) &&
                Objects.equals(text, content.text) &&
                Objects.equals(attachments, content.attachments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, slug, publishDate, preamble, html, text, attachments);
    }
}
