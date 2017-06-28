package com.fang;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 6/26/2017.
 */
public class TestCrower {

    private static final String AMAZON_QUERY_URL = "https://www.amazon.com/s/ref=nb_sb_noss?field-keywords=";

    public TestCrower() {

    }

    public void parseAmazonProdPage(String keyword, Map<String, Ad> map) {
        try {
            String url = AMAZON_QUERY_URL+keyword.replaceAll(" ", "%20");
            // Set up headers
            HashMap<String,String> headers = new HashMap<String,String>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Accept-Language", "en-US,en;q=0.8");

            // Get http requust result
            Document doc = Jsoup.connect(url).maxBodySize(0).headers(headers).timeout(10000).get();

            //Document doc = Jsoup.connect(url).timeout(2000).get();
            System.out.println(url);
            //String detailedUrl = doc.baseUri();
            //System.out.println(detailedUrl);

            Element category = doc.select("#leftNavContainer > ul:nth-child(2) > div > li:nth-child(1) > span > a > h4").first();
            System.out.println(category.text());

            Elements results = doc.getElementsByAttributeValueStarting("id", "result_");

            System.out.println(results.size());
            for(Element element : results) {
                System.out.println(element.attributes().get("data-asin"));
                Element titleElement = element.getElementsByAttribute("data-attribute").first();
                Element priceElement = element.getElementsByAttribute("aria-label").first();
                Element brandElement = element.getElementsByClass("a-size-small a-color-secondary").get(1);
                Element imgUrlElement = element.getElementsByAttribute("src").first();
                Element refUrlElement = element.getElementsByAttribute("href").first();
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



            //#result_0 > div > div > div > div.a-fixed-left-grid-col.a-col-right > div:nth-child(4) > div.a-column.a-span7 > div.a-row.a-spacing-none > a


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
