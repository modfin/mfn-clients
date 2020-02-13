package se.mfn.client;

import com.google.gson.Gson;
import se.mfn.client.models.Feed;
import se.mfn.client.models.NewsItem;
import se.mfn.client.models.Type;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private final String baseUrl;
    private final String entityId;

    public Client(String baseUrl, String entityId) {
        this.baseUrl = baseUrl;
        this.entityId = entityId;
    }

    public Filter feed() {
        StringBuilder url = new StringBuilder(this.baseUrl);
        url.append("/all/a.json");
        url.append("?.author.entity_id=").append(entityId);

        return new Filter(url.toString());
    }



    public NewsItem newsItem(String newsSlug) throws IOException {
        // Todo check that newsId is a uuid

        StringBuilder url = new StringBuilder(this.baseUrl);
        url.append("/all/a.json").append("?type=all");
        url.append("&.author.entity_id=").append(entityId);
        url.append("&news_slug=").append(newsSlug);

        InputStream is = new URL(url.toString()).openStream();

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            Gson gson = new Gson();
            Feed feed = gson.fromJson(rd, Feed.class);
            if (feed.getItems() == null || feed.getItems().size() != 1) {
                return null;
            }

            return feed.getItems().get(0);
        } finally {
            is.close();
        }
    }

    public NewsItem newsItemById(NewsItem item) throws IOException {
        return newsItemById(item.getNewsId());
    }

    public NewsItem newsItemById(String newsId) throws IOException {
        // Todo check that newsId is a uuid

        StringBuilder url = new StringBuilder(this.baseUrl);
        url.append("/all/a.json").append("?type=all");
        url.append("&.author.entity_id=").append(entityId);
        url.append("&news_id=").append(newsId);

        InputStream is = new URL(url.toString()).openStream();

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            Gson gson = new Gson();
            Feed feed = gson.fromJson(rd, Feed.class);
            if (feed.getItems() == null || feed.getItems().size() != 1) {
                return null;
            }

            return feed.getItems().get(0);
        } finally {
            is.close();
        }
    }



    public static class Filter {
        private final String url;
        private int limit;
        private int offset;
        private Type type;
        private String lang;

        private int year;
        private List<String> tags;
        private String query;

        private Filter(String url) {
            this.url = url;
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

        private String value() throws UnsupportedEncodingException {
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

        public List<NewsItem> fetch() throws IOException {

            InputStream is = new URL(url + value()).openStream();

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                Gson gson = new Gson();
                Feed feed = gson.fromJson(rd, Feed.class);
                if (feed.getItems() == null) {
                    return new ArrayList<>();
                }
                return feed.getItems();
            } finally {
                is.close();
            }
        }

    }
}
