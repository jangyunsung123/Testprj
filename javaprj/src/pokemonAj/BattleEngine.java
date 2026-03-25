package pokemonAj;

import java.util.ArrayList;
import java.util.Scanner;

public class BattleEngine {

	private Scanner scanner = new Scanner(System.in);

	public boolean startBattle(ArrayList<Pokemon> playerParty, Pokemon wildPokemon) {
		System.out.println("\n╔═══════════════════════════════╗");
		System.out.println("야생의 " + wildPokemon.getName() + "이(가) 나타났다!");
		System.out.println("╚═══════════════════════════════╝\n");

		Pokemon mine = getFirstAlive(playerParty);
		if (mine == null) {
			System.out.println("싸울 수 있는 포켓몬이 없다!");
			return false;
		}

		System.out.println("가라! " + mine.getName() + "!\n");

		while (true) {
			printStatus(mine, wildPokemon);
			int action = chooseAction();

			if (action == 1) { // 싸운다
				Move move = chooseMove(mine);
				if (move == null) continue;

				if (!mine.getStatusEffect().canAct(mine)) {
					endOfTurn(mine, wildPokemon);
					if (wildPokemon.isFainted()) { printWin(wildPokemon); return true; }
					mine = checkMyFainted(mine, playerParty);
					if (mine == null) return false;
					continue;
				}

				doAttack(mine, move, wildPokemon);
				if (wildPokemon.isFainted()) { printWin(wildPokemon); return true; }

				if (wildPokemon.getStatusEffect().canAct(wildPokemon)) {
					Move wildMove = getRandomMove(wildPokemon);
					if (wildMove != null) doAttack(wildPokemon, wildMove, mine);
				}

				endOfTurn(mine, wildPokemon);
				if (wildPokemon.isFainted()) { printWin(wildPokemon); return true; }

				mine = checkMyFainted(mine, playerParty);
				if (mine == null) return false;

			} else if (action == 2) { // 포켓몬교체
				Pokemon next = switchPokemon(playerParty, mine);
				if (next != null) {
					System.out.println("수고했어, " + mine.getName() + "! 들어와!");
					mine = next;
					System.out.println("가라! " + mine.getName() + "!\n");
				}

			} else if (action == 3) { // 도망간다
				System.out.println("도망쳤다!");
				return false;

			} else {
				System.out.println("1~3 중에서 입력하세요.");
			}
		}
	}

	private Pokemon switchPokemon(ArrayList<Pokemon> party, Pokemon current) {
		System.out.println("\n╔═══════════════════════════════════╗");
		System.out.println("포켓몬을 선택하세요:");
		for (int i = 0; i < party.size(); i++) {
			Pokemon p = party.get(i);
			if (p == current) { // 문자열 
				System.out.printf("  [%d] %-8s HP: %3d / %3d  (출전 중)%n", i + 1, p.getName(), p.getHp(), p.getMaxHp());
			} else if (p.isFainted()) {
				System.out.printf("  [%d] %-8s [기절]%n", i + 1, p.getName());
			} else {
				System.out.printf("  [%d] %-8s HP: %3d / %3d%n", i + 1, p.getName(), p.getHp(), p.getMaxHp());
			}
		}
		System.out.println("╚════════════════════════════════════╝\\n");

		while (true) {
			try {
				int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
				if (idx < 0 || idx >= party.size()) {
					System.out.println("올바른 번호를 입력하세요.");
					continue;
				}
				Pokemon selected = party.get(idx);
				if (selected == current) {
					System.out.println("이미 출전 중인 포켓몬입니다!");
					continue;
				}
				if (selected.isFainted()) {
					System.out.println("기절한 포켓몬은 선택할 수 없습니다!");
					continue;
				}
				return selected;
			} catch (NumberFormatException e) {
				System.out.println("올바른 번호를 입력하세요.");
			}
		}
	}

