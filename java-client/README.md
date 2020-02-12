


# Example of use
```java 
import se.mfn.client.Client;
import se.mfn.client.Filter;

import se.mfn.client.models.NewsItem;
import se.mfn.client.models.Type;

import java.io.IOException;
import java.util.List;

public class Example {
    public static void main(String args[]) throws IOException {

        // Add the base url and entity id of the feed
        Client client = new Client("https://mfn.se", "2c07a2db-2f22-4a67-ab46-ccb464296638");

        // The latest item
        List<NewsItem> items = client.getFeed();
        System.out.println(" 1: " + items.get(0));

        // Only items from 2019
        items = client.getFeed(Filter.create()
                .year(2019));
        System.out.println(" 2: " + items.get(0));

        // Only items from 2019 that are Public relationship
        items = client.getFeed(Filter.create()
                .year(2019)
                .type(Type.PR));
        System.out.println(" 3: " + items.get(0));

        // Only items from 2019 that are Investor relationship
        items = client.getFeed(Filter.create()
                .year(2019)
                .type(Type.IR));
        System.out.println(" 4: " + items.get(0));

        // Only items from 2019 that are Investor relationship and has the tag sub:ca
        items = client.getFeed(Filter.create()
                .year(2019)
                .type(Type.IR)
                .hasTag("sub:ca"));
        System.out.println(" 5: " + items.get(0));

        // Only items from 2019 that are Investor relationship and has the tag sub:ca in English
        items = client.getFeed(Filter.create()
                .year(2019)
                .type(Type.IR)
                .hasTag("sub:ca")
                .lang("en"));
        System.out.println(" 6: " + items.get(0));

        // Only items from 2019 that are Investor relationship and has the tag sub:ca in English
        // containg the text "financial technology"
        items = client.getFeed(Filter.create()
                .year(2019)
                .type(Type.IR)
                .hasTag("sub:ca")
                .lang("en")
                .query("financial technology"));
        System.out.println(" 7: " + items.get(0));

        // Get specific item
        NewsItem item = client.getNewsItem("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");
        System.out.println(" 8: " + item);
    }
}

```