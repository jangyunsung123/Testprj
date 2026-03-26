package pokemonAj;

import java.util.*;

public class GameMain {

	static Scanner sc = new Scanner(System.in);
	static ArrayList<Pokemon> party = new ArrayList<>();
	static Pokedex pokedex = GameDataManager.createDefaultPokedex();
	static BattleEngine battleEngine = new BattleEngine();
	static Map map = new Map();
	static MapPlayer player = new MapPlayer();

	public static void main(String[] args) {
		System.out.println("╔══════════════════════════════════╗");
		System.out.println("      포켓몬스터의 세계에 온 걸 환영한다!");
		System.out.println("╚══════════════════════════════════╝");
		System.out.println("조작법: W/A/S/D 이동  |  M 메뉴  |  Q 종료\n");

		while (true) {
			for (int i = 0; i < 20; i++) System.out.println();

			map.printMap(player.x, player.y);
			printTileHint(map.getTile(player.x, player.y));
			System.out.print("입력 >> ");

			String line = sc.nextLine().trim().toUpperCase();
			if (line.isEmpty()) continue;
			char input = line.charAt(0);

			if (input == 'Q') {
				System.out.println("게임을 종료합니다. 또 만나요!");
				break;
			}

			if (input == 'M') {
				showMenu();
				continue;
			}

			if ("WASD".indexOf(input) >= 0) {
				map.move(player, input);
				handleTileEvent(map.getTile(player.x, player.y));
			}
		}
		sc.close();
	}

	// ──────────── 타일 힌트 ────────────
	private static void printTileHint(char tile) {
		System.out.println("[ W/A/S/D: 이동  M: 메뉴  Q: 종료 ]");
		if (tile == 'C')      System.out.println(">>> 포켓몬센터 앞입니다. 이동하면 진입합니다.");
		else if (tile == 'G') System.out.println(">>> 체육관 앞입니다. 이동하면 진입합니다.");
		else if (tile == 'T') System.out.println(">>> 마을입니다. M을 눌러 메뉴를 열어보세요!");
		else                  System.out.println(">>> 풀숲입니다. 야생 포켓몬이 나타날 수 있습니다!");
	}

	// ──────────── 타일 이벤트 ────────────
	private static void handleTileEvent(char tile) {
		switch (tile) {
		case 'C': enterCenter();   break;
		case 'G': enterGym();      break;
		case 'T':
			System.out.println("\n마을에 도착했습니다! M을 눌러 메뉴를 열어보세요.");
			break;
		case 'F':
			if (Math.random() < 0.4) wildEncounter();
			break;
		}
	}

	// ──────────── 메뉴 ────────────
	private static void showMenu() {
		while (true) {
			System.out.println("\n======== 메인 메뉴 ========");
			System.out.println("1. 포켓몬 도감 보기");
			System.out.println("2. 포켓몬 상세 보기");
			System.out.println("3. 내 파티 보기");
			System.out.println("4. 게임 저장");
			System.out.println("5. 게임 불러오기");
			System.out.println("0. 닫기");
			System.out.print("선택 >> ");
			switch (sc.nextLine().trim()) {
			case "1": pokedex.displayAll(); break;
			case "2":
				System.out.print("포켓몬 이름 입력 >> ");
				pokedex.displayOne(sc.nextLine().trim());
				break;
			case "3": showParty(); break;
			case "4":
				SaveManager.saveGame(party);
				break;
			case "5":
				List<Pokemon> loaded = SaveManager.loadGame();
				party.clear();
				party.addAll(loaded);
				break;
			case "0": return;
			default: System.out.println("올바른 번호를 입력하세요.");
			}
		}
	}

