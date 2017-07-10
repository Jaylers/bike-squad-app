package com.jaylerrs.bikesquad.utility.manager;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import static android.graphics.Color.parseColor;

/**
 * Created by jaylerr on 24-Mar-17.
 */

public class ColorManager {

    public ColorManager() {
    }

    public int colorParser(String color){
        return Color.parseColor((color.contains("#")) ? color : "#".concat(color));
    }

    public int modifyBrightness(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }

    public GradientDrawable getColorDrawable(String color){
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[] {colorParser(parser(color)),
                        modifyBrightness(colorParser(parser(color)), (float) 1.0)});
        gradientDrawable.setCornerRadius(0f);
        return gradientDrawable;
    }

    public GradientDrawable getGradientDrawable(String color){
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[] {colorParser(parser(color)),
                        modifyBrightness(colorParser(parser(color)), (float) 0.5)});
        gradientDrawable.setCornerRadius(0f);
        return gradientDrawable;
    }

    public GradientDrawable getTwoShadeGradientDrawable(String start, String end){
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[] {colorParser(parser(start)), colorParser(parser(end))});
        gradientDrawable.setCornerRadius(0f);
        return gradientDrawable;
    }

    public GradientDrawable getThreeShadeGradientDrawable(String start, String center, String end){
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[] {colorParser(parser(start)), colorParser(parser(center)), colorParser(parser(end))});
        gradientDrawable.setCornerRadius(0f);
        return gradientDrawable;
    }

    public String parser(String color){
        return (color.contains("#"))? color : "#".concat(color);
    }

    public int parserToInt(String color){ return parseColor(parser(color));}
}