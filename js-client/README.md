# Client API
The api utilizes a builder pattern for ease of use and tries make what is expected to return.

# Example of use
```js 

const MFN = require('mfn-client.js');

// Add the base url and entity id of the feed
const client = MFN.create("https://mfn.se", "2c07a2db-2f22-4a67-ab46-ccb464296638");

let items = client.feed().fetch();
console.log(" 1: ", items[0]);

// Only items from 2019
items = client.feed().year(2019).fetch();
console.log(" 2: ", items[0]);

// Only items from 2019 that are Public relationship
items = client.feed()
        .year(2019)
        .type(client.TYPE_PR)
        .fetch();
console.log(" 3: ", items[0]);

// Only items from 2019 that are Investor relationship
items = client.feed()
        .year(2019)
        .type(client.TYPE_IR)
        .fetch();
console.log(" 4: ", items[0]);

// Only items from 2019 that are Investor relationship and has the tag sub:ca
items = client.feed()
        .year(2019)
        .type(client.TYPE_IR)
        .hasTag("sub:ca")
        .fetch();
console.log(" 5: ", items[0]);

// Only items from 2019 that are Investor relationship and has the tag sub:ca in English
items = client.feed()
        .year(2019)
        .type(client.TYPE_IR)
        .hasTag("sub:ca")
        .lang("en")
        .fetch();
console.log(" 6: ", items[0]);

// Only items from 2019 that are Investor relationship and has the tag sub:ca in English
// containg the text "financial technology"
items = client.feed()
        .year(2019)
        .type(client.TYPE_IR)
        .hasTag("sub:ca")
        .lang("en")
        .query("financial technology")
        .fetch();
console.log(" 7: ", items[0]);

// Get specific item by id
let item = client.itemById("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");
console.log(" 8: ", item);

// Get specific item by url slug
item = client.item("modular-finance-launches-a-new-irm-in-monitor");
console.log(" 9: ", item);

```