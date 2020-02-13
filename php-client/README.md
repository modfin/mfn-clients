# Client API
The api utilizes a builder pattern for ease of use and tries make what is expected to return.

# Test
```bash 
composer install
composer test
```

# Example of use
```php
include 'src/mfn-client->php';
use MFN\Client;

// Add the base url and entity id of the feed
$client = new Client("https://mfn.se", "2c07a2db-2f22-4a67-ab46-ccb464296638");

// The latest item
$items = $client->feed()->fetch();
print_r($items[0]);

// Only $items from 2019
$items = $client->feed()
        ->year(2019)
        ->fetch();
print_r($items[0]);

// Only $items from 2019 that are Public relationship
$items = $client->feed()
        ->year(2019)
        ->type(TYPE_PR)
        ->fetch();
print_r($items[0]);

// Only $items from 2019 that are Investor relationship
$items = $client->feed()
        ->year(2019)
        ->type(TYPE_IR)
        ->fetch();
print_r($items[0]);

// Only $items from 2019 that are Investor relationship and has the tag sub:ca
$items = $client->feed()
        ->year(2019)
        ->type(TYPE_IR)
        ->hasTag("sub:ca")
        ->fetch();
print_r($items[0]);

// Only $items from 2019 that are Investor relationship and has the tag sub:ca in English
$items = $client->feed()
        ->year(2019)
        ->type(TYPE_IR)
        ->hasTag("sub:ca")
        ->lang("en")
        ->fetch();
print_r($items[0]);

// Only $items from 2019 that are Investor relationship and has the tag sub:ca in English
// containg the text "financial technology"
$items = $client->feed()
        ->year(2019)
        ->type(TYPE_IR)
        ->hasTag("sub:ca")
        ->lang("en")
        ->query("financial technology")
        ->fetch();
print_r($items[0]);

// Get specific item by id
$item = $client->itemById("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");
print_r($item);

// Get specific item by url slug
$item = $client->item("modular-finance-launches-a-new-irm-in-monitor");
print_r($item);

```