package se.mfn.client;

import org.junit.jupiter.api.Test;
import se.mfn.client.models.NewsItem;
import se.mfn.client.models.Type;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFeed {


    static Client createClient() {
        return new Client("https://feed.mfn.se/v1", "2c07a2db-2f22-4a67-ab46-ccb464296638");
    }

    static Client createClientThunderfulGroup() {
        return new Client("https://feed.mfn.se/v1", "94334e99-00f2-4c98-b5a5-314a8c73fbfe");
    }

    static Client createClientIntrum() {
        return new Client("https://feed.mfn.se/v1", "d7d583cc-c99d-4152-baa9-bbd89061d9e6");
    }

    @Test
    public void testFeedItems() throws IOException {
        Client c = createClient();
        List<NewsItem> items = c.feed().fetch();
        assertTrue(items.size() >= 10);
    }

    @Test
    public void testSingleItemIR() throws IOException {

        Client c = createClient();
        List<NewsItem> items = c.feed().type(Type.IR).fetch();
        assertTrue(items.size() > 0);

        NewsItem item0 = items.get(0);
        assertEquals(item0.getProperties().getType(), Type.IR);

        NewsItem item1 = c.itemById(item0);
        NewsItem item2 = c.itemById(item0.getNewsId());
        NewsItem item3 = c.item(item0.getContent().getSlug());


        assertTrue(item0.equals(item1) && item1.equals(item2) && item2.equals(item3));
    }

    @Test
    public void testSingleItemPR() throws IOException {

        Client c = createClient();
        List<NewsItem> items = c.feed().type(Type.PR).fetch();
        assertTrue(items.size() > 0);

        NewsItem item0 = items.get(0);
        assertEquals(item0.getProperties().getType(), Type.PR);

        NewsItem item1 = c.itemById(item0);
        NewsItem item2 = c.itemById(item0.getNewsId());
        NewsItem item3 = c.item(item0.getContent().getSlug());
        assertEquals(item0, item1);
        assertEquals(item0, item2);
        assertEquals(item0, item3);

    }

    @Test
    public void testFilterEmpty() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed().fetch();
        assertTrue(0 < items0.size());
    }

    @Test
    public void testFilterLimit() throws IOException {
        Client c = createClient();
        List<NewsItem> items = c.feed()
                .limit(5)
                .fetch();
        assertEquals(5, items.size());
    }

    @Test
    public void testFilterOffset() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .limit(5)
                .offset(0)
                .fetch();
        assertEquals(5, items0.size());

        List<NewsItem> items1 = c.feed()
                .limit(5)
                .offset(4)
                .fetch();
        assertEquals(5, items1.size());

        NewsItem i1l = items0.get(4);
        NewsItem i2f = items1.get(0);

        assertEquals(i1l, i2f);
    }


    @Test
    public void testFilterPR() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .type(Type.PR)
                .fetch();
        assertTrue(items0.size() > 0);


        for (NewsItem item : items0) {
            assertSame(item.getProperties().getType(), Type.PR);
        }

    }


    @Test
    public void testFilterPRFetch() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .type(Type.PR)
                .fetch();
        assertTrue(items0.size() > 0);

        NewsItem item0 = items0.get(0);

        NewsItem item1 = c.itemById(item0);

        assertEquals(item0, item1);
    }

    @Test
    public void testFilterYear() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .type(Type.PR)
                .year(2019)
                .fetch();
        assertEquals(8, items0.size());

        NewsItem item0 = items0.get(0);
        NewsItem item1 = c.itemById(item0);

        assertEquals(item0, item1);
    }

    @Test
    public void testFilterYear2() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .year(2018)
                .fetch();

        assertEquals(0, items0.size());
    }

    @Test
    public void testFilterLang() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .year(2020)
                .type(Type.IR)
                .lang("sv")
                .fetch();

        assertEquals(1, items0.size());
        for (NewsItem item: items0) {
            assertEquals(item.getProperties().getLang(), "sv");
        }

        items0 = c.feed()
                .year(2019)
                .lang("en")
                .fetch();
        for (NewsItem item: items0) {
            assertEquals(item.getProperties().getLang(), "en");
        }
        assertEquals(4, items0.size());
    }

    @Test
    public void testFilterTags() throws IOException {
        Client c = createClientIntrum();
        List<NewsItem> items0 = c.feed()
                .year(2023)
                .hasTag("sub:report")
                .fetch();

        assertEquals(10, items0.size());
        for (NewsItem item : items0) {
            assertTrue(item.getProperties().getTags().contains(":regulatory"));
        }

    }

    @Test
    public void testCorrection() throws IOException {
        Client c = createClientThunderfulGroup();
        NewsItem item = c.itemById("c50e4c61-c49f-440b-815b-daaca5336822");

        assertTrue(item.getProperties().getTags().contains(":correction"));
        assertTrue(item.getProperties().getTags().contains(":correction:9ee6a296-0db6-4137-a7c7-14c9a5579953"));
    }

    @Test
    public void testFilterQuery() throws IOException {
        Client c = createClientThunderfulGroup();
        List<NewsItem> items0 = c.feed()
                .year(2023)
                .query("genomför")
                .fetch();

        assertEquals(1, items0.size());
        assertEquals("Thunderful Group har genomfört två mindre tilläggsförvärv", items0.get(0).getContent().getTitle());
    }


    @Test
    public void testContent() throws IOException, ParseException {
        Client c = createClient();
        NewsItem item = c.itemById("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");

        assertEquals("b660f6cc-5d7e-4cab-8862-3271b649a636", item.getGroupId());
        assertEquals("https://feed.mfn.se/v1/feed/2c07a2db-2f22-4a67-ab46-ccb464296638/item/a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3.html", item.getUrl());
        assertEquals("2c07a2db-2f22-4a67-ab46-ccb464296638", item.getAuthor().getEntityId());
        assertEquals(1, item.getSubjects().size());
        assertEquals("2c07a2db-2f22-4a67-ab46-ccb464296638", item.getSubjects().get(0).getEntityId());
        assertEquals(item.getAuthor(), item.getSubjects().get(0));

        assertEquals("Modular Finance launches a new IRM in Monitor", item.getContent().getTitle());
        assertEquals("modular-finance-launches-a-new-irm-in-monitor", item.getContent().getSlug());


        assertEquals("en", item.getProperties().getLang());
        assertEquals(Type.PR, item.getProperties().getType());

        assertArrayEquals(new String[]{"cus:monitor", "cus:modular finance"}, item.getProperties().getTags().toArray());

        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse("2019-11-23T14:30:00Z");
        assertEquals(date, item.getContent().getPublishDate());
        assertEquals("After a considerable time of product development, in close co-operation with our " +
                        "customers, we now launch a new IRM in Monitor. Investor Relationship Management assists listed " +
                        "companies in making their IR-work more effective and easier to follow up.",
                item.getContent().getPreamble()
        );
        assertEquals("<div class=\"mfn-preamble\"><p><strong>After a considerable time of product development, in close co-operation with our customers, we now launch a new IRM in Monitor. Investor Relationship Management assists listed companies in making their IR-work more effective and easier to follow up.</strong></p></div>\n" +
                        "<div class=\"mfn-body\"><p>Monitor has since the start provided unique data and functionailty in regards to ownership information. The platform assists listed companies in the Nordics to dynamicailly and on an ongoing basis identify and track shareholders that otherwise are hard to identify. Since the launch in 2016 the platform has been broadened and also includes complete functionality for Investor Targeting, data for reports, a mobile app with notifications and now, a complete and state of the art IRM.</p><blockquote><em>I am very proud of the team and its efforts in this development. In my view Monitor is now by far the most complete and potent IR-product in the Nordics</em>, says Petter Hedborg, CEO of Modular Finance.</blockquote><p>IRM is short for Investor Relationship Management and can be seen as a CRM for listed companies. The new improved module offers new possibilites to search for counterparts and investors in a global database but also for planning and logging IR-activities in an efficient way.</p></div>\n" +
                        "<div class=\"mfn-footer mfn-contacts\"><p><strong>Contacts</strong></p>\n" +
                        "\n" +
                        "<hr/>\n" +
                        "\n" +
                        "<p><strong>Petter Hedborg</strong>         <br/>\n" +
                        "<em>CEO and Founder</em>         <br/>\n" +
                        "Phone: +46 709 – 42 41 13         <br/>\n" +
                        "Email: <a href=\"mailto:petter.hedborg@modularfinance.se\" target=\"_blank\" rel=\"nofollow noopener\">petter.hedborg@modularfinance.se</a></p>\n" +
                        "\n" +
                        "<p><strong>Måns Flodberg</strong>  <br/>\n" +
                        "<em>Deputy CEO and Founder</em>    <br/>\n" +
                        "Phone: +46 702 – 83 11 99     <br/>\n" +
                        "Mail: <a href=\"mailto:faw.azzat@modularfinance.se\" target=\"_blank\" rel=\"nofollow noopener\">mans.flodberg@modularfinance.se</a></p>\n" +
                        "</div>\n" +
                        "<div class=\"mfn-footer mfn-about\"><p><strong>About Modular Finance</strong></p>\n" +
                        "\n" +
                        "<hr/>\n" +
                        "\n" +
                        "<p>Modular Finance is a SaaS company focusing on the financial market in the Nordic region. Through our two business areas, Banking &amp; Finance and Listed companies, we offer a number of niche products with a focus on user friendliness.</p>\n" +
                        "</div>\n" +
                        "<div class=\"mfn-footer mfn-attachment mfn-attachment-pdf\"><p><strong class=\"mfn-heading-1\">Attachments</strong></p><hr/><p><a class=\"mfn-generated mfn-primary\" href=\"https://storage.mfn.se/22eaf750-a26f-40a0-843a-e3520d356c90/modular-finance-launches-a-new-irm-in-monitor.pdf\" target=\"_blank\" rel=\"nofollow noopener\">Modular Finance launches a new IRM in Monitor</a></p></div>",
                item.getContent().getHtml());
    }

}

