package pokemonAj;

import javax.swing.*;
import java.util.Scanner;

public class GameMain {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("==================================");
			System.out.println("        포켓몬 게임 시작 메뉴");
			System.out.println("==================================");
			System.out.println("1. 게임 시작");
			System.out.println("0. 게임 종료");
			System.out.print("선택 >> ");

			String choice = sc.nextLine().trim();

			if ("1".equals(choice)) {
				SwingUtilities.invokeLater(() -> {
					PokemonGameFrame frame = new PokemonGameFrame();
					frame.setVisible(true);
				});
				break;
			} else if ("0".equals(choice)) {
				System.out.println("게임을 종료합니다.");
				break;
			} else {
				System.out.println("올바른 번호를 입력하세요.");
			}
		}
	}
}