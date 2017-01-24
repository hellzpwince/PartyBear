package com.discoverfriend.partybear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by mukesh on 19/01/17.
 */

public class ImageSlider extends BaseSliderView {
    public ImageSlider(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.image_slider_layout, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        bindEventAndShow(v, target);
        return v;
    }

}
