<?php

include 'src/mfn-client.php';
use MFN\Client;
use PHPUnit\Framework\TestCase;

class MFNClientTest extends TestCase
{

    public function createClient(){
        return new Client("https://feed.mfn.se/v1", "2c07a2db-2f22-4a67-ab46-ccb464296638");
    }
    public function createClientIntrum(){
        return new Client("https://feed.mfn.se/v1", "d7d583cc-c99d-4152-baa9-bbd89061d9e6");
    }
    public function createClientThunderfulGroup(){
        return new Client("https://feed.mfn.se/v1", "94334e99-00f2-4c98-b5a5-314a8c73fbfe");
    }

    public function testFeedItems()
    {
        $c = $this->createClient();
        $items = $c->feed()->fetch();
        $this->assertTrue(sizeof($items) == 25);
    }


    public function testSingleItemIR(){

        $c = $this->createClient();
        $items = $c->feed()->type(TYPE_IR)->fetch();
        $this->assertTrue(sizeof($items) == 25);

        foreach ($items as $item){
            $this->assertEquals($item->properties->type, TYPE_IR);
        }

        $item0 = $items[0];

        $item1 = $c->itemById($item0->news_id);
        $this->assertEquals($item0, $item1);

        $item2 = $c->item($item0->content->slug);
        $this->assertEquals($item0, $item2);
    }

    public function testSingleItemPR(){

        $c = $this->createClient();
        $items = $c->feed()->type(TYPE_PR)->fetch();
        $this->assertTrue(sizeof($items) > 0);

        foreach ($items as $item){
            $this->assertEquals($item->properties->type, TYPE_PR);
        }

        $item0 = $items[0];

        $item1 = $c->itemById($item0->news_id);
        $this->assertEquals($item0, $item1);

        $item2 = $c->item($item0->content->slug);
        $this->assertEquals($item0, $item2);
    }


    public function testLimit(){

        $c = $this->createClient();
        $items = $c->feed()->type(TYPE_PR)->limit(5)->fetch();
        $this->assertEquals(sizeof($items) , 5);

        foreach ($items as $item){
            $this->assertEquals($item->properties->type, TYPE_PR);
        }
    }

    public function testOffset(){

        $c = $this->createClient();
        $items0 = $c->feed()->type(TYPE_PR)->limit(5)->fetch();
        $this->assertEquals(sizeof($items0) , 5);
        foreach ($items0 as $item){
            $this->assertEquals($item->properties->type, TYPE_PR);
        }

        $items1 = $c->feed()->type(TYPE_PR)->limit(5)->offset(4)->fetch();
        $this->assertEquals(sizeof($items0) , 5);
        foreach ($items0 as $item){
            $this->assertEquals($item->properties->type, TYPE_PR);
        }

        $this->assertEquals($items0[4], $items1[0]);
    }

    public function testYear(){

        $c = $this->createClient();
        $items0 = $c->feed()->type(TYPE_PR)->year(2019)->fetch();
        $this->assertEquals(sizeof($items0) , 8);
        foreach ($items0 as $item){
            $this->assertEquals($item->properties->type, TYPE_PR);
        }

        $c = $this->createClient();
        $items0 = $c->feed()->type(TYPE_PR)->year(2020)->fetch();
        $this->assertEquals(sizeof($items0) , 2);
    }

    public function testLang(){

        $c = $this->createClient();
        $items0 = $c->feed()->type(TYPE_IR)->year(2020)->lang("sv")->fetch();
        $this->assertEquals(sizeof($items0) , 1);
        foreach ($items0 as $item){
            $this->assertEquals($item->properties->lang, "sv");
        }

        $items0 = $c->feed()->year(2019)->lang("en")->fetch();
        $this->assertEquals(sizeof($items0) , 4);
        foreach ($items0 as $item){
            $this->assertEquals($item->properties->lang, "en");
        }
    }

    public function testTag(){

        $c = $this->createClientIntrum();
        $items0 = $c->feed()->year(2023)->hasTag("sub:report")->fetch();
        $this->assertEquals(sizeof($items0) , 10);
        foreach ($items0 as $item){
            $this->assertTrue(in_array(":regulatory", $item->properties->tags));
        }
    }

    public function testCorrection() {
        $c = $this->createClientThunderfulGroup();
        $item = $c->itemById("c50e4c61-c49f-440b-815b-daaca5336822");
        $this->assertTrue(in_array(":correction", $item->properties->tags));
        $this->assertTrue(in_array(":correction:9ee6a296-0db6-4137-a7c7-14c9a5579953", $item->properties->tags));
    }

    public function testQuery(){
        $c = $this->createClientThunderfulGroup();
        $items0 = $c->feed()->year(2023)->query("genomför")->fetch();
        $this->assertEquals(sizeof($items0) , 1);
        $this->assertEquals($items0[0]->content->title , "Thunderful Group har genomfört två mindre tilläggsförvärv");
    }


