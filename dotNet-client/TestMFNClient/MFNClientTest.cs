using System;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using MFNClient;
using MFNClient.models;
using NUnit.Framework;

namespace TestMFNClient
{
    public class Tests
    {
        [SetUp]
        public void Setup()
        {
        }

        public static Client NewClient()
        {
            return new Client("https://feed.mfn.se/v1", "2c07a2db-2f22-4a67-ab46-ccb464296638");
        }
        
        [Test]
        public void TestFeed()
        {
            Client c = NewClient();
            NewsItem[] n = c.Feed().Fetch();
            Assert.True(n.Length >= 20);
        }
        
        [Test]
        public void TestFeedAsync()
        {
            Client c = NewClient();
            NewsItem[] n = c.Feed().FetchAsync().Result;
            Assert.True(n.Length >= 20);
        }
        
        [Test]
        public void TestNewsItemIR()
        {
            Client c = NewClient();
            NewsItem[] n = c.Feed().Type(Enums.TypeIr).Fetch();
            Assert.True(n.Length > 0);

            var item0 = n.First();
            Assert.AreEqual(item0.Properties.Type, Enums.TypeIr);

            var item1 = c.ItemById(item0.NewsId);
            var item2 = c.ItemById(item0.NewsId.ToString());
            var item3 = c.Item(item0);
            var item4 = c.Item(item0.Content.Slug);
            
            Assert.AreEqual(item0, item1);
            Assert.AreEqual(item0, item2);
            Assert.AreEqual(item0, item3);
            Assert.AreEqual(item0, item4);
            
        }
        [Test]
        public void TestNewsItemPR()
        {
            Client c = NewClient();
            NewsItem[] n = c.Feed().Type(Enums.TypePr).Fetch();
            Assert.True(n.Length > 0);

            var item0 = n.First();
            Assert.AreEqual(item0.Properties.Type, Enums.TypePr);
            var item1 = c.ItemById(item0.NewsId);
            var item2 = c.ItemById(item0.NewsId.ToString());
            var item3 = c.Item(item0);
            var item4 = c.Item(item0.Content.Slug);
            
            Assert.AreEqual(item0, item1);
            Assert.AreEqual(item0, item2);
            Assert.AreEqual(item0, item3);
            Assert.AreEqual(item0, item4);
            
        }
        
        [Test]
        public void TestFeedLimit()
        {
            Client c = NewClient();
            NewsItem[] n = c.Feed().Limit(5).Fetch();
            Assert.True(n.Length == 5);
        }
        
        [Test]
        public void TestFeedOffset()
        {
            Client c = NewClient();
            NewsItem[] n0 = c.Feed().Limit(5).Fetch();
            Assert.True(n0.Length == 5);
            NewsItem[] n1 = c.Feed().Limit(5).Offset(4).Fetch();
            Assert.True(n0.Length == 5);
            Assert.AreEqual(n0.Last(), n1.First());
        }
        
        [Test]
        public void TestFeedPr()
        {
            Client c = NewClient();
            NewsItem[] items = c.Feed().Type(Enums.TypePr).Fetch();
            Assert.True(items.Length > 0);

            foreach (var item in items)
            {
                Assert.AreEqual(item.Properties.Type, Enums.TypePr);
            }

            var n0 = items.First();
            var n1 = c.ItemById(n0.NewsId.ToString());
            Assert.AreEqual(n0, n1);

        }
        
        [Test]
        public void TestFeedYear()
        {
            Client c = NewClient();
            NewsItem[] items = c.Feed().Type(Enums.TypePr).Year(2019).Fetch();
            foreach (var item in items)
            {
                Assert.AreEqual(item.Content.PublishDate.Year, 2019);
            }
            
            
            var n0 = items.First();
            var n1 = c.Item(n0);
            Assert.AreEqual(n0, n1);

        }
        
        [Test]
        public void TestFeedYear2()
        {
            Client c = NewClient();
            var items = c.Feed().Year(2018).Fetch();
            Assert.True(items.Length == 0);
        }
        
        [Test]
        public void TestFeedLang()
        {
            Client c = NewClient();
            var items = c.Feed().Year(2019).Type(Enums.TypeIr).Lang("sv").Fetch();
            foreach (var item in items)
            {
                Assert.True(item.Properties.Lang == "sv");
            }
            
            items = c.Feed().Year(2019).Lang("en").Fetch();
            foreach (var item in items)
            {
                Assert.True(item.Properties.Lang == "en");
            }
        }
        
        [Test]
        public void TestFeedTags()
        {
            Client c = NewClient();
            var items0 = c.Feed().Year(2019).HasTag("sub:ci").Fetch();
            foreach (var item in items0)
            {
                Assert.True(item.Properties.Tags.Contains("sub:ci"));
            }
        }
        
        [Test]
        public void TestFeedQuery()
        {
            Client c = NewClient();
            var items0 = c.Feed().Year(2019).Query("correction Lorem ipsum").Fetch();
            Assert.AreEqual(0, items0.Length);
        }
        
        [Test]
        public void TestFeedContent()
        {
            Client c = NewClient();
            var item = c.ItemById("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");
            Assert.AreEqual("b660f6cc-5d7e-4cab-8862-3271b649a636", item.GroupId.ToString());
            Assert.AreEqual("https://feed.mfn.se/v1/item/a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3.html", item.Url.ToString());
            Assert.AreEqual("2c07a2db-2f22-4a67-ab46-ccb464296638", item.Author.EntityId.ToString());
            Assert.AreEqual(1, item.Subjects.Length);
            Assert.AreEqual("2c07a2db-2f22-4a67-ab46-ccb464296638", item.Subjects.First().EntityId.ToString());
            Assert.AreEqual("Modular Finance launches a new IRM in Monitor", item.Content.Title);
            Assert.AreEqual("modular-finance-launches-a-new-irm-in-monitor", item.Content.Slug);
            Assert.AreEqual("en", item.Properties.Lang);
            Assert.AreEqual(Enums.TypePr, item.Properties.Type);

            var dateTime = DateTimeOffset.ParseExact("2019-11-23T14:30:00Z", "yyyy-MM-dd'T'HH:mm:ssK", CultureInfo.InvariantCulture);
            Assert.AreEqual(dateTime, item.Content.PublishDate);
            
            Assert.AreEqual("<div class=\"mfn-preamble\"><p><strong>After a considerable time of product development, in close co-operation with our customers, we now launch a new IRM in Monitor. Investor Relationship Management assists listed companies in making their IR-work more effective and easier to follow up.</strong></p></div>\n" +
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
                item.Content.Html);


        }
        
    }
}