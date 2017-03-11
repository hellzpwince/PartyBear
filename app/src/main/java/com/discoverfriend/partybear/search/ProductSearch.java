package com.discoverfriend.partybear.search;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by mukesh on 10/02/17.
 */

public class ProductSearch implements SearchSuggestion {
    private String product;

    @Override
    public String getBody() {
        return this.product;
    }

    public ProductSearch(Parcel source) {
        this.product = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductSearch> CREATOR = new Creator<ProductSearch>() {
        @Override
        public ProductSearch createFromParcel(Parcel in) {
            return new ProductSearch(in);
        }

        @Override
        public ProductSearch[] newArray(int size) {
            return new ProductSearch[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
