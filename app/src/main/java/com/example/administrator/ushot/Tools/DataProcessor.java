package com.example.administrator.ushot.Tools;

/**
 * Created by atsst on 2017/10/10.
 */

public class DataProcessor {

    public static float process(float f) {
        if (f > 0)
            f = f * 100 + 45;
        else
            f = 70 + f * 20;
        if (f <= 0)
            f = 15;
        if (f > 100)
            f = 100;
        return f;
    }
}
