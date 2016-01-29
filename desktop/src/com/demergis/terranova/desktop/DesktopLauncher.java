package com.demergis.terranova.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.demergis.terranova.TerraNova;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        int fps = 60;
        config.width = 1600;
        config.height = 900;

		new LwjglApplication(new TerraNova(), config);
	}
}
