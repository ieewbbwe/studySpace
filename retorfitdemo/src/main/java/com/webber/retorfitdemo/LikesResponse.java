package com.webber.retorfitdemo;

import java.util.List;

/**
 * Created by lh on 2016/7/12.
 */
public class LikesResponse {


    /**
     * likeObjects : [{"id":"PRODUCT180918","targetId":"180918","targetType":"PRODUCT"},{"id":"PRODUCT180927","targetId":"180927","targetType":"PRODUCT"},{"id":"PRODUCT180934","targetId":"180934","targetType":"PRODUCT"},{"id":"PRODUCT177056","targetId":"177056","targetType":"PRODUCT"},{"id":"PRODUCT1014","targetId":"1014","targetType":"PRODUCT"},{"id":"PRODUCT178761","targetId":"178761","targetType":"PRODUCT"}]
     * resultsTotal : 6
     */

    public String resultsTotal;
    /**
     * id : PRODUCT180918
     * targetId : 180918
     * targetType : PRODUCT
     */

    public List<LikesItem> likeObjects;



}
