package com.demergis.terranova;

import com.badlogic.gdx.Gdx;

/**
 * Created by Dimitri on 1/23/2016.
 */
public class TimeManager {

    public static double timeRatio = 1.0d; // Ratio of idealized framerate and actual

    private static int defaultFPS = 60;

    public void TimeManager() {

    }

    public static void update() {
        int actualFPS = Gdx.graphics.getFramesPerSecond();
        actualFPS = (actualFPS == 0) ? 300 : actualFPS;
        timeRatio = (double) defaultFPS / actualFPS;
    }
}
