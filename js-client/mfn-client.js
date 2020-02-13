"use strict"

var XMLHttpRequest;
if (!XMLHttpRequest && require) {
    XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
}


var MFNClient = (function () {

    function get(url, array, callback) {

        var p;
        var resolve;
        var reject;

        if (!!Promise) {
            p = new Promise(function (res, rej) {
                resolve = res;
                reject = rej;
            })
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                var j = JSON.parse(xhttp.responseText);
                var res = j.items || [];
                if (!array) {
                    res = res[0];
                }

                if (!!callback) {
                    callback(res)
                }
                if (!!resolve) {
                    resolve(res)
                }
            }
            if (this.readyState === 4 && this.status !== 200) {
                var err = this.responseText;
                if (!!callback) {
                    callback(null, err)
                }
                if (!!reject) {
                    reject(err)
                }
            }
        };
        xhttp.open("GET", url);
        xhttp.send();
        return p;
    }

    function Filter(url) {
        this._url = url;
        this._limit = 25;
        this._offset = 0;
        this._tags = [];
        this._type = "all";
        this._lang = "";
        this._year = 0;
        this._query = "";

        this.limit = function (limit) {
            this._limit = limit;
            return this;
        };
        this.offset = function (offset) {
            this._offset = offset;
            return this;
        };
        this.lang = function (lang) {
            this._lang = lang;
            return this;
        };
        this.year = function (year) {
            this._year = year;
            return this;
        };
        this.query = function (query) {
            this._query = query;
            return this;
        };
        this.type = function (type) {
            this._type = type;
            return this;
        };
        this.hasTag = function (tag) {
            this._tags.push(tag);
            return this;
        };

        this._value = function () {
            var q = "";
            q += "&limit=" + this._limit;
            q += "&offset=" + this._offset;
            q += "&type=" + this._type;

            if (this._lang && this._lang.length === 2) {
                q += "&lang=" + this._lang;
            }

            if (1900 < this._year && this._year < 2100) {
                q += "&from=" + this._year + "-01-01T00%3A00%3A00Z";
                q += "&to=" + this._year + "-12-31T23%3A59%3A59Z";
            }

            if (this._tags && this._tags.length > 0) {
                for (var i = 0; i < this._tags.length; i++) {
                    q += "&tag=" + encodeURIComponent((this._tags[i]));
                }
            }

            if (this._query && this._query.length > 3) {
                q += "&query=" + encodeURIComponent(this._query);
            }
            return q
        };

        this.fetch = function (callback) {
            return get(this._url + this._value(), true, callback)
        }
    }

    return {

        create: function (baseUrl, entityId) {
            return {
                TYPE_ALL: "all",
                TYPE_IR: "ir",
                TYPE_PR: "pr",
                item: function (newsSlug, callback) {
                    var url = baseUrl + "/all/a.json?type=all&.author.entity_id=" + entityId + "&news_slug=" + newsSlug;
                    return get(url, false, callback)

                },
                itemById: function (newsId, callback) {
                    var url = baseUrl + "/all/a.json?type=all&.author.entity_id=" + entityId + "&news_id=" + newsId;
                    return get(url, false, callback)
                },
                feed: function () {
                    return new Filter(baseUrl + "/all/a.json?.author.entity_id=" + entityId)
                }
            }
        }
    }
})();


if (module && module.exports) {
    module.exports = {
        create: MFNClient.create
    };
}
