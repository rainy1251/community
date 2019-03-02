package com.service.community.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Levi on 2017/4/22.
 */

public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setVisibility(View.INVISIBLE);
        Glide.with(context).load((String) path).into(imageView);
    }
}
