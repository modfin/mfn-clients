package se.mfn.client.models;

import com.google.gson.annotations.SerializedName;

public enum Type {
    @SerializedName("ir")
    IR("ir"),

    @SerializedName("pr")
    PR("pr"),

    @SerializedName("all")
    ALL("all");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return "Type{" +
                "value='" + value + '\'' +
                '}';
    }


}
