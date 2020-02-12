using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace MFNClient.models
{
    public static class Enums
    {
        public const string TypeAll = "all";
        public const string TypePr = "pr";
        public const string TypeIr = "ir";
    }

    internal static class Eq
    {
        public static bool AreEqual<T>(T[] a, T[] b)
        {
            return AreEqual(a, b, EqualityComparer<T>.Default);
        }

        public static bool AreEqual<T>(T[] a, T[] b, IEqualityComparer<T> comparer)
        {
            if (a == b)
            {
                return true;
            }

            if (a == null || b == null)
            {
                return false;
            }

            if (a.Length != b.Length)
            {
                return false;
            }

            for (int i = 0; i < a.Length; i++)
            {
                if (!comparer.Equals(a[i], b[i]))
                {
                    return false;
                }
            }

            return true;
        }
    }

    public partial class Feed
    {
        [JsonProperty("items")] public NewsItem[] Items { get; set; }
    }

    public partial class NewsItem
    {
        [JsonProperty("news_id")] public Guid NewsId { get; set; }

        [JsonProperty("group_id")] public Guid GroupId { get; set; }

        [JsonProperty("url")] public Uri Url { get; set; }

        [JsonProperty("author")] public Entity Author { get; set; }

        [JsonProperty("subjects")] public Entity[] Subjects { get; set; }

        [JsonProperty("properties")] public Properties Properties { get; set; }

        [JsonProperty("content")] public Content Content { get; set; }

        [JsonProperty("extensions")] public Extensions Extensions { get; set; }

        [JsonProperty("source")] public string Source { get; set; }


        protected bool Equals(NewsItem other)
        {
            return NewsId.Equals(other.NewsId) &&
                   GroupId.Equals(other.GroupId) &&
                   Equals(Url, other.Url)
                   && Equals(Author, other.Author)
                   && Eq.AreEqual(Subjects, other.Subjects)
                   && Equals(Properties, other.Properties)
                   && Equals(Content, other.Content)
                   && Equals(Extensions, other.Extensions)
                   && Source == other.Source;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((NewsItem) obj);
        }
    }

    public partial class Entity
    {
        [JsonProperty("entity_id")] public Guid EntityId { get; set; }

        [JsonProperty("slug")] public string Slug { get; set; }

        [JsonProperty("slugs")] public string[] Slugs { get; set; }

        [JsonProperty("name")] public string Name { get; set; }

        [JsonProperty("brand_image_url")] public Uri BrandImageUrl { get; set; }

        [JsonProperty("isins")] public string[] Isins { get; set; }

        [JsonProperty("leis")] public string[] Leis { get; set; }

        [JsonProperty("local_refs")] public string[] LocalRefs { get; set; }

        [JsonProperty("tickers")] public string[] Tickers { get; set; }


        protected bool Equals(Entity other)
        {
            return EntityId.Equals(other.EntityId)
                   && Slug == other.Slug
                   && Eq.AreEqual(Slugs, other.Slugs)
                   && Name == other.Name
                   && Equals(BrandImageUrl, other.BrandImageUrl)
                   && Eq.AreEqual(Isins, other.Isins)
                   && Eq.AreEqual(Leis, other.Leis)
                   && Eq.AreEqual(LocalRefs, other.LocalRefs)
                   && Eq.AreEqual(Tickers, other.Tickers);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Entity) obj);
        }
    }

    public partial class Content
    {
        [JsonProperty("title")] public string Title { get; set; }

        [JsonProperty("slug")] public string Slug { get; set; }

        [JsonProperty("publish_date")] public DateTimeOffset PublishDate { get; set; }

        [JsonProperty("preamble")] public string Preamble { get; set; }

        [JsonProperty("html")] public string Html { get; set; }

        [JsonProperty("text")] public string Text { get; set; }

        [JsonProperty("attachments")] public Attachment[] Attachments { get; set; }

        protected bool Equals(Content other)
        {
            return Title == other.Title
                   && Slug == other.Slug
                   && PublishDate.Equals(other.PublishDate)
                   && Preamble == other.Preamble
                   && Html == other.Html
                   && Text == other.Text
                   && Eq.AreEqual(Attachments, other.Attachments);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Content) obj);
        }
    }

    public partial class Attachment
    {
        [JsonProperty("file_title")] public string FileTitle { get; set; }

        [JsonProperty("content_type")] public string ContentType { get; set; }

        [JsonProperty("url")] public Uri Url { get; set; }

        [JsonProperty("tags")] public string[] Tags { get; set; }

        protected bool Equals(Attachment other)
        {
            return FileTitle == other.FileTitle
                   && ContentType == other.ContentType
                   && Equals(Url, other.Url)
                   && Eq.AreEqual(Tags, other.Tags);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Attachment) obj);
        }
    }

    public partial class Extensions
    {
        public override bool Equals(object obj)
        {
            return true;
        }
    }

    public partial class Properties
    {
        [JsonProperty("lang")] public string Lang { get; set; }

        [JsonProperty("tags")] public string[] Tags { get; set; }

        [JsonProperty("type")] public string Type { get; set; }

        protected bool Equals(Properties other)
        {
            return Lang == other.Lang && Eq.AreEqual(Tags, other.Tags) && Type == other.Type;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Properties) obj);
        }
    }

    public partial class Feed
    {
        public static Feed FromJson(string json) => JsonConvert.DeserializeObject<Feed>(json, Converter.Settings);
    }

    public static class Serialize
    {
        public static string ToJson(this Feed self) => JsonConvert.SerializeObject(self, Converter.Settings);
    }

    internal static class Converter
    {
        public static readonly JsonSerializerSettings Settings = new JsonSerializerSettings
        {
            MetadataPropertyHandling = MetadataPropertyHandling.Ignore,
            DateParseHandling = DateParseHandling.None,
            Converters =
            {
                new IsoDateTimeConverter {DateTimeStyles = DateTimeStyles.AssumeUniversal}
            },
        };
    }
}