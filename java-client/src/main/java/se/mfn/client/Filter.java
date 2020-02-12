package se.mfn.client;


import se.mfn.client.models.Type;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Filter {
    private int limit;
    private int offset;
    private Type type;
    private String lang;

    private int year;
    private List<String> tags;
    private String query;

    private Filter() {
        this.limit = 25;
        this.offset = 0;
        this.tags = new ArrayList<>();
        this.type = Type.ALL;
    }

    public Filter limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Filter offset(int offset) {
        this.offset = offset;
        return this;
    }

    public Filter lang(String lang) {
        this.lang = lang;
        return this;
    }

    public Filter year(int year) {
        this.year = year;
        return this;
    }

    public Filter query(String query) {
        this.query = query;
        return this;
    }

    public Filter type(Type type) {
        this.type = type;
        return this;
    }

    public Filter hasTag(String tag) {
        tags.add(tag);
        return this;
    }

    public String value() throws UnsupportedEncodingException {
        StringBuilder b = new StringBuilder();

        b.append("&limit=").append(this.limit);
        b.append("&offset=").append(this.offset);
        b.append("&type=").append(this.type.value());

        if (this.lang != null && this.lang.length() == 2) {
            b.append("&lang=").append(this.lang);
        }
        if (1900 < this.year && this.year < 2100) {
            b.append("&from=").append(year).append("-01-01T00%3A00%3A00Z");
            b.append("&to=").append(year).append("-12-31T23%3A59%3A59Z");
        }

        if (this.tags.size() > 0) {
            for (String tag : this.tags) {
                b.append("&tag=").append(URLEncoder.encode(tag, StandardCharsets.UTF_8.toString()));
            }
        }

        if(this.query != null && this.query.length() > 3){
            b.append("&query=").append(URLEncoder.encode(this.query, StandardCharsets.UTF_8.toString()));
        }

        return b.toString();
    }

    public static Filter create(){
        return new Filter();
    }
}



