package com.webber.retorfitdemo;


import java.util.ArrayList;

/**
 * Created by mxh on 2016/5/24.
 * 商品详情返回结果封装类
 */
public class ProductResponse extends BaseProduct {

    public ArrayList<ProductSpec> options;                           //規格選項

    public String message;

    public class ProductImages  {
        public ArrayList<Scheme> schemes;


    }

    public enum SchemeType {
        THUMB,
        MEDIUM,
        DISPLAY,
        ORIGINAL
    }

    public class Scheme   {
        public String height;
        public String type;
        public String uri;
        public String width;
    }

    public class MyProductImage   {
        public String height;
        public String uri;
        public String width;
    }

    public class ProductSpec  {
        public String name;
    }


}
