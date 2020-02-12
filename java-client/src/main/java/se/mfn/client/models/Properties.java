package se.mfn.client.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Properties {

    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("tags")
    @Expose
    private List<String> tags;

    @SerializedName("type")
    @Expose
    private Type type;

    @SerializedName("scopes")
    @Expose
    private List<String> scopes;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "lang='" + lang + '\'' +
                ", tags=" + tags +
                ", type=" + type +
                ", scopes=" + scopes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Properties that = (Properties) o;
        return Objects.equals(lang, that.lang) &&
                Objects.equals(tags, that.tags) &&
                type == that.type &&
                Objects.equals(scopes, that.scopes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lang, tags, type, scopes);
    }
}