	// ──────────── 파티 보기 ────────────
	private static void showParty() {
		System.out.println("\n==== 내 파티 ====");
		if (party.isEmpty()) {
			System.out.println("파티가 비어 있습니다. 야생 포켓몬을 포획해보세요!");
			return;
		}
		for (int i = 0; i < party.size(); i++) {
			Pokemon p = party.get(i);
			String status = p.isFainted() ? " [기절]" : "";
			System.out.printf("%d. %-8s | 타입: %-8s | Lv.%d | HP %d/%d%s%n",
				i + 1, p.getName(), p.getType(), p.getLevel(),
				p.getHp(), p.getMaxHp(), status);
		}
	}

	// ──────────── 포켓몬센터 ────────────
	private static void enterCenter() {
		System.out.println("\n간호사 조이: \"안녕하세요! 포켓몬센터입니다.\"");
		System.out.println("간호사 조이: \"포켓몬을 쉬게 해 드릴까요?\"");
		System.out.println("1. 예   2. 아니요");
		System.out.print("선택 >> ");
		if (sc.nextLine().trim().equals("1")) {
			try { Thread.sleep(1000); } catch (Exception e) {}
			for (Pokemon p : party) p.healFull();
			System.out.println("간호사 조이: \"맡겨 두신 포켓몬이 모두 건강해졌습니다!\"");
			System.out.println("간호사 조이: \"또 이용해 주세요!\"\n");
		}
	}

	// ──────────── 체육관 ────────────
	private static void enterGym() {
		if (party.isEmpty()) {
			System.out.println("\n포켓몬 없이는 체육관에 도전할 수 없습니다!");
			return;
		}
		String firstName = party.get(0).getName();
		System.out.println("\n체육관 관장: \"오호... " + firstName + "와(과) 함께 왔군요.\"");
		System.out.println("체육관 관장: \"실력을 시험해보겠습니다! 승부!\"");
		System.out.println("1. 도전한다!   2. 돌아간다");
		System.out.print("선택 >> ");
		if (!sc.nextLine().trim().equals("1")) return;

		Pokemon legendary = pokedex.getPokemon("기라티나");
		if (legendary == null) {
			System.out.println("오류: 체육관 포켓몬을 불러올 수 없습니다.");
			return;
		}
		boolean won = battleEngine.startBattle(party, legendary.copy());
		if (won) {
			System.out.println("🏆 체육관을 제패했습니다!");
		} else {
			checkAllFainted();
		}
	}

	// ──────────── 야생 포켓몬 조우 ────────────
	private static void wildEncounter() {
		List<Pokemon> wildList = pokedex.getByRarity("Wild");
		if (wildList.isEmpty()) return;

		Pokemon template = wildList.get((int) (Math.random() * wildList.size()));
		Pokemon wild = template.copy();

		boolean won = battleEngine.startBattle(party, wild);

		if (won) {
			// 전투 승리 후 포획 시도
			System.out.println("1. 몬스터볼 던지기   2. 그냥 간다");
			System.out.print("선택 >> ");
			if (sc.nextLine().trim().equals("1")) {
				if (Math.random() < 0.5) {
					if (party.size() < 6) {
						wild.healFull(); // 포획한 포켓몬은 체력 회복 후 추가
						party.add(wild);
						System.out.println(wild.getName() + " 포획 성공! 파티에 추가되었습니다.");
					} else {
						System.out.println("파티가 가득 찼습니다! (최대 6마리)");
					}
				} else {
					System.out.println("아깝다! 조금만 더 하면 잡을 수 있었는데!");
				}
			}
		} else {
			checkAllFainted();
		}
	}

	// ──────────── 전멸 체크 ────────────
	private static void checkAllFainted() {
		boolean allFainted = party.stream().allMatch(Pokemon::isFainted);
		if (allFainted && !party.isEmpty()) {
			System.out.println("\n눈앞이 캄캄해졌다...");
			System.out.println("가까운 포켓몬센터로 이동합니다...");
			try { Thread.sleep(1500); } catch (Exception e) {}
			player.x = 3; // 포켓몬센터 위치
			player.y = 3;
			for (Pokemon p : party) p.healFull();
			System.out.println("포켓몬이 모두 회복되었습니다!\n");
		}
	}
}
