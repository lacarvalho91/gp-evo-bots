package uk.ac.reading.yz009013.evobots.tree;

import java.io.Serializable;
import java.util.Random;

public class Node implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node leftChild;
	private Node rightChild;
	private int depth;
	private int expressionId;
	private boolean isTerminal = true;
	private Actuator actuator;
	private boolean marked = false;

	private final static double PROB_FUNCTION = 0.8;
	private final static double PROB_TERMINAL = 0.2;
	private final static int NUMBER_OF_TERMINALS = 15;
	private final static int NUMBER_OF_FUNCTIONS = 16;

	public final static int MAX_DEPTH = 2;

	// called for root node to force it to be a function
	public Node(int depth, Actuator actuator, boolean isTerminal) {
		this.depth = depth;
		this.actuator = actuator;
		this.isTerminal = isTerminal;
		this.assignExpression();
	}

	public Node(int depth, Actuator actuator) {
		this.depth = depth;
		this.setActuator(actuator);
		this.assignExpression();
	}

	public Node(Actuator actuator) {
		this.actuator = actuator;
	}

	private void assignExpression() {
		Random r = new Random();
		double operator = r.nextDouble();
		if (this.depth == MAX_DEPTH) {
			this.expressionId = r.nextInt(NUMBER_OF_TERMINALS);
		} else if (this.depth == 0 || (operator -= PROB_FUNCTION) <= 0) {
			this.isTerminal = false;
			this.expressionId = r.nextInt(NUMBER_OF_FUNCTIONS);
			this.leftChild = new Node(this.depth + 1, this.actuator);
			this.rightChild = new Node(this.depth + 1, this.actuator);
		} else if ((operator -= PROB_TERMINAL) <= 0) {
			this.expressionId = r.nextInt(NUMBER_OF_TERMINALS);
		}
	}

	public Node getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getExpressionId() {
		return expressionId;
	}

	public void setExpressionId(int expressionId) {
		this.expressionId = expressionId;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}

	public Actuator getActuator() {
		return actuator;
	}

	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}
}