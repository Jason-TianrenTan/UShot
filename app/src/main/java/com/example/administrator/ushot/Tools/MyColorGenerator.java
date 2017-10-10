package com.example.administrator.ushot.Tools;

import com.manolovn.trianglify.generator.color.ColorGenerator;
import com.manolovn.colorbrewer.ColorBrewer;

/**
 * Created by fingr on 2017/10/10.
 */

public class MyColorGenerator implements ColorGenerator {
    private ColorBrewer palette;
    private int index = 0;
    private int[] colors;

    public MyColorGenerator(ColorBrewer palette) {
        this.palette = palette;
    }

    public void setCount(int count) {
        colors = palette.getColorPalette(count);
        index = 0;
    }

    @Override
    public int nextColor() {
        int color = colors[index];
        index++;
        return color;
    }
}
