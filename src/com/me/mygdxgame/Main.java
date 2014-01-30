package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Rolit       \u00a9 Max Messerich & Joeri Kock";
		cfg.useGL20 = false;
		cfg.width = 900;
		cfg.height = 600;
		cfg.resizable = true;
		new LwjglApplication(new RollIt(), cfg);
	}
}
