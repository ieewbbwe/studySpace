package com.webber.myapplication;

import com.android_mobile.core.net.http.ServiceResponse;

/**
 * Created by mxh on 2016/6/21.
 * 個人信息封裝類
 */
public class BuyerInfoModel extends ServiceResponse {
    public String buyerFirstName;
    public String buyerLastName;
    public String buyerPhone;
    public String receiverFirstName;
    public String receiverLastName;
    public String receiverPhone;
    public String deliveryDistrict;
    public String deliveryAddress1;
    public String deliveryAddress2;

    public boolean isSaveContactInfo;//保存聯絡資料
    public boolean isLikeToPerson;//與個人資料一致

    public BuyerInfoModel(String buyerFirstName, String buyerLastName) {
        this.buyerFirstName = buyerFirstName;
        this.buyerLastName = buyerLastName;
    }

    @Override
    public String toString() {
        return "BuyerInfoModel{" +
                "buyerFirstName='" + buyerFirstName + '\'' +
                ", buyerLastName='" + buyerLastName + '\'' +
                ", buyerPhone='" + buyerPhone + '\'' +
                ", receiverFirstName='" + receiverFirstName + '\'' +
                ", receiverLastName='" + receiverLastName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", deliveryDistrict='" + deliveryDistrict + '\'' +
                ", deliveryAddress1='" + deliveryAddress1 + '\'' +
                ", deliveryAddress2='" + deliveryAddress2 + '\'' +
                ", isSaveContactInfo=" + isSaveContactInfo +
                ", isLikeToPerson=" + isLikeToPerson +
                '}';
    }
}
