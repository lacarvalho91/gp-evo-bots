package uk.ac.reading.yz009013.evobots.tree;

import java.util.Random;

public enum Actuator {
	MOVE_TANK("MoveTank"), TURN_TANK_RIGHT("TurnTankRight"), TURN_GUN_RIGHT(
			"TurnGunRight");

	private String name;

	Actuator(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static Actuator getRandomActuator() {
		Random r = new Random();
		return values()[r.nextInt(values().length)];
	}
}
