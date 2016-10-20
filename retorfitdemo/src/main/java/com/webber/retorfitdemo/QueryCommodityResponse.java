package com.webber.retorfitdemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Mr.fan on 2016/7/15.
 */
public class QueryCommodityResponse {

    public QueryCommodity query;

    public class QueryCommodity {
        public int count;
        public String created;
        public String lang;
        public QueryCommodityResult results;
    }


    public class QueryCommodityResult{
        public int total;
        public ArrayList<Commodity> product;
        public ArrayList<Category> category;
        public ArrayList<Category> categoryInfo;
        public ArrayList<Seller> seller;
    }

    public class Category {
        public String title;
        public String id;
        public int level;
        public int productCount;
        public String parent;
    }

    public class Commodity extends BaseProduct {
        public String shortDesc;                                               //商品小標題
        public ArrayList<ImageOne> image;                     //商品圖片
        public String buyPrice;
        public long startTime;
        public long endTime;
        public String discription;
        public Extra extra;
        public String[] option;
        public int salesVolume;
        public int discountPercent;

        public boolean isNearlySell() {

            return startTime != 0 && (startTime * 1000) - System.currentTimeMillis() > 0;
        }

        public boolean isOptionContains(String optionStr) {
            if (option != null) {
                for (String op : option) {
                    if (op.equals(optionStr)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public String TimeStamp2Date(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        return new SimpleDateFormat(formats).format(new java.util.Date(timestamp));
    }

    public class Extra {
        public float shippingFee;
        public Seller seller;
    }

    public class ImageOne{
        public ArrayList<Image> image;
    }

    public class Seller {
        public String id;
        public String title;
        public String url;
        public int ratingAvg;
        public int productCount;
    }

    public class Image {
        public String url;
        public String id;
        public String width;
        public String height;
    }

}
