package uk.ac.reading.yz009013.evobots;

import java.util.Random;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import uk.ac.reading.yz009013.evobots.tree.Node;

public class AbstractLawrenceCarvalhoGPBot extends AdvancedRobot {

	private Node moveTank;
	private Node turnTankRight;
	private Node turnGunRight;

	// enemy properties
	private double enemyBearing;
	private double enemyDist;
	private double enemyVelocity;
	private double enemyHeading;
	private double enemyEnergy;

	public void onScannedRobot(ScannedRobotEvent e) {
		this.enemyBearing = e.getBearing();
		this.enemyDist = e.getDistance();
		this.enemyVelocity = e.getVelocity();
		this.enemyHeading = e.getHeading();
		this.enemyEnergy = e.getEnergy();

		ahead(parseNode(moveTank));
		turnRight(parseNode(turnTankRight));
		turnGunRight(parseNode(turnGunRight));
		fire(3);
	}

	public double runFunc(Node node) {
		switch (node.getExpressionId()) {
		case 0:
			return parseNode(node.getLeftChild())
					+ parseNode(node.getRightChild());
		case 1:
			return parseNode(node.getLeftChild())
					- parseNode(node.getRightChild());
		case 2:
			return parseNode(node.getLeftChild())
					* parseNode(node.getRightChild());
		case 3:
			return parseNode(node.getLeftChild())
					/ parseNode(node.getRightChild());
		case 4:
			return Math.abs(parseNode(node.getLeftChild()));
		case 5:
			return Math.abs(parseNode(node.getRightChild()));
		case 6:
			return 0 - Math.abs(parseNode(node.getLeftChild()));
		case 7:
			return 0 - Math.abs(parseNode(node.getRightChild()));
		case 8:
			return Math.sin(parseNode(node.getLeftChild()));
		case 9:
			return Math.sin(parseNode(node.getRightChild()));
		case 10:
			return Math.cos(parseNode(node.getLeftChild()));
		case 11:
			return Math.cos(parseNode(node.getRightChild()));
		case 12:
			return Math.asin(parseNode(node.getLeftChild()));
		case 13:
			return Math.asin(parseNode(node.getRightChild()));
		case 14:
			return Math.acos(parseNode(node.getLeftChild()));
		default:
			return Math.acos(parseNode(node.getRightChild()));
		}
	}

	public double parseNode(Node node) {
		if (node.isTerminal()) {
			switch (node.getExpressionId()) {
			case 0:
				return (int) this.getEnergy();
			case 1:
				return (int) this.getHeading();
			case 2:
				return (int) this.getX();
			case 3:
				return (int) this.getY();
			case 4:
				return (int) this.getBattleFieldHeight();
			case 5:
				return (int) this.getBattleFieldWidth();
			case 6:
				return this.enemyBearing;
			case 7:
				return this.enemyDist;
			case 8:
				return this.enemyVelocity;
			case 9:
				return this.enemyHeading;
			case 10:
				return this.enemyEnergy;
			case 11:
				Random r1 = new Random();
				return r1.nextFloat() * 2 - 1;
			case 12:
				Random r2 = new Random();
				return r2.nextFloat() * 2 - 1;
			case 13:
				return 0;
			default:
				return 1;
			}
		} else {
			return runFunc(node);
		}
	}

	public Node getMoveTank() {
		return moveTank;
	}

	public void setMoveTank(Node moveTank) {
		this.moveTank = moveTank;
	}

	public Node getTurnTankRight() {
		return turnTankRight;
	}

	public void setTurnTankRight(Node turnTankRight) {
		this.turnTankRight = turnTankRight;
	}

	public Node getTurnGunRight() {
		return turnGunRight;
	}

	public void setTurnGunRight(Node turnGunRight) {
		this.turnGunRight = turnGunRight;
	}
}
