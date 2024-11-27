'use strict';
const assert = require('assert'); // Node.js assertion module
const MFN = require('./mfn-client.js');

const test = require('node:test');

const newClient = () => MFN.create("https://feed.mfn.se/v1", "2c07a2db-2f22-4a67-ab46-ccb464296638");
const newClientIntrum = () => MFN.create("https://feed.mfn.se/v1", "d7d583cc-c99d-4152-baa9-bbd89061d9e6");
const newClientThunderfulGroup = () => MFN.create("https://feed.mfn.se/v1", "94334e99-00f2-4c98-b5a5-314a8c73fbfe");

test('test fetch with standard limit', async (t) => {
    const c = newClient();
    const p = await c.feed().fetch();
    assert.strictEqual(p.length, 25, 'length not 25');
});

test('test IR feed', async (t) => {
    const c = newClient();
    const p = await c.feed().type(c.TYPE_IR).fetch();
    assert.strictEqual(true, (p.length > 0));

    p.forEach(i => assert.strictEqual(true, i.properties.type === c.TYPE_IR));

    const i0 = await c.itemById(p[0].news_id);
    const i1 = await c.item(p[0].content.slug);

    assert.deepStrictEqual(p[0], i0);
    assert.deepStrictEqual(p[0], i1);
});

test('test limit', async (t) => {
    const c = newClient();
    const p = await c.feed().type(c.TYPE_PR).limit(5).fetch();
    assert.strictEqual(true, p.length === 5);
    p.forEach(i => assert.strictEqual(true, i.properties.type === c.TYPE_PR));
});

test('test offset', async (t) => {
    const c = newClient();
    const p0 = await c.feed().type(c.TYPE_PR).limit(5).offset(0).fetch();
    const p1 = await c.feed().type(c.TYPE_PR).limit(5).offset(4).fetch();
    assert.strictEqual(true, p0.length === 5);
    assert.strictEqual(true, p1.length === 5);
    p0.forEach(i => assert.strictEqual(true, i.properties.type === c.TYPE_PR));
    p1.forEach(i => assert.strictEqual(true, i.properties.type === c.TYPE_PR));

    assert.deepStrictEqual(p0[4], p1[0]);
});


test('test year', async (t) => {
    const c = newClient();
    const p0 = await c.feed().type(c.TYPE_PR).year(2019).fetch();
    assert.strictEqual(true, p0.length === 8);
    p0.forEach(i => assert.strictEqual(true, i.properties.type === c.TYPE_PR));
});

test('test year 2', async (t) => {
    const c = newClient();
    const p0 = await c.feed().type(c.TYPE_PR).year(2018).fetch();
    assert.strictEqual(true, p0.length === 0);
});

test('test lang', async (t) => {
    const c = newClient();
    let p0 = await c.feed().type(c.TYPE_IR).year(2020).lang('sv').fetch();
    assert.strictEqual(true, p0.length === 1);
    p0.forEach(i => assert.strictEqual(true, i.properties.lang === 'sv'));

    p0 = await c.feed().year(2019).lang('en').fetch();
    assert.strictEqual(true, p0.length === 4);
    p0.forEach(i => assert.strictEqual(true, i.properties.lang === 'en'));
});

test('test tags', async (t) => {

    const c = newClientIntrum();
    let p0 = await c.feed().year(2023).hasTag("sub:report").fetch();
    assert.strictEqual(true, p0.length === 10);
    p0.forEach(i => assert.strictEqual(true, i.properties.tags.includes(":regulatory")));
});

test('test correction', async (t) => {
    const c = newClientThunderfulGroup();
    const p0 = await c.itemById("c50e4c61-c49f-440b-815b-daaca5336822");
    assert.strictEqual(true, (p0.properties.tags.includes(":correction") && p0.properties.tags.includes(":correction:9ee6a296-0db6-4137-a7c7-14c9a5579953") ));
})


test('test query', async (t) => {
    const c = newClientThunderfulGroup();
    let p0 = await c.feed().year(2023).query("genomför").fetch();
    assert.strictEqual(1, p0.length);
    assert.strictEqual(p0[0].content.title, "Thunderful Group har genomfört två mindre tilläggsförvärv");
});


test('test query', async (t) => {
    const c = newClient();
    let item = await c.itemById("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");
    assert.strictEqual("b660f6cc-5d7e-4cab-8862-3271b649a636", item.group_id);
    assert.strictEqual("https://feed.mfn.se/v1/feed/2c07a2db-2f22-4a67-ab46-ccb464296638/item/a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3.html", item.url);
    assert.strictEqual("2c07a2db-2f22-4a67-ab46-ccb464296638", item.author.entity_id);
    assert.strictEqual(1, item.subjects.length);
    assert.strictEqual("2c07a2db-2f22-4a67-ab46-ccb464296638", item.subjects[0].entity_id);
    assert.strictEqual("Modular Finance launches a new IRM in Monitor", item.content.title);
    assert.strictEqual("modular-finance-launches-a-new-irm-in-monitor", item.content.slug);
    assert.strictEqual("en", item.properties.lang);
    assert.strictEqual(c.TYPE_PR, item.properties.type);
    assert.deepStrictEqual(["cus:monitor", "cus:modular finance"], item.properties.tags);
    assert.strictEqual("2019-11-23T14:30:00Z", item.content.publish_date);
    assert.strictEqual("<div class=\"mfn-preamble\"><p><strong>After a considerable time of product development, in close co-operation with our customers, we now launch a new IRM in Monitor. Investor Relationship Management assists listed companies in making their IR-work more effective and easier to follow up.</strong></p></div>\n" +
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
        item.content.html);

});
