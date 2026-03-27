package swing_version;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BT_Dialog {

	private static final int BOX_WIDTH = 520;

	private BT_Dialog() {
	}

	// 콘솔 출력이 꼭 필요하면 그냥 평문만 출력
	public static void show(String message) {
		System.out.println(message);
		System.out.println();
	}

	// 더 이상 문자열 박스 포맷은 안 씀
	public static String format(String message) {
		return message == null ? "" : message;
	}

	public static JPanel createMessageBox(String message) {
		JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		wrapper.setOpaque(false);
		wrapper.setBorder(new EmptyBorder(0, 0, 10, 0));
		wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel box = new JPanel(new BorderLayout());
		box.setBackground(Color.WHITE);
		box.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(80, 80, 80), 2),
				new EmptyBorder(12, 14, 12, 14)
		));

		JTextArea textArea = new JTextArea(message == null ? "" : message);
		textArea.setEditable(false);
		textArea.setOpaque(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Dialog", Font.PLAIN, 17));
		textArea.setFocusable(false);

		textArea.setSize(new Dimension(BOX_WIDTH - 32, Short.MAX_VALUE));
		Dimension textPref = textArea.getPreferredSize();

		box.add(textArea, BorderLayout.CENTER);

		JLabel arrow = new JLabel("▼", SwingConstants.RIGHT);
		arrow.setFont(new Font("Dialog", Font.PLAIN, 11));
		arrow.setForeground(new Color(130, 130, 130));
		arrow.setBorder(new EmptyBorder(6, 0, 0, 0));
		box.add(arrow, BorderLayout.SOUTH);

		int boxHeight = textPref.height + 55;
		Dimension fixedSize = new Dimension(BOX_WIDTH, boxHeight);

		box.setPreferredSize(fixedSize);
		box.setMinimumSize(fixedSize);
		box.setMaximumSize(fixedSize);

		wrapper.add(box);

		Dimension wrapperSize = new Dimension(BOX_WIDTH, boxHeight + 10);
		wrapper.setPreferredSize(wrapperSize);
		wrapper.setMinimumSize(wrapperSize);
		wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, boxHeight + 10));

		return wrapper;
	}
}