package pokemonAj;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonGameFrame extends JFrame {

    private final String playerName = "지우";

    private final ArrayList<Pokemon> party = new ArrayList<>();
    private final Pokedex pokedex = GameDataManager.createDefaultPokedex();
    private final BattleEngine battleEngine = new BattleEngine();
    private final Map map = new Map();
    private final MapPlayer player = new MapPlayer();

    private final JTextArea logArea = new JTextArea();
    private final MapPanel mapPanel = new MapPanel(map, player);

    public PokemonGameFrame() {
        setTitle("포켓몬 게임");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initGameData();
        initUI();
        initKeyBindings();

        appendLog("포켓몬스터의 세계에 온 걸 환영한다!");
        appendLog("플레이어 이름: " + playerName);
        appendLog("스타팅 포켓몬: " + party.get(0).getName());
        appendLog("조작법: W / A / S / D 이동");
        appendLog("오른쪽 메뉴 버튼으로 도감, 파티, 저장 등을 사용할 수 있다.");
    }

    private void initGameData() {
        Pokemon starter = pokedex.getPokemon("피카츄");
        if (starter != null) {
            party.add(starter.copy());
        }

        battleEngine.setLogger(this::appendLog);
        battleEngine.setParentComponent(this);
    }

    private void initUI() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(840, 800));

        mapPanel.setPreferredSize(new Dimension(840, 560));
        leftPanel.add(mapPanel, BorderLayout.CENTER);

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(840, 240));
        logScroll.setBorder(BorderFactory.createTitledBorder("로그 기록"));
        leftPanel.add(logScroll, BorderLayout.SOUTH);

        JPanel menuPanel = createMenuPanel();
        menuPanel.setPreferredSize(new Dimension(360, 800));

        add(leftPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.EAST);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("메뉴", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 22));
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel info = new JLabel("<html>플레이어: 지우<br>파티 첫 포켓몬: " + party.get(0).getName() + "</html>");
        info.setAlignmentX(CENTER_ALIGNMENT);

        JButton btn1 = createMenuButton("1. 포켓몬 도감 보기", this::showPokedexAll);
        JButton btn2 = createMenuButton("2. 포켓몬 상세 보기", this::showPokedexOne);
        JButton btn3 = createMenuButton("3. 내 파티 보기", this::showParty);
        JButton btn4 = createMenuButton("4. 파티 회복", this::healParty);
        JButton btn5 = createMenuButton("5. 게임 저장", this::saveGame);
        JButton btn6 = createMenuButton("6. 게임 불러오기", this::loadGame);
        JButton btn0 = createMenuButton("0. 게임 종료", this::exitGame);

        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(15));
        menuPanel.add(info);
        menuPanel.add(Box.createVerticalStrut(25));

        menuPanel.add(btn1);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btn2);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btn3);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btn4);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btn5);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btn6);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btn0);

        menuPanel.add(Box.createVerticalGlue());

        JLabel moveGuide = new JLabel("<html><center>이동 키<br>W / A / S / D</center></html>", SwingConstants.CENTER);
        moveGuide.setAlignmentX(CENTER_ALIGNMENT);
        menuPanel.add(moveGuide);

        return menuPanel;
    }

    private JButton createMenuButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.addActionListener(e -> action.run());
        return button;
    }

    private void initKeyBindings() {
        InputMap inputMap = mapPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = mapPanel.getActionMap();

        bindMove(inputMap, actionMap, "W", 'W');
        bindMove(inputMap, actionMap, "A", 'A');
        bindMove(inputMap, actionMap, "S", 'S');
        bindMove(inputMap, actionMap, "D", 'D');
    }

    private void bindMove(InputMap inputMap, ActionMap actionMap, String key, char moveKey) {
        inputMap.put(KeyStroke.getKeyStroke(key), "move_" + key);
        actionMap.put("move_" + key, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                movePlayer(moveKey);
            }
        });
    }

    private void movePlayer(char input) {
        map.move(player, input);
        mapPanel.repaint();
        handleTileEvent(map.getTile(player.x, player.y));
    }

    private void handleTileEvent(char tile) {
        switch (tile) {
            case 'C':
                enterCenter();
                break;
            case 'G':
                enterGym();
                break;
            case 'T':
                appendLog("마을에 도착했습니다.");
                break;
            case 'F':
                if (Math.random() < 0.4) {
                    wildEncounter();
                }
                break;
        }
        mapPanel.repaint();
    }

    private void showPokedexAll() {
        appendLog("==== 포켓몬 도감 ====");
        List<Pokemon> all = pokedex.getAllPokemon();

        for (int i = 0; i < all.size(); i++) {
            Pokemon p = all.get(i);
            appendLog((i + 1) + ". " + p.getName()
                    + " | 타입: " + p.getType()
                    + " | Lv." + p.getLevel()
                    + " | HP " + p.getMaxHp()
                    + " | ATK " + p.getAttack()
                    + " | 등급: " + p.getRarity());
        }
    }

    private void showPokedexOne() {
        String name = JOptionPane.showInputDialog(this, "상세 정보를 볼 포켓몬 이름 입력");
        if (name == null || name.trim().isEmpty()) {
            return;
        }

        Pokemon p = pokedex.getPokemon(name.trim());
        if (p == null) {
            appendLog("해당 포켓몬이 존재하지 않습니다.");
            return;
        }

        appendLog("==== " + p.getName() + " 상세 정보 ====");
        appendLog("타입: " + p.getType());
        appendLog("레벨: " + p.getLevel());
        appendLog("HP: " + p.getCurrentHp() + "/" + p.getMaxHp());
        appendLog("ATK: " + p.getAttack());
        appendLog("등급: " + p.getRarity());
        appendLog("스킬 목록:");
        for (Skill s : p.getSkills()) {
            appendLog(" - " + s.getName() + " (" + s.getType() + ") | 위력: " + s.getPower() + " | 효과: " + s.getEffect());
        }
    }

    private void showParty() {
        appendLog("==== 내 파티 ====");
        if (party.isEmpty()) {
            appendLog("현재 파티가 비어 있습니다.");
            return;
        }

        for (int i = 0; i < party.size(); i++) {
            Pokemon p = party.get(i);
            String faint = p.isFainted() ? " [기절]" : "";
            appendLog((i + 1) + ". " + p.getName()
                    + " | 타입: " + p.getType()
                    + " | Lv." + p.getLevel()
                    + " | HP " + p.getHp() + "/" + p.getMaxHp()
                    + faint);
        }
    }

    private void healParty() {
        if (party.isEmpty()) {
            appendLog("회복할 포켓몬이 없습니다.");
            return;
        }

        for (Pokemon p : party) {
            p.healFull();
        }
        appendLog("파티의 모든 포켓몬이 회복되었습니다.");
    }

    private void saveGame() {
        String msg = SaveManager.saveGameMessage(party);
        appendLog(msg);
    }

    private void loadGame() {
        SaveManager.LoadResult result = SaveManager.loadGameWithMessage();
        party.clear();
        party.addAll(result.party);
        appendLog(result.message);

        if (party.isEmpty()) {
            appendLog("불러온 파티가 비어 있습니다.");
        } else {
            appendLog("현재 첫 번째 포켓몬: " + party.get(0).getName());
        }
        mapPanel.repaint();
    }

    private void exitGame() {
        int result = JOptionPane.showConfirmDialog(this, "게임을 종료할까요?", "게임 종료", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    private void enterCenter() {
        appendLog("포켓몬센터에 들어갔습니다.");
        appendLog("간호사 조이: \"안녕하세요! 포켓몬센터입니다.\"");

        int choice = JOptionPane.showConfirmDialog(
                this,
                "포켓몬을 회복할까요?",
                "포켓몬센터",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            for (Pokemon p : party) {
                p.healFull();
            }
            appendLog("간호사 조이: \"맡겨 두신 포켓몬이 모두 건강해졌습니다!\"");
            appendLog("간호사 조이: \"또 이용해 주세요!\"");
        } else {
            appendLog("회복을 취소했습니다.");
        }
    }

    private void enterGym() {
        if (party.isEmpty()) {
            appendLog("포켓몬 없이는 체육관에 도전할 수 없습니다!");
            return;
        }

        appendLog("체육관에 들어갔습니다.");
        appendLog("체육관 관장: \"오호... " + party.get(0).getName() + "와(과) 함께 왔군요.\"");
        appendLog("체육관 관장: \"실력을 시험해보겠습니다! 승부!\"");

        int choice = JOptionPane.showConfirmDialog(
                this,
                "체육관에 도전할까요?",
                "체육관",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            appendLog("체육관 도전을 취소했습니다.");
            return;
        }

        Pokemon legendary = pokedex.getPokemon("기라티나");
        if (legendary == null) {
            appendLog("오류: 체육관 포켓몬을 불러올 수 없습니다.");
            return;
        }

        boolean won = battleEngine.startBattle(party, legendary.copy());

        if (won) {
            appendLog("🏆 체육관을 제패했습니다!");
        } else {
            checkAllFainted();
        }

        mapPanel.repaint();
    }

    private void wildEncounter() {
        if (party.isEmpty()) {
            appendLog("파티가 비어 있어 전투를 할 수 없습니다.");
            return;
        }

        List<Pokemon> wildList = pokedex.getByRarity("Wild");
        if (wildList.isEmpty()) {
            return;
        }

        Pokemon wild = wildList.get((int) (Math.random() * wildList.size())).copy();
        boolean won = battleEngine.startBattle(party, wild);

        if (won) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    wild.getName() + "을(를) 포획할까요?",
                    "포획 시도",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                if (Math.random() < 0.5) {
                    if (party.size() < 6) {
                        wild.healFull();
                        party.add(wild);
                        appendLog(wild.getName() + " 포획 성공! 파티에 추가되었습니다.");
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
            checkAllFainted();
        }

        mapPanel.repaint();
    }

    private void checkAllFainted() {
        boolean allFainted = !party.isEmpty() && party.stream().allMatch(Pokemon::isFainted);

        if (allFainted) {
            appendLog("눈앞이 캄캄해졌다...");
            appendLog("가까운 포켓몬센터로 이동합니다...");

            player.x = 3;
            player.y = 3;

            for (Pokemon p : party) {
                p.healFull();
            }

            appendLog("포켓몬이 모두 회복되었습니다!");
            mapPanel.repaint();
        }
    }

    private void appendLog(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}