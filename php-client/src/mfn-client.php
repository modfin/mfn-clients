<?php


namespace MFN;

define("TYPE_ALL", "all");
define("TYPE_IR", "ir");
define("TYPE_PR", "pr");





class Client
{

    private $baseUrl;
    private $feedId;

    /**
     * MFNClient constructor.
     * @param $baseUrl
     * @param $feedId
     */
    public function __construct($baseUrl, $feedId)
    {
        $this->baseUrl = $baseUrl;
        $this->feedId = $feedId;
    }

    /**
     * @return Filter a request builder
     */
    public function feed()
    {
        return new Filter($this->baseUrl . "/feed/" . $this->feedId);
    }

    /**
     * @param string $newsSlug
     * @return array NewsItem
     */
    public function item($newsSlug)
    {
        $url = $this->baseUrl . "/feed/" . $this->feedId . "?news-slug=" . $newsSlug;
        return get($url, 0);
    }

    /**
     * @param string $newsId
     * @return array NewsItem
     */
    public function itemById($newsId)
    {
        $url = $this->baseUrl . "/feed/" . $this->feedId . "?news-id=" . $newsId;
        return get($url, 0);
    }


}

class Filter
{
    private $url;

    private $limit;
    private $offset;
    private $type;
    private $lang;

    private $year;
    private $tags;
    private $query;


    public function __construct($url)
    {
        $this->url = $url;
        $this->limit = 25;
        $this->offset = 0;
        $this->type = TYPE_ALL;
        $this->tags = [];
        $this->year = 0;
        $this->query = "";
        $this->lang = "";
    }

    /**
     * @param int $limit
     * @return Filter
     */
    public function limit($limit)
    {
        $this->limit = $limit;
        return $this;
    }

    /**
     * @param int $offset
     * @return Filter
     */
    public function offset($offset)
    {
        $this->offset = $offset;
        return $this;
    }

    /**
     * @param string $type
     * @return Filter
     */
    public function type($type)
    {
        $this->type = $type;
        return $this;
    }

    /**
     * @param string $lang
     * @return Filter
     */
    public function lang($lang)
    {
        $this->lang = $lang;
        return $this;
    }

    /**
     * @param integer $year
     * @return Filter
     */
    public function year($year)
    {
        $this->year = $year;
        return $this;
    }

    /**
     * @param string $tags
     * @return Filter
     */
    public function hasTag($tag)
    {
        array_push($this->tags, $tag);
        return $this;
    }

    /**
     * @param string $query
     * @return Filter
     */
    public function query($query)
    {
        $this->query = $query;
        return $this;
    }


    private function value()
    {
        $q = "?limit=" . $this->limit;
        $q .= "&offset=" . $this->offset;
        $q .= "&type=" . $this->type;

        if (is_string($this->lang) && strlen($this->lang) == 2) {
            $q .= "&lang=" . $this->lang;
        }
        if (is_int($this->year) && 1900 < $this->year && $this->year < 2100) {
            $q .= "&from=" . $this->year . "-01-01T00%3A00%3A00Z";
            $q .= "&to=" . $this->year . "-12-31T23%3A59%3A59Z";
        }

        if(is_array($this->tags)){
            foreach ($this->tags as $tag) {
                $q .= "&tag=" . urlencode($tag);
            }
        }

        if(is_string($this->query) && strlen($this->query) > 3){
            $q .= "&query=" . urlencode($this->query);
        }
        return $q;
    }


    public function fetch(){
        return get($this->url . $this->value(), 1);

    }
}

function get($url, $isarray){
    $json = "";
    if( ini_get('allow_url_fopen') ) {
        $json = file_get_contents($url);
    }else {
        $curl = curl_init($url);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        $response = curl_exec($curl);
        $json = $response . PHP_EOL;
    }

    $items =  json_decode($json)->items;

    if(is_null($items)){
        if($isarray == 1){
            return array();
        }
        return null;
    }

    if($isarray == 1){
        return $items;
    }

    return $items[0];

}
