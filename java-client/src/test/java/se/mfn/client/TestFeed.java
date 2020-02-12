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
        return new Client("https://mfn.se", "2c07a2db-2f22-4a67-ab46-ccb464296638");
    }

    @Test
    public void testFeedItems() throws IOException {
        Client c = createClient();
        List<NewsItem> items = c.feed().get();
        assertTrue(items.size() >= 10);
    }

    @Test
    public void testSingleItem() throws IOException {

        Client c = createClient();
        List<NewsItem> items = c.feed().get();
        assertTrue(items.size() > 0);

        NewsItem item0 = items.get(0);
        NewsItem item1 = c.newsItem(item0);
        NewsItem item2 = c.newsItem(item0.getNewsId());

        assertTrue(item0.equals(item1) && item1.equals(item2));
    }

    @Test
    public void testFilterEmpty() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed().get();
        assertTrue(0 < items0.size());
    }

    @Test
    public void testFilterLimit() throws IOException {
        Client c = createClient();
        List<NewsItem> items = c.feed()
                .limit(5)
                .get();
        assertEquals(5, items.size());
    }

    @Test
    public void testFilterOffset() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .limit(5)
                .offset(0)
                .get();
        assertEquals(5, items0.size());

        List<NewsItem> items1 = c.feed()
                .limit(5)
                .offset(4)
                .get();
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
                .get();
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
                .get();
        assertTrue(items0.size() > 0);

        NewsItem item0 = items0.get(0);

        NewsItem item1 = c.newsItem(item0);

        assertEquals(item0, item1);
    }

    @Test
    public void testFilterYear() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .type(Type.PR)
                .year(2019)
                .get();
        assertEquals(10, items0.size());

        NewsItem item0 = items0.get(0);
        NewsItem item1 = c.newsItem(item0);

        assertEquals(item0, item1);
    }

    @Test
    public void testFilterYear2() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .year(2018)
                .get();

        assertEquals(0, items0.size());
    }

    @Test
    public void testFilterLang() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .year(2019)
                .type(Type.IR)
                .lang("sv")
                .get();

        assertEquals(5, items0.size());
        items0 = c.feed()
                .year(2019)
                .lang("en")
                .get();
        assertEquals(10, items0.size());
    }

    @Test
    public void testFilterTags() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .year(2019)
                .hasTag("sub:ci")
                .get();

        assertEquals(7, items0.size());
        for (NewsItem item : items0) {
            assertTrue(item.getProperties().getTags().contains("sub:ci"));
        }

        items0 = c.feed()
                .year(2019)
                .hasTag("sub:ca")
                .hasTag(":correction:7e5ece8b-9e1a-4db4-8775-0d97cf2d5b8e")
                .get();

        assertEquals(1, items0.size());
        for (NewsItem item : items0) {
            assertTrue(item.getProperties().getTags().contains("sub:ca"));
            assertTrue(item.getProperties().getTags().contains(":correction:7e5ece8b-9e1a-4db4-8775-0d97cf2d5b8e"));
        }

    }

    @Test
    public void testFilterQuery() throws IOException {
        Client c = createClient();
        List<NewsItem> items0 = c.feed()
                .year(2019)
                .query("correction Lorem ipsum")
                .get();

        assertEquals(1, items0.size());
        assertEquals("Correction: Test release (2) from MFN", items0.get(0).getContent().getTitle());
    }


    @Test
    public void testConent() throws IOException, ParseException {
        Client c = createClient();
        NewsItem item = c.newsItem("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");

        assertEquals("b660f6cc-5d7e-4cab-8862-3271b649a636", item.getGroupId());
        assertEquals("https://mfn.se/a/modfin/modular-finance-launches-a-new-irm-in-monitor", item.getUrl());
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

        assertEquals("After a considerable time of product development, in close co-operation with\n" +
                        "our customers, we now launch a new IRM in Monitor. Investor Relationship\n" +
                        "Management assists listed companies in making their IR-work more effective and\n" +
                        "easier to follow up.\n" +
                        "\n" +
                        "Monitor has since the start provided unique data and functionailty in regards\n" +
                        "to ownership information. The platform assists listed companies in the Nordics\n" +
                        "to dynamicailly and on an ongoing basis identify and track shareholders that\n" +
                        "otherwise are hard to identify. Since the launch in 2016 the platform has been\n" +
                        "broadened and also includes complete functionality for Investor Targeting,\n" +
                        "data for reports, a mobile app with notifications and now, a complete and\n" +
                        "state of the art IRM.\n" +
                        "| I am very proud of the team and its efforts in this development. In my view\n" +
                        "| Monitor is now by far the most complete and potent IR-product in the\n" +
                        "| Nordics, says Petter Hedborg, CEO of Modular Finance.\n" +
                        "\n" +
                        "IRM is short for Investor Relationship Management and can be seen as a CRM for\n" +
                        "listed companies. The new improved module offers new possibilites to search\n" +
                        "for counterparts and investors in a global database but also for planning and\n" +
                        "logging IR-activities in an efficient way.\n" +
                        "\n" +
                        "Contacts\n" +
                        "------------------------------------------------------------------------------\n" +
                        "Petter Hedborg\n" +
                        "CEO and Founder\n" +
                        "Phone: +46 709 - 42 41 13\n" +
                        "Email: petter.hedborg@modularfinance.se\n" +
                        "\n" +
                        "Måns Flodberg\n" +
                        "Deputy CEO and Founder\n" +
                        "Phone: +46 702 - 83 11 99\n" +
                        "Mail: mans.flodberg@modularfinance.se (faw.azzat@modularfinance.se)\n" +
                        "\n" +
                        "About Modular Finance\n" +
                        "------------------------------------------------------------------------------\n" +
                        "Modular Finance is a SaaS company focusing on the financial market in the\n" +
                        "Nordic region. Through our two business areas, Banking & Finance and Listed\n" +
                        "companies, we offer a number of niche products with a focus on user\n" +
                        "friendliness.\n" +
                        "\n" +
                        "== Attachments ==\n" +
                        "------------------------------------------------------------------------------\n" +
                        "Modular Finance launches a new IRM in Monitor\n" +
                        "(https://storage.mfn.se/22eaf750-a26f-40a0-843a-e3520d356c90/modular-finance--\n" +
                        "launches-a-new-irm-in-monitor.pdf)",
                item.getContent().getText());
    }

}

