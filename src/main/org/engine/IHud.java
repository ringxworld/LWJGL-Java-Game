package main.org.engine;

import main.org.engine.items.GameItem;

public interface IHud {

	GameItem[] getGameItems();

	default void cleanup() {
		GameItem[] gameItems = getGameItems();
		for (GameItem gameItem : gameItems) {
			gameItem.getMesh().cleanUp();
		}
	}
}