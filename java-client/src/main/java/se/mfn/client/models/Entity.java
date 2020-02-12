package se.mfn.client.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Entity {

    @SerializedName("entity_id")
    @Expose
    private String entityId;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("slugs")
    @Expose
    private List<String> slugs = null;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("brand_image_url")
    @Expose
    private String brandImageUrl;

    @SerializedName("isins")
    @Expose
    private List<String> isins = null;

    @SerializedName("leis")
    @Expose
    private List<String> leis = null;

    @SerializedName("local_refs")
    @Expose
    private List<String> localRefs = null;

    @SerializedName("tickers")
    @Expose
    private List<String> tickers = null;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<String> getSlugs() {
        return slugs;
    }

    public void setSlugs(List<String> slugs) {
        this.slugs = slugs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandImageUrl() {
        return brandImageUrl;
    }

    public void setBrandImageUrl(String brandImageUrl) {
        this.brandImageUrl = brandImageUrl;
    }

    public List<String> getIsins() {
        return isins;
    }

    public void setIsins(List<String> isins) {
        this.isins = isins;
    }

    public List<String> getLeis() {
        return leis;
    }

    public void setLeis(List<String> leis) {
        this.leis = leis;
    }

    public List<String> getLocalRefs() {
        return localRefs;
    }

    public void setLocalRefs(List<String> localRefs) {
        this.localRefs = localRefs;
    }

    public List<String> getTickers() {
        return tickers;
    }

    public void setTickers(List<String> tickers) {
        this.tickers = tickers;
    }


    @Override
    public String toString() {
        return "Entity{" +
                "entityId='" + entityId + '\'' +
                ", slug='" + slug + '\'' +
                ", slugs=" + slugs +
                ", name='" + name + '\'' +
                ", brandImageUrl='" + brandImageUrl + '\'' +
                ", isins=" + isins +
                ", leis=" + leis +
                ", localRefs=" + localRefs +
                ", tickers=" + tickers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(entityId, entity.entityId) &&
                Objects.equals(slug, entity.slug) &&
                Objects.equals(slugs, entity.slugs) &&
                Objects.equals(name, entity.name) &&
                Objects.equals(brandImageUrl, entity.brandImageUrl) &&
                Objects.equals(isins, entity.isins) &&
                Objects.equals(leis, entity.leis) &&
                Objects.equals(localRefs, entity.localRefs) &&
                Objects.equals(tickers, entity.tickers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, slug, slugs, name, brandImageUrl, isins, leis, localRefs, tickers);
    }
}
