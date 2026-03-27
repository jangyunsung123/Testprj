package swing_version;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class MapMain extends JFrame {

    private final Map map;
    private final MapPlayer player;
    private final JTextArea logArea;
    private final MapPanel mapPanel;
    private final Pokedex pokedex;
    private final BattleEngine battleEngine;

    private JLabel playerNameLabel;
    private JLabel firstPokemonLabel;

    public MapMain(MapPlayer player) {
        map = new Map();
        this.player = player;
        pokedex = GameDataManager.createDefaultPokedex();
        battleEngine = new BattleEngine();

        setTitle("포켓몬 게임");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mapPanel = new MapPanel(map, this.player);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(1200, 220));
        scrollPane.setBorder(BorderFactory.createTitledBorder("로그"));

        battleEngine.setParentComponent(this);
        battleEngine.setLogArea(logArea);

        add(mapPanel, BorderLayout.CENTER);
        add(createRightPanel(), BorderLayout.EAST);
        add(scrollPane, BorderLayout.SOUTH);

        initKeyBindings();
        setVisible(true);

        updatePlayerInfo();

        appendLog("포켓몬스터의 세계에 온 걸 환영한다!");
        appendLog("플레이어 이름: " + this.player.getName());
        appendLog("스타팅 포켓몬: " + this.player.getFirstPokemonName());
        appendLog("W / A / S / D 로 이동하세요.");
        appendLog("현재 위치: " + map.grid[this.player.y][this.player.x].getName());

        restoreFocus();
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(260, 800));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("메뉴");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 26));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        playerNameLabel = new JLabel();
        playerNameLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        playerNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        firstPokemonLabel = new JLabel();
        firstPokemonLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        firstPokemonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(titleLabel);
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(playerNameLabel);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(firstPokemonLabel);
        rightPanel.add(Box.createVerticalStrut(30));

        JButton btn1 = createMenuButton("1. 포켓몬 도감 보기", e -> {
            appendLog(pokedex.getAllPokemonText());
            restoreFocus();
        });

        JButton btn2 = createMenuButton("2. 포켓몬 상세 보기", e -> {
            String name = JOptionPane.showInputDialog(this, "상세 정보를 볼 포켓몬 이름 입력");
            if (name != null && !name.trim().isEmpty()) {
                appendLog(pokedex.getPokemonDetailText(name.trim()));
            }
            restoreFocus();
        });

        JButton btn3 = createMenuButton("3. 내 파티 보기", e -> {
            appendLog("==== 내 파티 ====");
            appendLog(player.getPartySummaryText());
            restoreFocus();
        });

        JButton btn4 = createMenuButton("4. 파티 회복", e -> {
            if (player.isPartyEmpty()) {
                appendLog("회복할 포켓몬이 없습니다.");
            } else {
                player.healAll();
                appendLog("파티의 모든 포켓몬이 회복되었습니다.");
                updatePlayerInfo();
            }
            restoreFocus();
        });

        JButton btn5 = createMenuButton("5. 게임 저장", e -> {
            appendLog(SaveManager.saveGameMessage(player.getParty()));
            restoreFocus();
        });

        JButton btn6 = createMenuButton("6. 게임 불러오기", e -> {
            SaveManager.LoadResult result = SaveManager.loadGameWithMessage();
            player.setParty(result.party);
            appendLog(result.message);
            if (!player.isPartyEmpty()) {
                appendLog("현재 첫 번째 포켓몬: " + player.getFirstPokemonName());
            }
            updatePlayerInfo();
            mapPanel.repaint();
            restoreFocus();
        });

        JButton btn0 = createMenuButton("0. 게임 종료", e -> {
            int result = JOptionPane.showConfirmDialog(this, "게임을 종료할까요?", "게임 종료", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
            restoreFocus();
        });

        rightPanel.add(btn1);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btn2);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btn3);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btn4);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btn5);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btn6);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btn0);
        rightPanel.add(Box.createVerticalStrut(20));

        JLabel guide = new JLabel("<html><center>이동 키<br>W / A / S / D</center></html>", SwingConstants.CENTER);
        guide.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(guide);

        return rightPanel;
    }

    private void updatePlayerInfo() {
        playerNameLabel.setText("플레이어: " + player.getName());
        firstPokemonLabel.setText("파티 첫 포켓몬: " + player.getFirstPokemonName());
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.addActionListener(listener);
        return button;
    }

    private void initKeyBindings() {
        InputMap inputMap = mapPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = mapPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("W"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");

        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                moveAndHandle('W');
            }
        });
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                moveAndHandle('A');
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                moveAndHandle('S');
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                moveAndHandle('D');
            }
        });
    }

    private void moveAndHandle(char input) {
        map.move(player, input);
        mapPanel.repaint();
        appendLog("현재 위치: " + map.grid[player.y][player.x].getName());

        char tile = map.getTile(player.x, player.y);
        if (tile == 'C') {
            visitCenter();
        } else if (tile == 'G') {
            visitGym();
        } else if (tile == 'T') {
            visitTown();
        } else if (tile == 'F' && Math.random() < 0.4) {
            encounterWildPokemon();
        }

        restoreFocus();
    }

    private void visitCenter() {
        appendLog("간호사 조이: \"안녕하세요! 포켓몬센터입니다.\"");
        appendLog("간호사 조이: \"당신의 포켓몬을 쉬게 해 주겠습니까?\"");

        int choice = JOptionPane.showConfirmDialog(this, "포켓몬을 회복하시겠습니까?", "포켓몬센터", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            appendLog("간호사 조이: \"잠시 포켓몬을 맡겨주시겠어요?\"");
            appendLog("간호사 조이: \"네, 맡아드리겠습니다!\"");
            player.healAll();
            appendLog("간호사 조이: \"오래 기다리셨습니다!\"");
            appendLog("간호사 조이: \"맡겨 두신 포켓몬이 모두 건강해졌습니다!\"");
            appendLog("간호사 조이: \"또 이용해 주세요!\"");
            updatePlayerInfo();
        }
    }

    private void visitGym() {
        appendLog("체육관 도착!");
        appendLog("안녕하세요! 미래의 챔피언! 체육관에 도전해주셔서 감사합니다!");
        appendLog("오호... " + player.getFirstPokemonName() + "와(과) 함께 왔군요.");
        appendLog(player.getFirstPokemonName() + "의 힘을 시험해보겠습니다!");
        appendLog("체육관 관장이 승부를 걸어왔다!");

        int choice = JOptionPane.showConfirmDialog(this, "싸우시겠습니까?", "체육관", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        appendLog("전설의 포켓몬이 나타났다!");
        int battleChoice = JOptionPane.showConfirmDialog(this, "계속 싸우시겠습니까?", "체육관 배틀", JOptionPane.YES_NO_OPTION);

        if (battleChoice == JOptionPane.YES_OPTION) {
            appendLog("전투를 시작합니다!");
            Pokemon legendaryPokemon = pokedex.getPokemon("기라티나");
            if (legendaryPokemon != null) {
                boolean won = battleEngine.startBattle(player.getParty(), legendaryPokemon.copy());
                if (won) appendLog("체육관에서 승리했습니다!");
                else handleAllFainted();
            }
        } else {
            if (new Random().nextInt(100) < 2) {
                appendLog("안돼! 트레이너 배틀 중에 상대에게 등을 보일 수는 없다!");
                appendLog("체육관 관장이 당신을 막아섰다!");
            } else {
                appendLog("무사히 도망쳤습니다!");
            }
        }
    }

    private void visitTown() {
        appendLog("oo마을에 도착했습니다!");
    }

    private void encounterWildPokemon() {
        List<Pokemon> wildList = pokedex.getByRarity("Wild");
        if (wildList.isEmpty()) {
            appendLog("포획 가능한 야생 포켓몬이 없습니다.");
            return;
        }
        if (player.isPartyEmpty()) {
            appendLog("파티가 비어 있어 전투를 할 수 없습니다.");
            return;
        }

        Pokemon wildPokemon = wildList.get((int) (Math.random() * wildList.size())).copy();
        boolean won = battleEngine.startBattle(player.getParty(), wildPokemon);

        if (won) {
            int capture = JOptionPane.showConfirmDialog(this, wildPokemon.getName() + "을(를) 포획할까요?", "포획 시도", JOptionPane.YES_NO_OPTION);
            if (capture == JOptionPane.YES_OPTION) {
                if (Math.random() < 0.5) {
                    wildPokemon.healFull();
                    if (player.addPokemon(wildPokemon)) {
                        appendLog(wildPokemon.getName() + " 포획 성공! 파티에 추가되었습니다.");
                        updatePlayerInfo();
                    } else {
                        appendLog("파티가 가득 찼습니다! (최대 6마리)");
                    }
                } else {
                    appendLog("아깝다! 조금만 더 하면 잡을 수 있었는데!");
                }
            } else {
                appendLog("포획하지 않고 지나갔습니다.");
            }
        } else {
            handleAllFainted();
        }

        mapPanel.repaint();
    }

    private void handleAllFainted() {
        if (!player.isAllFainted()) return;

        appendLog("눈앞이 캄캄해졌다...");
        appendLog("가까운 포켓몬센터로 이동합니다...");
        player.moveToCenter();
        player.healAll();
        appendLog("포켓몬이 모두 회복되었습니다!");
        updatePlayerInfo();
        mapPanel.repaint();
    }

    private void restoreFocus() {
        SwingUtilities.invokeLater(() -> mapPanel.requestFocusInWindow());
    }

    private void appendLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}