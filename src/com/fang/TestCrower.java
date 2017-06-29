package com.fang;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 6/26/2017.
 */
public class TestCrower {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    private static final String AMAZON_QUERY_URL = "https://www.amazon.com/s/ref=nb_sb_noss?field-keywords=";

    public TestCrower() {

    }

    public void getAmazonProds(String query, double bidPrice, int campaignId, int queryGroupId, Map<String, Ad> productsRecord) {

        String url = AMAZON_QUERY_URL+query.replaceAll(" ", "%20");

        try {
            // Set up headers
            HashMap<String,String> headers = new HashMap<String,String>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            headers.put("Accept-Encoding", "gzip, deflate, sdch, br");
            headers.put("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4");


            // Get http requust result
            Document doc = Jsoup.connect(url).headers(headers).userAgent(USER_AGENT).timeout(10000).get();

            Element category = doc.select("#leftNavContainer > ul:nth-child(2) > div > li:nth-child(1) > span > a > h4").first();

            // Get product elements in one page
            Elements products = doc.getElementsByAttributeValueStarting("id", "result_");
            //System.out.println("number of products: " + products.size());

            for(Element product : products){
                // get asin, titile, price, brand, imgUrl, refUrl
                try {
                    String asin = product.attributes().get("data-asin");

                    // if Ad exist get the ad from record, if not create a new ad
                    Ad newAd = productsRecord.getOrDefault(asin, new Ad());

                    Element titleElement = product.getElementsByAttribute("title").first();
                    Element priceElement = product.getElementsByAttribute("aria-label").first();
                    Element brandElement = product.getElementsByClass("a-size-small a-color-secondary").get(1);
                    Element imgUrlElement = product.getElementsByAttribute("src").first();
                    Element refUrlElement = product.getElementsByAttribute("href").first();

                    newAd.title = titleElement.attr("title");
                    System.out.println("title: " + newAd.title);

                    String priceStr = null;
                    if(priceElement != null) {
                        priceStr = priceElement.attr("aria-label").trim();
                    } else {
                        priceStr = product.getElementsByClass("a-size-small a-color-secondary").get(3).text();
                    }

                    if(priceStr.contains("-")) {
                        priceStr = priceStr.substring(0, priceStr.indexOf("-"));
                    }
                    priceStr = priceStr.trim().substring(1);
                    priceStr.replace(",", "");
                    newAd.price = Double.parseDouble(priceStr.trim());
                    System.out.println("price: " + newAd.price);
                    newAd.brand = brandElement.text();
                    System.out.println("brand: " + newAd.brand);
                    newAd.thumbnail = imgUrlElement.attr("src");
                    System.out.println("imgUrl: " + newAd.thumbnail);
                    newAd.detailUrl = refUrlElement.attr("href");
                    System.out.println("detailUrl: " + newAd.detailUrl);
                    newAd.keyWords = new ArrayList<>(Arrays.asList(newAd.title.split(" ")));
                    newAd.bidPrice = bidPrice;
                    newAd.campaignId = campaignId;
                    newAd.catagory = category.text();
                    newAd.query = query;
                    newAd.queryGroupId = queryGroupId;

                    productsRecord.put(asin, newAd);
                } catch (Exception exception) {
                    System.out.print("query: " + query + " getting products ");
                    System.out.println(exception.toString());
                }

            }
        }catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("query: " + query + "getting connection");
            e.printStackTrace();
        }


    }
}
