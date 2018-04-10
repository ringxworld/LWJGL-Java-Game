package main.org.game;

import main.org.engine.GameEngine;
import main.org.engine.IGameLogic;

public class Driver {

	public static void main(String[] args) {
		try {
			boolean vSync = true;
			IGameLogic gameLogic = new Game();
			GameEngine gameEng = new GameEngine("Temp", 600, 480, vSync, gameLogic);
			gameEng.start();
		} catch (Exception excp) {
			excp.printStackTrace();
			System.exit(-1);
		}
	}
}