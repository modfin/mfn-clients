using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Text.Encodings.Web;
using System.Threading.Tasks;
using MFNClient.models;


namespace MFNClient
{
    public class Client
    {
        private readonly string _baseUrl;
        private readonly string _entityId;

        public Client(String baseUrl, String entityId)
        {
            this._baseUrl = baseUrl;
            this._entityId = entityId;
        }

        public Filter Feed()
        {
            var url = new StringBuilder();
            url.Append(this._baseUrl);
            url.Append("/feed/").Append(_entityId);

            return new Filter(url);
        }


        public NewsItem Item(NewsItem item)
        {
            return ItemById(item.NewsId);
        }
        
        public NewsItem Item(string newsSlug)
        {
            return ItemAsync(newsSlug).Result;
        }
        public async Task<NewsItem> ItemAsync(string newsSlug)
        {
            StringBuilder url = new StringBuilder();
            url.Append(this._baseUrl);
            url.Append("/feed/").Append(_entityId);
            url.Append("?news-slug=").Append(newsSlug);
            
            var json = await Http.Async(url.ToString());
            var items = models.Feed.FromJson(json).Items;
            return items?[0];
        }

        public NewsItem ItemById(Guid newsId)
        {
            return ItemById(newsId.ToString());
        }

        public NewsItem ItemById(string newsId)
        {
            return ItemByIdAsync(newsId).Result;
        }

        public async Task<NewsItem> ItemAsync(NewsItem item)
        {
            return await ItemByIdAsync(item.NewsId);
        }

        public async Task<NewsItem> ItemByIdAsync(Guid newsId)
        {
            return await ItemByIdAsync(newsId.ToString());
        }

        public async Task<NewsItem> ItemByIdAsync(string newsId)
        {
            StringBuilder url = new StringBuilder();
            url.Append(this._baseUrl);
            url.Append("/feed/").Append(_entityId);
            url.Append("?news-id=").Append(newsId);
            
            var json = await Http.Async(url.ToString());
            var items = models.Feed.FromJson(json).Items;
            return items?[0];
        }
    }

    internal static class Http
    {
        internal static string Sync(string url)
        {
            var client = new HttpClient();
            var response = client.GetAsync(url).Result;
            response.EnsureSuccessStatusCode();
            return response.Content.ReadAsStringAsync().Result;
        }

        internal static async Task<string> Async(string url)
        {
            var client = new HttpClient();
            var response = await client.GetAsync(url);
            response.EnsureSuccessStatusCode();
            return await response.Content.ReadAsStringAsync();
        }
    }


    public class Filter
    {
        private readonly StringBuilder _url;

        private int _limit;
        private int _offset;
        private string _type;
        private string _lang;

        private int _year;
        private readonly List<string> _tags;
        private string _query;


        internal Filter(StringBuilder url)
        {
            _url = url;
            _limit = 25;
            _offset = 0;
            _tags = new List<string>();
            _type = Enums.TypeAll;
        }

        public Filter Limit(int limit)
        {
            _limit = limit;
            return this;
        }

        public Filter Offset(int offset)
        {
            _offset = offset;
            return this;
        }

        public Filter Lang(String lang)
        {
            _lang = lang;
            return this;
        }

        public Filter Year(int year)
        {
            _year = year;
            return this;
        }

        public Filter Query(String query)
        {
            _query = query;
            return this;
        }

        public Filter Type(string type)
        {
            _type = type;
            return this;
        }

        public Filter HasTag(String tag)
        {
            _tags.Add(tag);
            return this;
        }


        private string QueryParams()
        {
            var b = new StringBuilder();

            b.Append("?limit=").Append(this._limit);
            b.Append("&offset=").Append(this._offset);
            b.Append("&type=").Append(this._type);

            if (this._lang != null && this._lang.Length == 2)
            {
                b.Append("&lang=").Append(this._lang);
            }

            if (1900 < this._year && this._year < 2100)
            {
                b.Append("&from=").Append(_year).Append("-01-01T00%3A00%3A00Z");
                b.Append("&to=").Append(_year).Append("-12-31T23%3A59%3A59Z");
            }

            if (this._tags.Count > 0)
            {
                foreach (var tag in this._tags)
                {
                    b.Append("&tag=").Append(UrlEncoder.Create().Encode(tag));
                }
            }

            if (this._query != null && this._query.Length > 3)
            {
                b.Append("&query=").Append(UrlEncoder.Default.Encode(_query));
            }

            return b.ToString();
        }


        public NewsItem[] Fetch()
        {
            var f = Feed.FromJson(Http.Sync(_url + QueryParams()));

            if (f.Items == null)
            {
                return new NewsItem[] { };
            }

            return f.Items;
        }

        public async Task<NewsItem[]> FetchAsync()
        {
            var json = await Http.Async(_url + QueryParams());
            var f = Feed.FromJson(json);
            if (f.Items == null)
            {
                return new NewsItem[] { };
            }

            return f.Items;
        }
    }
}