    public function testContent(){

        $c = $this->createClient();
        $item = $c->itemById("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");
        $this->assertEquals("b660f6cc-5d7e-4cab-8862-3271b649a636", $item->group_id);
        $this->assertEquals("https://feed.mfn.se/v1/feed/2c07a2db-2f22-4a67-ab46-ccb464296638/item/a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3.html", $item->url);
        $this->assertEquals("2c07a2db-2f22-4a67-ab46-ccb464296638", $item->author->entity_id);
        $this->assertEquals(1, sizeof($item->subjects));
        $this->assertEquals("2c07a2db-2f22-4a67-ab46-ccb464296638", $item->subjects[0]->entity_id);
        $this->assertEquals("Modular Finance launches a new IRM in Monitor", $item->content->title);
        $this->assertEquals("modular-finance-launches-a-new-irm-in-monitor", $item->content->slug);
        $this->assertEquals("en", $item->properties->lang);
        $this->assertEquals(TYPE_PR, $item->properties->type);
        $this->assertEquals(["cus:monitor", "cus:modular finance"], $item->properties->tags);
        $this->assertEquals("2019-11-23T14:30:00Z", $item->content->publish_date);
        $this->assertEquals("<div class=\"mfn-preamble\"><p><strong>After a considerable time of product development, in close co-operation with our customers, we now launch a new IRM in Monitor. Investor Relationship Management assists listed companies in making their IR-work more effective and easier to follow up.</strong></p></div>\n" .
            "<div class=\"mfn-body\"><p>Monitor has since the start provided unique data and functionailty in regards to ownership information. The platform assists listed companies in the Nordics to dynamicailly and on an ongoing basis identify and track shareholders that otherwise are hard to identify. Since the launch in 2016 the platform has been broadened and also includes complete functionality for Investor Targeting, data for reports, a mobile app with notifications and now, a complete and state of the art IRM.</p><blockquote><em>I am very proud of the team and its efforts in this development. In my view Monitor is now by far the most complete and potent IR-product in the Nordics</em>, says Petter Hedborg, CEO of Modular Finance.</blockquote><p>IRM is short for Investor Relationship Management and can be seen as a CRM for listed companies. The new improved module offers new possibilites to search for counterparts and investors in a global database but also for planning and logging IR-activities in an efficient way.</p></div>\n" .
            "<div class=\"mfn-footer mfn-contacts\"><p><strong>Contacts</strong></p>\n" .
            "\n" .
            "<hr/>\n" .
            "\n" .
            "<p><strong>Petter Hedborg</strong>         <br/>\n" .
            "<em>CEO and Founder</em>         <br/>\n" .
            "Phone: +46 709 – 42 41 13         <br/>\n" .
            "Email: <a href=\"mailto:petter.hedborg@modularfinance.se\" target=\"_blank\" rel=\"nofollow noopener\">petter.hedborg@modularfinance.se</a></p>\n" .
            "\n" .
            "<p><strong>Måns Flodberg</strong>  <br/>\n" .
            "<em>Deputy CEO and Founder</em>    <br/>\n" .
            "Phone: +46 702 – 83 11 99     <br/>\n" .
            "Mail: <a href=\"mailto:faw.azzat@modularfinance.se\" target=\"_blank\" rel=\"nofollow noopener\">mans.flodberg@modularfinance.se</a></p>\n" .
            "</div>\n" .
            "<div class=\"mfn-footer mfn-about\"><p><strong>About Modular Finance</strong></p>\n" .
            "\n" .
            "<hr/>\n" .
            "\n" .
            "<p>Modular Finance is a SaaS company focusing on the financial market in the Nordic region. Through our two business areas, Banking &amp; Finance and Listed companies, we offer a number of niche products with a focus on user friendliness.</p>\n" .
            "</div>\n" .
            "<div class=\"mfn-footer mfn-attachment mfn-attachment-pdf\"><p><strong class=\"mfn-heading-1\">Attachments</strong></p><hr/><p><a class=\"mfn-generated mfn-primary\" href=\"https://storage.mfn.se/22eaf750-a26f-40a0-843a-e3520d356c90/modular-finance-launches-a-new-irm-in-monitor.pdf\" target=\"_blank\" rel=\"nofollow noopener\">Modular Finance launches a new IRM in Monitor</a></p></div>",
            $item->content->html);

    }

    public function testStaging() {
        $c = new Client("https://feed.mfn.modfin.se/v1", "8f950a38-5364-4628-8c4b-1aa3003626f5");
        $item = $c->itemById("389fb781-8e4e-5398-81b3-c89ad9815959");
        $this->assertEquals("93313986-7eed-4c6d-bcab-81cec65b90d3", $item->group_id);
    }
}
