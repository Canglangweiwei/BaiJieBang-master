package com.water.helper.util;

import android.content.Context;
import android.widget.ImageView;

import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.water.helper.bean.TopAdBean;
import com.youth.banner.loader.ImageLoader;

/**
 * 用于Banner加载图片使用
 */
public class BannerGlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        // 将传递的Object强制转化为自己添加进入的广告实体类
        TopAdBean topAdBean = (TopAdBean) path;
        // Glide 加载图片简单用法
        ImageLoaderUtils.display(context, imageView, topAdBean.getImage());
    }
}
