package com.service.community.ui.view;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.List;

public class BannerView {
    private static Banner banner;
    private static List<?> imageUrls;
    private static boolean isAutoPlay;

    /**
     * 开始轮播
     * @param banner
     * @param imageUrls
     */
    public static void startBanner(Banner banner, List<?> imageUrls,List<String> titles, boolean isAutoPlay){
        BannerView.banner = banner;
        BannerView.imageUrls = imageUrls;
        BannerView.isAutoPlay = isAutoPlay;
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new GlideImageVisibleLoader())
//                .setBannerAnimation(Transformer.Accordion)
                .setImages(imageUrls)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .isAutoPlay(isAutoPlay)
                .setBannerTitles(titles)
                .start();
    }
    /**
     * 开始轮播
     * @param banner
     * @param imageUrls
     */
    public static void startBanner(Banner banner, List<?> imageUrls, boolean isAutoPlay){
        BannerView.banner = banner;
        BannerView.imageUrls = imageUrls;
        BannerView.isAutoPlay = isAutoPlay;
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setImageLoader(new GlideImageLoader())
//                .setBannerAnimation(Transformer.Accordion)
                .setImages(imageUrls)
                .isAutoPlay(true)
                .setDelayTime(3000)
                .isAutoPlay(isAutoPlay)
                .start();
    }

}