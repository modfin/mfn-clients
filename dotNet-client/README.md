
# Client API
The api utilizes a builder pattern for ease of use and tries make what is expected to return.

# Example of use
```csharp
using System;
using System.Linq;
using MFNClient;
using MFNClient.models;

namespace MFNClientExample
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            // Add the base url and entity id of the feed
            var client = new Client("https://mfn.se", "2c07a2db-2f22-4a67-ab46-ccb464296638");
            
            // The latest item
            var items = client.Feed().Fetch();
            Console.WriteLine(" 1: " + items.First());
            
            // Only items from 2019
            items = client.Feed()
                .Year(2019)
                .Fetch();
            Console.WriteLine(" 2: " + items.First());
            
            // Only items from 2019 that are Public relationship
            items = client.Feed()
                .Year(2019)
                .Type(Enums.TypePr)
                .Fetch();
            Console.WriteLine(" 3: " + items.First());
            
            // Only items from 2019 that are Public relationship
            items = client.Feed()
                .Year(2019)
                .Type(Enums.TypeIr)
                .Fetch();
            Console.WriteLine(" 4: " + items.First());
            
            // Only items from 2019 that are Investor relationship and has the tag sub:ca
            items = client.Feed()
                .Year(2019)
                .Type(Enums.TypeIr)
                .HasTag("sub:ca")
                .Fetch();
            Console.WriteLine(" 5: " + items.First());
            
            // Only items from 2019 that are Investor relationship and has the tag sub:ca in English
            items = client.Feed()
                .Year(2019)
                .Type(Enums.TypeIr)
                .HasTag("sub:ca")
                .Lang("en")
                .Fetch();
            Console.WriteLine(" 6: " + items.First());
            
            // Only items from 2019 that are Investor relationship and has the tag sub:ca in English
            // containg the text "financial technology"
            items = client.Feed()
                .Year(2019)
                .Type(Enums.TypeIr)
                .HasTag("sub:ca")
                .Lang("en")
                .Query("financial technology")
                .Fetch();
            Console.WriteLine(" 7: " + items.First());

            // Get specific item by id
            var item = client.NewsItemById("a9e4b2ac-fb06-47a9-b3c6-6c9a632efde3");
            Console.WriteLine(" 8: " + item);
            
            // Get specific item by slug
            item = client.NewsItem("modular-finance-lanserar-nytt-irm-i-monitor");
            Console.WriteLine(" 9: " + item);

        }
    }
}
```