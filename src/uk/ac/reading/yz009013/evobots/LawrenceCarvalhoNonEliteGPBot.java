package uk.ac.reading.yz009013.evobots;

import uk.ac.reading.yz009013.evobots.tree.Actuator;
import uk.ac.reading.yz009013.evobots.tree.serializer.NodeDeserializer;

public class LawrenceCarvalhoNonEliteGPBot extends
		AbstractLawrenceCarvalhoGPBot {

	private String robotName = "NonElite";

	public void run() {
		setMoveTank(NodeDeserializer.deserializeBestNode(Actuator.MOVE_TANK,
				robotName));
		setTurnTankRight(NodeDeserializer.deserializeBestNode(
				Actuator.TURN_TANK_RIGHT, robotName));
		setTurnGunRight(NodeDeserializer.deserializeBestNode(
				Actuator.TURN_GUN_RIGHT, robotName));
		while (true) {
			turnGunRight(5);
		}
	}

	// public static void main(String[] args) {
	// BattleRunner runner = new BattleRunner();
	//
	// runner.runBattle();
	// }
}
