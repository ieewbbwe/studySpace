package com.webber.retorfitdemo;

/**
 * Created by mxh on 2016/6/21.
 * 個人信息封裝類
 */
public class BuyerInfoModel {
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
