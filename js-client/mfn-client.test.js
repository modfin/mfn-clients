'use strict';
const Suite = require('node-test');
const MFN = require('./mfn-client.js');

const newClient = () => MFN.create("https://mfn.se", "2c07a2db-2f22-4a67-ab46-ccb464296638");

const suite = new Suite('MFN Client');
suite.test('testFeedItems', async t => {

    const c = newClient();
    const p = await c.feed().fetch();

    let ir = false;
    let pr = false;

    p.forEach((i) => {
        ir = ir || i.properties.type === c.TYPE_IR;
        pr = pr || i.properties.type === c.TYPE_PR;
    });

    t.true(ir);
    t.true(pr);
    t.true(p.length > 2);

    return false
});
suite.test('testSingleItemIR', async t => {

    const c = newClient();
    const p = await c.feed().type(c.TYPE_IR).fetch();
    t.true(p.length > 0);

    p.forEach(i => t.true(i.properties.type === c.TYPE_IR));

    const i0 = await c.itemById(p[0].news_id);
    const i1 = await c.item(p[0].content.slug);

    t.deepEqual(p[0], i0);
    t.deepEqual(p[0], i1);

    return true
});

suite.test('testSingleItemPR', async t => {

    const c = newClient();
    const p = await c.feed().type(c.TYPE_PR).fetch();
    t.true(p.length > 0);

    p.forEach(i => t.true(i.properties.type === c.TYPE_PR));

    const i0 = await c.itemById(p[0].news_id);
    const i1 = await c.item(p[0].content.slug);

    t.deepEqual(p[0], i0);
    t.deepEqual(p[0], i1);

    return true
});

suite.test('testLimit', async t => {

    const c = newClient();
    const p = await c.feed().type(c.TYPE_PR).limit(5).fetch();
    t.true(p.length === 5);
    p.forEach(i => t.true(i.properties.type === c.TYPE_PR));

    return true
});

suite.test('testOffset', async t => {

    const c = newClient();
    const p0 = await c.feed().type(c.TYPE_PR).limit(5).offset(0).fetch();
    const p1 = await c.feed().type(c.TYPE_PR).limit(5).offset(4).fetch();
    t.true(p0.length === 5);
    t.true(p1.length === 5);
    p0.forEach(i => t.true(i.properties.type === c.TYPE_PR));
    p1.forEach(i => t.true(i.properties.type === c.TYPE_PR));

    t.deepEqual(p0[4], p1[0]);

    return true
});

suite.test('testOffset', async t => {

    const c = newClient();
    const p0 = await c.feed().type(c.TYPE_PR).limit(5).offset(0).fetch();
    const p1 = await c.feed().type(c.TYPE_PR).limit(5).offset(4).fetch();
    t.true(p0.length === 5);
    t.true(p1.length === 5);
    p0.forEach(i => t.true(i.properties.type === c.TYPE_PR));
    p1.forEach(i => t.true(i.properties.type === c.TYPE_PR));

    t.deepEqual(p0[4], p1[0]);

    return true
});

suite.test('testYear', async t => {

    const c = newClient();
    const p0 = await c.feed().type(c.TYPE_PR).year(2019).fetch();
    t.true(p0.length === 10);
    p0.forEach(i => t.true(i.properties.type === c.TYPE_PR));

    return true
});
suite.test('testYear2', async t => {

    const c = newClient();
    const p0 = await c.feed().type(c.TYPE_PR).year(2018).fetch();
    t.true(p0.length === 0);

    return true
});

suite.test('testLang', async t => {

    const c = newClient();
    let p0 = await c.feed().type(c.TYPE_IR).year(2019).lang('sv').fetch();
    t.true(p0.length === 5);
    p0.forEach(i => t.equal(i.properties.lang, 'sv'));

    p0 = await c.feed().year(2019).lang('en').fetch();
    t.true(p0.length === 10);
    p0.forEach(i => t.equal(i.properties.lang, 'en'));
    return true
});

suite.test('testTags', async t => {

    const c = newClient();
    let p0 = await c.feed().year(2019).hasTag("sub:ci").fetch();
    t.true(p0.length === 7);
    p0.forEach(i => t.true(i.properties.tags.includes("sub:ci")));

    p0 = await c.feed().year(2019).hasTag("sub:ca").hasTag(":correction:7e5ece8b-9e1a-4db4-8775-0d97cf2d5b8e").fetch();
    t.true(p0.length === 1);
    p0.forEach(i => t.true(i.properties.tags.includes("sub:ca") && i.properties.tags.includes(":correction:7e5ece8b-9e1a-4db4-8775-0d97cf2d5b8e") ));
    return true
});

suite.test('testQuery', async t => {

    const c = newClient();
    let p0 = await c.feed().year(2019).query("correction Lorem ipsum").fetch();
    t.equals(p0.length, 1);
    t.equals(p0[0].content.title, "Correction: Test release (2) from MFN");

    return true
});


suite.test('testContent', async t => {

    const c = newClient();
    let item = await c.itemById("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");
    t.equals("b660f6cc-5d7e-4cab-8862-3271b649a636", item.group_id);
    t.equals("https://mfn.se/a/modfin/modular-finance-launches-a-new-irm-in-monitor", item.url);
    t.equals("2c07a2db-2f22-4a67-ab46-ccb464296638", item.author.entity_id);
    t.equals(1, item.subjects.length);
    t.equals("2c07a2db-2f22-4a67-ab46-ccb464296638", item.subjects[0].entity_id);
    t.equals("Modular Finance launches a new IRM in Monitor", item.content.title);
    t.equals("modular-finance-launches-a-new-irm-in-monitor", item.content.slug);
    t.equals("en", item.properties.lang);
    t.equals(c.TYPE_PR, item.properties.type);
    t.deepEqual(["cus:monitor", "cus:modular finance"], item.properties.tags);
    t.equals("2019-11-23T14:30:00Z", item.content.publish_date);
    t.equals("<div class=\"mfn-preamble\"><p><strong>After a considerable time of product development, in close co-operation with our customers, we now launch a new IRM in Monitor. Investor Relationship Management assists listed companies in making their IR-work more effective and easier to follow up.</strong></p></div>\n" +
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

    t.equals("After a considerable time of product development, in close co-operation with\n" +
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
        item.content.text);


    return true
});