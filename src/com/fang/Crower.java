package com.fang;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.HashMap;

/**
 * Created by Administrator on 6/26/2017.
 */
public class Crower {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
    private final String authUser = "bittiger";
    private final String authPassword = "cs504";
    private static final String AMAZON_QUERY_URL = "https://www.amazon.com/s/ref=nb_sb_noss?field-keywords=";
    public void initProxy() {
        //System.setProperty("socksProxyHost", "199.101.97.161"); // set socks proxy server
        //System.setProperty("socksProxyPort", "61336"); // set socks proxy port

        System.setProperty("http.proxyHost", "199.101.97.159"); // set proxy server
        System.setProperty("http.proxyPort", "60099"); // set proxy port
        //System.setProperty("http.proxyUser", authUser);
        //System.setProperty("http.proxyPassword", authPassword);
        Authenticator.setDefault(
                new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                authUser, authPassword.toCharArray());
                    }
                }
        );
    }
    public void testProxy() {

        String test_url = "http://www.toolsvoid.com/what-is-my-ip-address";
        try {
            HashMap<String,String> headers = new HashMap<String,String>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Accept-Language", "en-US,en;q=0.8");
            Document doc = Jsoup.connect(test_url).headers(headers).userAgent(USER_AGENT).timeout(10000).get();
            String iP = doc.select("body > section.articles-section > div > div > div > div.col-md-8.display-flex > div > div.table-responsive > table > tbody > tr:nth-child(1) > td:nth-child(2) > strong").first().text(); //get used IP.
            System.out.println("IP-Address: " + iP);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void getAmazonProds(String query) {

        String url = AMAZON_QUERY_URL+query.replaceAll(" ", "%20");
        System.out.println("url: " + url);

        try {
            // Set up headers
            HashMap<String,String> headers = new HashMap<String,String>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            headers.put("Accept-Encoding", "gzip, deflate, sdch, br");
            headers.put("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4");


            // Get http requust result
            Document doc = Jsoup.connect(url).headers(headers).userAgent(USER_AGENT).timeout(10000).get();

            // Get page size
            Integer docSize = doc.text().length();
            System.out.println("page size: " + docSize);

            // Get category of products
            //Element categoryElement = doc.select("#leftNavContainer > ul:nth-child(2) > div > li:nth-child(1) > span > a > h4").first();
            //String category = categoryElement.text();
            //System.out.println(categoryElement.toString());
            //System.out.println(categoryElement.text());

            Element category = doc.select("#leftNavContainer > ul:nth-child(2) > div > li:nth-child(1) > span > a > h4").first();
            System.out.println(category.text());
            // Get product elements in one page
            Elements products = doc.getElementsByAttributeValueStarting("id", "result_");
            System.out.println("number of products: " + products.size());

            for(Element product : products){
                // get asin, titile, price, brand, imgUrl, refUrl
                System.out.println(product.attributes().get("data-asin"));
                Element titleElement = product.getElementsByAttribute("data-attribute").first();
                Element priceElement = product.getElementsByAttribute("aria-label").first();
                Element brandElement = product.getElementsByClass("a-size-small a-color-secondary").get(1);
                Element imgUrlElement = product.getElementsByAttribute("src").first();
                Element refUrlElement = product.getElementsByAttribute("href").first();
                try {
                    System.out.println(titleElement.attr("data-attribute"));
                    System.out.println(Double.parseDouble(priceElement.attr("aria-label").substring(1)));
                    System.out.println(brandElement.text());
                    System.out.println(imgUrlElement.attr("src"));
                    System.out.println(refUrlElement.attr("href"));
                } catch (Exception exception) {
                    System.out.println(exception.toString());
                }

            }
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public void parseAmazonProdPage(String url) {
        try {
            HashMap<String,String> headers = new HashMap<String,String>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Accept-Language", "en-US,en;q=0.8");
            Document doc = Jsoup.connect(url).headers(headers).userAgent(USER_AGENT).timeout(10000).get();
            Element titleEle = doc.getElementById("productTitle");
            String title = titleEle.text();
            System.out.println("title: " + title);

            Element priceEle =doc.getElementById("priceblock_ourprice");
            String price = priceEle.text();
            System.out.println("price: " + price);

            //review
            //#cm-cr-dp-review-list
            Elements reviews = doc.getElementsByClass("a-expander-content a-expander-partial-collapse-content");
            System.out.println("number of reviews: " + reviews.size());
            for (Element review : reviews) {
                System.out.println("review content: " + review.text());
            }

            //#customer_review-R188VC0CBW8NLR > div:nth-child(4) > span > div > div.a-expander-content.a-expander-partial-collapse-content



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}