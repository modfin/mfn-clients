package se.mfn.client;

import com.google.gson.Gson;
import se.mfn.client.models.Feed;
import se.mfn.client.models.NewsItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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

    public List<NewsItem> getFeed() throws IOException {
        return getFeed(null);
    }

    public List<NewsItem> getFeed(Filter filter) throws IOException {

        StringBuilder url = new StringBuilder(this.baseUrl);
        url.append("/all/a.json");
        url.append("?.author.entity_id=").append(entityId);

        if (filter == null) {
            url.append("&type=all");
        } else {
            url.append(filter.value());
        }

        InputStream is = new URL(url.toString()).openStream();

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

    public NewsItem getNewsItem(NewsItem item) throws IOException {
        return getNewsItem(item.getNewsId());
    }

    public NewsItem getNewsItem(String newsId) throws IOException {
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


}
