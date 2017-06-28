package com.fang;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 6/28/2017.
 */
public class Ad implements Serializable {
    private static final long sericalVersionUID = 1L;
    public int adId;
    public int campaignId;
    public List<String> keyWords;
    public double relevanceScore;
    public double pClick;
    public double bidPrice;
    public double rankScore;
    public double qualityScore;
    public double costPerClick;
    public int position;
    public String title;
    public double price;
    public String thumbnail;
    public String description;
    public String brand;
    public String detailUrl;
    public String query;
    public int queryGroupId;
    public String catagory;
}
