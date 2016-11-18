package com.webber.retorfitdemo;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by mxh on 2016/6/17.
 * 產品基類
 */
public class BaseProduct   {

    public String availableQuantity;                                        //可購買數量
    public String categoryId;                                               //分類ID
    public String currentPrice;                                             //商品當前價格
    public String disclaimer;                                               //細則
    public String endPublishedDate;                                         //停售時間
    public String fullDesc;                                                 //商品介紹
    public String discount;                                                 //折扣
    public String id;                                                       //商品id
    public String property;                                                 //商品屬性DEALS團購 STORE商鋪
    public String shortTitle;                                               //商品小標題
    public String sku;                                                      //
    public String soldQuantity;                                             //已售數量
    public String startPublishedDate;
    public String status;                                                   //商品狀態預售：PREANNOUNCE 在售：SELLING 結束：SOLDOUT 關閉：CLOSED
    public String stepPurchaseQuantity;
    public String title;                                                     //商品標題
    public String toDealOnQuantity;                                          //差多少人成團
    public ArrayList<ProductResponse.ProductImages> images;                     //商品圖片
    public String[] paymentMethods;                                             //支持的支付方式
    public boolean isFreightNoMatch = false;
    public boolean isLiked;                                                      //該用戶是否收藏
    public String marketPrice;                                                   //商品市場價 折扣前
    public String maxPurchaseQuantity;                                           //最大購買數量
    public String merchantId;                                                    //商戶ID
    public String minPurchaseQuantity;                                           //最小購買數量
    public ArrayList<Location> locations;                                       //換購地點
    public String likedCount;                                                   //收藏人數
    public String redeemEndDate;
    public String redeemStartDate;
    public String subTitles;                                                    //簡介
    public String dealOnDate;                                                   //
    public boolean isPaperless;                                                  //是否支持無紙
    public String isDealsOn;                                                    //
    public String deliveryText;                                                 //額外的航運信息
    public String promotionText;                                                //折扣隱藏信息
    public String shippingRuleId;
    public String istodayfinal;                                                 //最後今天
    public String[] activityTexts;                                              //活動信息
    public String highlight;                                                    //買點

    public int quantity; //商品購買數量
    public String favorableInfo;//优惠信息
    public String parentTitle;
    public String parentId;
    public String refId;
    public String mername; //商鋪名
    public long countDownTime;//倒計時時間 自用字段
    public String ischecked;   //保存收藏狀態

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
