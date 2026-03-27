package swing_version;

import javax.swing.*;

public class GameMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"게임 시작", "게임 종료"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "포켓몬 게임을 시작할까요?",
                    "포켓몬 게임 시작 메뉴",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) new MapMain();
            else System.exit(0);
        });
    }
}
