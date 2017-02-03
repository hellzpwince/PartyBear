package com.discoverfriend.partybear.cart;

/**
 * Created by mukesh on 01/02/17.
 */

public class CartModel {
    private String productid, imageurl, name, producttype;
    private long total;

    public CartModel() {
    }

    public CartModel(String productid, String imageurl, String name, long total, String producttype) {
        this.productid = productid;
        this.imageurl = imageurl;
        this.name = name;
        this.total = total;
        this.producttype = producttype;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }
}
