package com.discoverfriend.partybear.category;

/**
 * Created by mukesh on 21/01/17.
 */

public class CategoryModel {
    String categoryName;
    String categoryImage;

    public CategoryModel() {
    }

    public CategoryModel(String categoryName, String categoryImage) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }


}
