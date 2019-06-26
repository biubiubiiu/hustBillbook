package com.example.hustbillbook.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;


public class ImageUtils
{
    /**
     * 根据图片url设置与之相对应的本地图片
     * @param imgUrl - String（不带后缀名）
     * @return Drawable
     */
    public static Drawable getDrawable(@NotNull Context context, @NonNull String imgUrl) {
        int id = getImageId(context, imgUrl);
        return context.getResources().getDrawable(id, null);
    }

    /**
     * 根据图片url找到对应本地图片的id
     * @param imgUrl - String（不带后缀名）
     * @return Drawable
     */
    public static int getImageId(@NotNull Context context, @NotNull String imgUrl) {
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(imgUrl, "drawable", packageName);
    }

}