	private void doAttack(Pokemon attacker, Move move, Pokemon defender) {
		System.out.println(attacker.getName() + "의 " + move.getName() + "!");

		if (move.isStatusMove()) {
			tryInflictStatus(move, defender);
			return;
		}

		double mult = TypeEffect.getMultiplier(move.getType(), defender.getType1(), defender.getType2());
		String effMsg = TypeEffect.getEffectMessage(mult);
		if (!effMsg.isEmpty()) System.out.println(effMsg);

		int dmg = calculateDamage(attacker, move, mult);
		if (attacker.getStatusEffect().isBurned()) dmg /= 2;

		defender.takeDamage(dmg);
		// System.out.println("→ " + defender.getName() + "에게 " + dmg + "의 데미지!"); // 데미지 얼마나 들어갔는지 확인하고 싶으면 이거 추가

		tryInflictStatus(move, defender);
	}

	private void tryInflictStatus(Move move, Pokemon target) {
		if (move.getStatusEffect().equals(StatusEffect.NONE)) return;
		if (!target.getStatusEffect().getStatus().equals(StatusEffect.NONE)) return;
		int roll = (int) (Math.random() * 100);
		if (roll < move.getStatusChance()) {
			target.getStatusEffect().apply(move.getStatusEffect());
			System.out.println(target.getName() + "은(는) " + move.getStatusEffect() + " 상태가 됐다!");
		}
	}

	private int calculateDamage(Pokemon attacker, Move move, double multiplier) {
		return (int) (attacker.getAttack() * move.getPower() * multiplier / 50.0);
	}

	private void endOfTurn(Pokemon mine, Pokemon wild) {
		mine.getStatusEffect().applyEndOfTurn(mine);
		wild.getStatusEffect().applyEndOfTurn(wild);
	}

	private Pokemon checkMyFainted(Pokemon mine, ArrayList<Pokemon> party) {
		if (!mine.isFainted()) return mine;
		System.out.println(mine.getName() + "은(는) 쓰러졌다!");
		Pokemon next = getFirstAlive(party);
		if (next == null) {
			System.out.println("\n눈앞이 깜깜해졌다...\n");
			return null;
		}
		System.out.println("가라! " + next.getName() + "!\n");
		return next;
	}

	private void printWin(Pokemon wild) {
		System.out.println(wild.getName() + "은(는) 쓰러졌다!");
		System.out.println("전투에서 이겼다!\n");
	}

	private void printStatus(Pokemon mine, Pokemon wild) {
		System.out.println("──────────────────────────────");
		System.out.printf("[적] %-8s HP: %3d / %3d  [%s]%n", wild.getName(), wild.getHp(), wild.getMaxHp(),
				wild.getStatusEffect().getStatus());
		System.out.printf("[나] %-8s HP: %3d / %3d  [%s]%n", mine.getName(), mine.getHp(), mine.getMaxHp(),
				mine.getStatusEffect().getStatus());
		System.out.println("──────────────────────────────");
	}

	private int chooseAction() {
		System.out.println("[1] 싸운다   [2] 포켓몬교체   [3] 도망간다");
		try {
			return Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private Move chooseMove(Pokemon pokemon) {
		ArrayList<Move> moves = pokemon.getMoves();
		System.out.println("기술을 선택하세요:");
		for (int i = 0; i < moves.size(); i++) {
			Move m = moves.get(i);
			System.out.printf("  [%d] %-14s 타입: %-6s 위력: %3d%n", i + 1, m.getName(), m.getType(), m.getPower());
		}
		try {
			int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
			if (idx < 0 || idx >= moves.size()) {
				System.out.println("올바른 번호를 입력하세요.");
				return null;
			}
			return moves.get(idx);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private Move getRandomMove(Pokemon pokemon) {
		ArrayList<Move> moves = pokemon.getMoves();
		if (moves.isEmpty()) return null;
		return moves.get((int) (Math.random() * moves.size()));
	}

	private Pokemon getFirstAlive(ArrayList<Pokemon> party) {
		for (Pokemon p : party) {
			if (!p.isFainted()) return p;
		}
		return null;
	}
}
