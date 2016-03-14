package uk.ac.reading.yz009013.evobots.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import org.apache.log4j.Logger;

import uk.ac.reading.yz009013.evobots.tree.serializer.NodeSerializer;

public class Population {

	private ArrayList<Individual> individuals = new ArrayList<Individual>();
	private final static double PROB_CROSSOVER = 0.95;
	private final static double PROB_MUTATION = 0.05;
	private final static int POP_SIZE = 30;
	private final static int TOURNAMENT_SIZE = 10;
	private final static int AMOUNT_FOR_CROSSOVER = 6;
	private Random random = new Random();
	private static double BEST_FITNESS = 0;

	private final static Logger eliteTankLog = Logger
			.getLogger("eliteTankLogger");
	private final static Logger nonEliteTankLog = Logger
			.getLogger("nonEliteTankLogger");

	// constructor to initialise population
	public Population() {
		for (int i = 0; i < POP_SIZE; i++) {
			Individual individual = new Individual(new Node(0,
					Actuator.MOVE_TANK, false), new Node(0,
					Actuator.TURN_TANK_RIGHT, false), new Node(0,
					Actuator.TURN_GUN_RIGHT, false));
			individuals.add(individual);
		}
	}

	/**
	 * Put the top 2 competitors from tournament straight into the next
	 * generation (elitism) Fill the population back up to its original size by
	 * either crossing over or mutating 2 parents (95% chance of crossover, 5%
	 * chance of mutation)
	 */
	public void eliteEvolve() {
		ArrayList<Individual> competitors = tournamentSelection(0);
		// ELITISM: Top 2 competitors put into next generation
		this.individuals.add(competitors.get(0));
		this.individuals.add(competitors.get(1));

		while (this.individuals.size() < POP_SIZE) {
			double geneticOporator = random.nextDouble();
			Individual parent1 = rouletteWheelSelection(competitors);
			Individual parent2 = rouletteWheelSelection(competitors);
			while (parent2.equals(parent1)) {
				parent2 = rouletteWheelSelection(competitors);
			}
			if ((geneticOporator -= PROB_CROSSOVER) <= 0) {
				this.individuals.addAll(parent1.crossover(parent2));
			} else if ((geneticOporator -= PROB_MUTATION) <= 0) {
				this.individuals.add(parent1.mutate());
				this.individuals.add(parent2.mutate());
			}
		}
	}

	/**
	 * Select 6 competitors and perform crossovers Mutate the remaining 4
	 * competitors Store the 10 parents and 10 children in a new list Select 10
	 * from that list to go into next generation
	 */
	public void nonEliteEvolve() {
		ArrayList<Individual> competitors = tournamentSelection(1);
		ArrayList<Individual> parents = competitors;
		ArrayList<Individual> forCrossover = new ArrayList<Individual>();
		forCrossover.addAll(competitors.subList(0, AMOUNT_FOR_CROSSOVER));
		ArrayList<Individual> children = new ArrayList<Individual>();

		int idx = 0;
		while (children.size() < AMOUNT_FOR_CROSSOVER) {
			children.addAll(forCrossover.get(idx).crossover(
					forCrossover.get(idx + 1)));
			idx += 2;
		}

		// mutate remaining parents
		for (Individual competitor : competitors.subList(AMOUNT_FOR_CROSSOVER,
				competitors.size())) {
			children.add(competitor.mutate());
		}

		ArrayList<Individual> forSelection = new ArrayList<Individual>();
		ArrayList<Individual> nextGen = new ArrayList<Individual>();
		forSelection.addAll(parents);

		// calculate fitness of new children
		for (Individual child : children) {
			child.calcFitnessScore();
		}

		forSelection.addAll(children);

		Collections.sort(forSelection, new Comparator<Individual>() {

			@Override
			public int compare(Individual o1, Individual o2) {
				return Double.compare(o1.getFitness(), o2.getFitness());
			}

		});

		int totalRank = 0;

		for (int i = forSelection.size() - 1; i > 0; i--) {
			forSelection.get(i).setRank(i);
			totalRank += i;
		}

		Collections.reverse(forSelection);

		// select 10 from list to be put into next generation using roulette
		// wheel selection
		Individual candidate;
		while (nextGen.size() < TOURNAMENT_SIZE) {
			candidate = rankSelection(forSelection, totalRank);
			while (nextGen.contains(candidate)) {
				candidate = rankSelection(forSelection, totalRank);
			}
			nextGen.add(candidate);
		}

		// add next generation to population
		this.individuals.addAll(nextGen);
	}

	/**
	 * algorithm to randomly select an individual using a higher probability for
	 * higher fitness individuals
	 * 
	 * Get total fitness, generate a random between 0 and total fitness, loop
	 * through individuals and sum up fitness until the sum is greater than the
	 * random
	 * 
	 * @param tournament
	 * @return random individual
	 */
	public Individual rouletteWheelSelection(ArrayList<Individual> individuals) {
		double totalFitness = 0;
		for (Individual individual : individuals) {
			totalFitness += individual.getFitness();
		}
		if (totalFitness == 0) {
			return individuals.get(random.nextInt(individuals.size()));
		}
		double randNum = random.nextDouble() * totalFitness;
		double sum = 0;
		for (int i = 0; i < individuals.size(); i++) {
			sum += individuals.get(i).getFitness();
			if (sum >= randNum) {
				return individuals.get(i);
			}
			if (i == individuals.size()) {
				return individuals.get(random.nextInt(individuals.size()));
			}
		}
		// shouldn't reach here
		return null;
	}

	public Individual rankSelection(ArrayList<Individual> individuals,
			int totalRank) {
		int rand = random.nextInt(totalRank);
		int sum = 0;
		for (Individual individual : individuals) {
			sum += individual.getRank();
			if (sum >= rand) {
				return individual;
			}
		}
		return null;
	}

	public ArrayList<Individual> tournamentSelection(int evoType) {
		double averageFitness = 0;
		int totalFitness = 0;
		ArrayList<Individual> competitors = new ArrayList<Individual>();

		for (int i = 0; i < TOURNAMENT_SIZE; i++) {
			int k = random.nextInt(POP_SIZE);
			competitors.add(this.individuals.get(k));
		}

		this.individuals.removeAll(competitors);

		for (Individual competitor : competitors) {
			competitor.calcFitnessScore();
			if (competitor.getFitness() > BEST_FITNESS) {
				BEST_FITNESS = competitor.getFitness();
				NodeSerializer.serializeBest(competitor.getMoveTank(), evoType);
				NodeSerializer.serializeBest(competitor.getTurnGunRight(),
						evoType);
				NodeSerializer.serializeBest(competitor.getTurnTankRight(),
						evoType);
			}
			totalFitness += competitor.getFitness();
		}
		averageFitness = (double) (totalFitness / competitors.size());
		Collections.sort(competitors, new Comparator<Individual>() {

			@Override
			public int compare(Individual o1, Individual o2) {
				return Double.compare(o1.getFitness(), o2.getFitness());
			}

		});
		Collections.reverse(competitors);

		System.out.println("-----Average fitness: " + averageFitness + "-----");

		if (evoType == 0) {
			eliteTankLog.info("Average fitness = " + averageFitness);

		} else {
			nonEliteTankLog.info("Average fitness = " + averageFitness);
		}
		return competitors;
	}
}
