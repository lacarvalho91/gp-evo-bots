package uk.ac.reading.yz009013.evobots.tree.serializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import uk.ac.reading.yz009013.evobots.tree.Node;

public class NodeSerializer {
	public static void serialize(Node node) {
		File file = new File("src/uk/ac/reading/yz009013/evobots/"
				+ node.getActuator().getName() + ".ser");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream fout = new FileOutputStream(
					"src/uk/ac/reading/yz009013/evobots/"
							+ node.getActuator().getName() + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(node);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void serializeBest(Node node, int evoType) {
		String append;
		if (evoType == 0) {
			append = "Elite";
		} else {
			append = "NonElite";
		}
		File file = new File("src/uk/ac/reading/yz009013/evobots/Best"
				+ node.getActuator().getName() + append + ".ser");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream fout = new FileOutputStream(
					"src/uk/ac/reading/yz009013/evobots/Best"
							+ node.getActuator().getName() + append + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(node);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
