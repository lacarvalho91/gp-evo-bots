package uk.ac.reading.yz009013.evobots.tree.serializer;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import uk.ac.reading.yz009013.evobots.tree.Actuator;
import uk.ac.reading.yz009013.evobots.tree.Node;

public class NodeDeserializer {

	private final static String ROBOT_NODES_DIR = "C:/robocode/robots/LawrenceCarvalhoGPNodes/";

	public static Node deserialize(Actuator actuator, String robotName) {
		try {
			FileInputStream fin = new FileInputStream(
					"src/uk/ac/reading/yz009013/evobots/" + actuator.getName()
							+ robotName + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			Node node = (Node) ois.readObject();
			ois.close();
			return node;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Node deserializeBestNode(Actuator actuator, String robotName) {
		try {
			FileInputStream fin = new FileInputStream(ROBOT_NODES_DIR + "Best"
					+ actuator.getName() + robotName + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			Node node = (Node) ois.readObject();
			ois.close();
			return node;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
