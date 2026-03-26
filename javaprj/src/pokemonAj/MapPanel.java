package pokemonAj;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {

    private final Map map;
    private final MapPlayer player;

    public MapPanel(Map map, MapPlayer player) {
        this.map = map;
        this.player = player;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int rows = map.size;
        int cols = map.size;

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        g.setFont(new Font("Dialog", Font.PLAIN, 14));

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                int drawX = x * cellWidth;
                int drawY = y * cellHeight;

                char tile = map.grid[y][x].getType();

                if (tile == 'F') {
                    g.setColor(new Color(200, 255, 200));
                } else if (tile == 'T') {
                    g.setColor(new Color(255, 245, 200));
                } else if (tile == 'C') {
                    g.setColor(new Color(255, 220, 220));
                } else if (tile == 'G') {
                    g.setColor(new Color(220, 235, 255));
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }

                g.fillRect(drawX, drawY, cellWidth, cellHeight);
                g.setColor(Color.WHITE);
                g.drawRect(drawX, drawY, cellWidth, cellHeight);

                String text = "";
                if (tile == 'F') text = "🌳";
                else if (tile == 'T') text = "🏠";
                else if (tile == 'C') text = "🏥";
                else if (tile == 'G') text = "🏟";

                g.drawString(text, drawX + 10, drawY + 20);

                if (player.x == x && player.y == y) {
                    g.setColor(Color.RED);
                    g.fillOval(drawX + cellWidth / 2 - 10, drawY + cellHeight / 2 - 10, 20, 20);
                    g.setColor(Color.WHITE);
                    g.drawString(" ", drawX + cellWidth / 2 - 5, drawY + cellHeight / 2 + 5);
                }
            }
        }
    }
}