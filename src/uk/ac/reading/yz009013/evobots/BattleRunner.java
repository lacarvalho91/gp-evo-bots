package uk.ac.reading.yz009013.evobots;

import java.io.File;

import robocode.BattleResults;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleErrorEvent;
import uk.ac.reading.yz009013.evobots.tree.FitnessFunction;
import uk.ac.reading.yz009013.evobots.tree.Population;

public class BattleRunner {

	private final static int MAX_GENS = 5000;
	private final static int NUM_ROUNDS = 2;

	private final static String BOT_NAME = "uk.ac.reading.yz009013.evobots.LawrenceCarvalhoEliteGPBot*";
	private final static String SITTING_DUCK = "sample.SittingDuck";
	private final static String RAM_FIRE = "sample.RamFire";
	private final static String FIRE = "sample.Fire";

	private final static String ROBOCODE_PATH = "/Users/admin/robocode";
	private BattlefieldSpecification battlefield;
	private RobocodeEngine engine;
	private BattleObserver observer;
	private final static FitnessFunction FITNESS_FUNCTION = FitnessFunction.SCORE;

	public BattleRunner() {
		System.setProperty("NOSECURITY", "true");
		observer = new BattleObserver();
		RobocodeEngine.setLogMessagesEnabled(false);
		engine = new RobocodeEngine(new File(ROBOCODE_PATH));
		engine.setVisible(true);
		engine.addBattleListener(observer);
		battlefield = new BattlefieldSpecification(800, 800);
	}

	public double runBattle() {
		RobotSpecification[] robots = null;
		BattleResults[] results;

		robots = engine.getLocalRepository(BOT_NAME + "," + SITTING_DUCK + ","
				+ SITTING_DUCK + "," + SITTING_DUCK + "," + SITTING_DUCK + ","
				+ SITTING_DUCK);

		// robots = engine.getLocalRepository(BOT_NAME + "," + FIRE + ","
		// + RAM_FIRE);

		// robots = engine.getLocalRepository(BOT_NAME + "," + RAM_FIRE);

		BattleSpecification battleSpec = new BattleSpecification(NUM_ROUNDS,
				battlefield, robots);
		engine.runBattle(battleSpec, true);
		engine.close();
		results = observer.getResults();

		double score = 0;

		switch (FITNESS_FUNCTION) {
		case BULLET_DAMAGE:
			score = results[0].getBulletDamage() / battleSpec.getNumRounds();
		case SCORE:
			double playerScore = results[0].getScore();
			double totalEnemyScore = 0;
			int c = 5;
			for (int i = 1; i < results.length; i++) {
				totalEnemyScore += results[i].getScore();
			}
			score = ((c + playerScore) / (c + playerScore + totalEnemyScore))
					/ battleSpec.getNumRounds();
		default:
			score = results[0].getBulletDamage() / battleSpec.getNumRounds();
		}

		return score;
	}

	public static void main(String args[]) {
		System.out.println("Initialising population...");
		Population population = new Population();
		for (int i = 0; i < MAX_GENS; i++) {
			System.out.println("----------Generation: " + (i + 1)
					+ "----------");
			// population.eliteEvolve();
			population.nonEliteEvolve();
		}
	}
}

class BattleObserver extends BattleAdaptor {

	robocode.BattleResults[] results;

	public void onBattleCompleted(BattleCompletedEvent e) {
		results = e.getIndexedResults();
	}

	public void onBattleError(BattleErrorEvent e) {
		System.out.println("Error running battle: " + e.getError());
	}

	public BattleResults[] getResults() {
		return results;
	}

}