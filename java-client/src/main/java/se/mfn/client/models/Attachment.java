package se.mfn.client.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Attachment {

    @SerializedName("file_title")
    @Expose
    private String fileTitle;

    @SerializedName("content_type")
    @Expose
    private String contentType;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("tags")
    @Expose
    private List<String> tags = null;

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "fileTitle='" + fileTitle + '\'' +
                ", contentType='" + contentType + '\'' +
                ", url='" + url + '\'' +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(fileTitle, that.fileTitle) &&
                Objects.equals(contentType, that.contentType) &&
                Objects.equals(url, that.url) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileTitle, contentType, url, tags);
    }
}
