package pokemonAj;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class SaveManager {
	private static final String SAVE_FILE = "pokemon_save.dat";

	public static void saveGame(List<Pokemon> playerParty) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
			oos.writeObject(playerParty);
			System.out.println("게임이 저장되었습니다. (" + SAVE_FILE + ")");
		} catch (IOException e) {
			System.out.println("저장 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Pokemon> loadGame() {
		File file = new File(SAVE_FILE);
		if (!file.exists()) {
			System.out.println("저장 파일이 없습니다.");
			return new ArrayList<>();
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
			List<Pokemon> loadedParty = (List<Pokemon>) ois.readObject();
			System.out.println("게임을 불러왔습니다. (" + SAVE_FILE + ")");
			return loadedParty;
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("불러오기 중 오류가 발생했습니다: " + e.getMessage());
			return new ArrayList<>();
		}
	}
}
