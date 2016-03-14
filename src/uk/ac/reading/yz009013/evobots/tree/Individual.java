package uk.ac.reading.yz009013.evobots.tree;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.reading.yz009013.evobots.BattleRunner;
import uk.ac.reading.yz009013.evobots.tree.serializer.NodeSerializer;

public class Individual {
	private double fitness;
	private Node moveTank;
	private Node turnTankRight;
	private Node turnGunRight;
	private int rank;

	public Individual() {
	}

	public Individual(Node moveTank, Node turnTankRight, Node turnGunRight) {
		this.moveTank = moveTank;
		this.turnTankRight = turnTankRight;
		this.turnGunRight = turnGunRight;
	}

	public void calcFitnessScore() {
		NodeSerializer.serialize(this.moveTank);
		NodeSerializer.serialize(this.turnTankRight);
		NodeSerializer.serialize(this.turnGunRight);

		BattleRunner runner = new BattleRunner();

		this.fitness = runner.runBattle();
	}

	/**
	 * crossover 2 parents by getting a random node for each and then swapping
	 * them and returning the 2 new children
	 * 
	 * @param parent2
	 * @return children of parents
	 */
	public ArrayList<Individual> crossover(Individual parent2) {

		Individual child1 = new Individual();
		Individual child2 = new Individual();

		child1.setMoveTank(this.moveTank);
		child1.setTurnGunRight(this.turnGunRight);
		child1.setTurnTankRight(this.turnTankRight);

		child2.setMoveTank(parent2.moveTank);
		child2.setTurnGunRight(parent2.turnGunRight);
		child2.setTurnTankRight(parent2.turnTankRight);

		// pick random actuator/tree
		Actuator randomActuator = Actuator.getRandomActuator();

		Node node1 = child1.getRandomNode(randomActuator);
		Node node2 = child2.getRandomNode(randomActuator);

		child1.findAndReplaceNode(randomActuator, node2);
		child2.findAndReplaceNode(randomActuator, node1);

		ArrayList<Individual> children = new ArrayList<Individual>();

		children.add(child1);
		children.add(child2);

		return children;
	}

	public Individual mutate() {
		Individual child = new Individual();
		child.moveTank = this.moveTank;
		child.turnGunRight = this.turnGunRight;
		child.turnTankRight = this.turnTankRight;

		// pick random actuator/tree
		Actuator randomActuator = Actuator.getRandomActuator();
		Node randomNode = child.getRandomNode(randomActuator);
		child.findAndReplaceNode(randomActuator, new Node(
				randomNode.getDepth(), randomActuator));
		return child;
	}

	public void findAndReplaceNode(Actuator actuator, Node newNode) {
		Node parentNode;
		Node previousNode;
		Node currentNode;

		switch (actuator) {
		case MOVE_TANK:
			parentNode = this.moveTank;
			currentNode = this.moveTank;
			break;
		case TURN_TANK_RIGHT:
			parentNode = this.turnTankRight;
			currentNode = this.turnTankRight;
			break;
		case TURN_GUN_RIGHT:
			parentNode = this.turnGunRight;
			currentNode = this.turnGunRight;
			break;
		default:
			parentNode = this.moveTank;
			currentNode = this.moveTank;
			break;
		}

		// counter to identify position in tree
		int i = 0;
		boolean traverse = false;
		while (!traverse) {
			if (currentNode == null) {
				currentNode = parentNode;
			}
			try {
				if (currentNode.getLeftChild() != null) {
					if (currentNode.getLeftChild().isMarked()) {
						currentNode.setLeftChild(newNode);
						traverse = true;
					}
				}
				if (currentNode.getRightChild() != null) {
					if (currentNode.getRightChild().isMarked()) {
						currentNode.setRightChild(newNode);
						traverse = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
			previousNode = currentNode;
			// when i is an odd number, move to left child else move to right
			if (!(i % 2 == 0)) {
				currentNode = parentNode.getLeftChild();
			} else {
				currentNode = parentNode.getRightChild();
			}
			parentNode = previousNode;
		}
	}

	public Node getRandomNode(Actuator actuator) {
		Node rootNode;
		Node parentNode;
		Node previousNode;
		Node currentNode;
		switch (actuator) {
		case MOVE_TANK:
			rootNode = this.moveTank;
			parentNode = this.moveTank;
			currentNode = this.moveTank;
			break;
		case TURN_TANK_RIGHT:
			rootNode = this.turnTankRight;
			parentNode = this.turnTankRight;
			currentNode = this.turnTankRight;
			break;
		case TURN_GUN_RIGHT:
			rootNode = this.turnGunRight;
			parentNode = this.turnGunRight;
			currentNode = this.turnGunRight;
			break;
		default:
			rootNode = this.moveTank;
			parentNode = this.moveTank;
			currentNode = this.moveTank;
			break;
		}
		// counter to identify position in tree
		int i = 0;
		while (true) {
			// start again when reached end of tree
			if (currentNode == null) {
				currentNode = rootNode;
				parentNode = rootNode;
			}
			if (currentNode.getLeftChild() != null) {
				if (selectNode()) {
					currentNode.getLeftChild().setMarked(true);
					return currentNode.getLeftChild();
				}
			}
			if (currentNode.getRightChild() != null) {
				if (selectNode()) {
					currentNode.getRightChild().setMarked(true);
					return currentNode.getRightChild();
				}
			}
			i++;
			previousNode = currentNode;
			// when i is an odd number, move to left child else move to right
			if (!(i % 2 == 0)) {
				currentNode = parentNode.getLeftChild();
			} else {
				currentNode = parentNode.getRightChild();
			}
			parentNode = previousNode;
		}
	}

	public boolean selectNode() {
		boolean selectNode = false;
		Random r = new Random();
		if (r.nextDouble() <= ((double) 1 / Node.MAX_DEPTH)) {
			selectNode = true;
		}
		return selectNode;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
}
