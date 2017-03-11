package com.discoverfriend.partybear.cart;

/**
 * Created by mukesh on 01/02/17.
 */

public class CartModel {
    private String productid, imageurl, name, producttype;
    private long total, time_charge, type_charge, price, image_required;

    public CartModel() {
    }

    public CartModel(String productid, String imageurl, String name, String producttype, long total, long time_charge, long type_charge, long price, long image_required) {
        this.productid = productid;
        this.imageurl = imageurl;
        this.name = name;
        this.producttype = producttype;
        this.total = total;
        this.time_charge = time_charge;
        this.type_charge = type_charge;
        this.price = price;
        this.image_required = image_required;
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

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTime_charge() {
        return time_charge;
    }

    public void setTime_charge(long time_charge) {
        this.time_charge = time_charge;
    }

    public long getType_charge() {
        return type_charge;
    }

    public void setType_charge(long type_charge) {
        this.type_charge = type_charge;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getImage_required() {
        return image_required;
    }

    public void setImage_required(long image_required) {
        this.image_required = image_required;
    }
}
