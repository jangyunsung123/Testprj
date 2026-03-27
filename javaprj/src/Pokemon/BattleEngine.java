package Pokemon;

import java.util.ArrayList;
import java.util.List;
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
				Skill move = chooseSkill(mine);
				if (move == null) continue;

				if (!mine.getStatusEffect().canAct(mine)) {
					endOfTurn(mine, wildPokemon);
					if (wildPokemon.isFainted()) {
						printWin(wildPokemon);
						// ─── 추가: 전투 승리 후 레벨업/기술/진화 처리 ───
						handlePostBattle(mine, wildPokemon);
						return true;
					}
					mine = checkMyFainted(mine, playerParty);
					if (mine == null) return false;
					continue;
				}

				doAttack(mine, move, wildPokemon);
				if (wildPokemon.isFainted()) {
					printWin(wildPokemon);
					// ─── 추가: 전투 승리 후 레벨업/기술/진화 처리 ───
					handlePostBattle(mine, wildPokemon);
					return true;
				}

				if (wildPokemon.getStatusEffect().canAct(wildPokemon)) {
					Skill wildSkill = getRandomSkill(wildPokemon);
					if (wildSkill != null) doAttack(wildPokemon, wildSkill, mine);
				}

				endOfTurn(mine, wildPokemon);
				if (wildPokemon.isFainted()) {
					printWin(wildPokemon);
					// ─── 추가: 전투 승리 후 레벨업/기술/진화 처리 ───
					handlePostBattle(mine, wildPokemon);
					return true;
				}

				mine = checkMyFainted(mine, playerParty);
				if (mine == null) return false;

			} else if (action == 2) { // 포켓몬교체
				Pokemon next = switchPokemon(playerParty, mine);
				if (next != null) {
					System.out.println("수고했어, " + mine.getName() + "! 들어와!");
					mine = next;
					System.out.println("가라! " + mine.getName() + "!\n");

					// 교체 후 야생 포켓몬 반격 (원작과 동일)
					if (wildPokemon.getStatusEffect().canAct(wildPokemon)) {
						Skill wildSkill = getRandomSkill(wildPokemon);
						if (wildSkill != null) doAttack(wildPokemon, wildSkill, mine);
					}

					// 턴 마무리 (지속 데미지, 카운터 감소)
					endOfTurn(mine, wildPokemon);

					// 반격/턴 마무리 이후 기절 체크
					mine = checkMyFainted(mine, playerParty);
					if (mine == null) return false;
				}

			} else if (action == 3) { // 도망간다
				System.out.println("도망쳤다!");
				return false;

			} else {
				System.out.println("1~3 중에서 입력하세요.");
			}
		}
	}

	// ─────────────────────────────────────────────────────────────
	// 추가: 전투 승리 후 처리 (레벨업 → 기술 습득 → 진화 체크)
	// ─────────────────────────────────────────────────────────────
	private void handlePostBattle(Pokemon mine, Pokemon defeated) {
		// 1) 레벨업
		mine.levelUp();
		System.out.println("\n" + mine.getName() + "의 레벨이 " + mine.getLevel() + "로 올랐다!");
		System.out.printf("  HP: %d  ATK: %d  (개체값 HP:%d ATK:%d)%n",
				mine.getMaxHp(), mine.getAttack(), mine.getIvHp(), mine.getIvAtk());

		// 2) 레벨업 기술 습득 체크
		String learnableName = BT_LearnSet.getLearnableSkill(mine.getName(), mine.getLevel());
		if (learnableName != null) {
			System.out.println("\n" + mine.getName() + "은(는) " + learnableName + "을(를) 떠올렸다!");
			System.out.println("배우고 싶다! 기술을 추가하시겠습니까? [1: 예  2: 아니오]");
			try {
				int choice = Integer.parseInt(scanner.nextLine().trim());
				if (choice == 1) {
					tryLearnSkill(mine, learnableName);
				} else {
					System.out.println(learnableName + "을(를) 배우지 않았다.");
				}
			} catch (NumberFormatException e) {
				System.out.println("배우지 않았다.");
			}
		}

		// 3) 진화 체크
		if (BT_Evolution.canEvolve(mine.getName(), mine.getLevel())) {
			String evolvedName = BT_Evolution.getEvolvedName(mine.getName());
			System.out.println("\n어라? " + mine.getName() + "의 모습이...!");
			System.out.println("[1] 진화한다   [2] 진화하지 않는다");
			try {
				int choice = Integer.parseInt(scanner.nextLine().trim());
				if (choice == 1) {
					System.out.println("축하합니다! " + mine.getName() + "은(는) "
							+ evolvedName + "로 진화했다!");
					mine.evolve(evolvedName);
				} else {
					System.out.println(mine.getName() + "은(는) 진화를 거부했다.");
				}
			} catch (NumberFormatException e) {
				System.out.println("진화를 건너뜁니다.");
			}
		}
	}

	// ─────────────────────────────────────────────────────────────
	// 추가: 기술 습득 처리 (빈 슬롯 있으면 바로 추가, 없으면 교체 선택)
	// ─────────────────────────────────────────────────────────────
	private void tryLearnSkill(Pokemon mine, String skillName) {
		if (mine.canLearnSkill()) {
			// 빈 슬롯에 Skill 객체 생성 후 추가
			// ※ 실제 기술 데이터(위력, 효과 등)는 BT_LearnSet 기술명 기준으로 직접 생성
			Skill newSkill = createSkillByName(skillName);
			if (newSkill == null) {
				System.out.println("(기술 데이터를 찾을 수 없어 배우지 못했다.)");
				return;
			}
			mine.addSkill(newSkill);
			System.out.println(mine.getName() + "은(는) " + skillName + "을(를) 배웠다!");

		} else {
			// 기술이 4개 꽉 찬 경우 → 교체할 슬롯 선택
			System.out.println("이미 기술이 4개입니다. 어떤 기술을 잊게 할까요?");
			List<Skill> skills = mine.getSkills();
			for (int i = 0; i < skills.size(); i++) {
				Skill s = skills.get(i);
				System.out.printf("  [%d] %-14s 타입: %-6s 위력: %3d%n",
						i + 1, s.getName(), s.getType(), s.getPower());
			}
			System.out.println("  [0] " + skillName + "을(를) 배우지 않는다");

			try {
				int idx = Integer.parseInt(scanner.nextLine().trim());
				if (idx == 0) {
					System.out.println(mine.getName() + "은(는) " + skillName + "을(를) 배우지 않았다.");
				} else if (idx >= 1 && idx <= skills.size()) {
					String forgotName = skills.get(idx - 1).getName();
					Skill newSkill = createSkillByName(skillName);
					if (newSkill == null) {
						System.out.println("(기술 데이터를 찾을 수 없어 배우지 못했다.)");
						return;
					}
					mine.replaceSkill(idx - 1, newSkill);
					System.out.println(mine.getName() + "은(는) " + forgotName
							+ "을(를) 잊고, " + skillName + "을(를) 배웠다!");
				} else {
					System.out.println("올바른 번호를 입력하세요. 배우지 않습니다.");
				}
			} catch (NumberFormatException e) {
				System.out.println("배우지 않았다.");
			}
		}
	}

	// ─────────────────────────────────────────────────────────────
	// 기술 이름으로 Skill 객체 생성 (BT_LearnSet 연동용)
	// GameDataManager의 스킬 목록과 일치해야 함 (A파트 담당자 확인)
	// ─────────────────────────────────────────────────────────────
	private Skill createSkillByName(String name) {
		switch (name) {
			// 노말
			case "재빠른공격":      return new Skill("재빠른공격", "노말", 40, "없음");
			case "몸통박치기":      return new Skill("몸통박치기", "노말", 40, "없음");
			// 불
			case "화염방사":        return new Skill("화염방사", "불", 90, "화상 10%");
			case "분화":            return new Skill("분화", "불", 150, "없음");
			case "불꽃세례":        return new Skill("불꽃세례", "불", 60, "화상 30%");
			// 물
			case "파도타기":        return new Skill("파도타기", "물", 90, "없음");
			case "하이드로펌프":    return new Skill("하이드로펌프", "물", 110, "없음");
			case "물대포":          return new Skill("물대포", "물", 40, "없음");
			// 풀
			case "기가드레인":      return new Skill("기가드레인", "풀", 90, "흡수");
			case "솔라빔":          return new Skill("솔라빔", "풀", 120, "없음");
			case "잎날가르기":      return new Skill("잎날가르기", "풀", 55, "없음");
			// 전기
			case "10만볼트":        return new Skill("10만볼트", "전기", 90, "마비 10%");
			case "번개":            return new Skill("번개", "전기", 110, "마비 30%");
			case "전기쇼크":        return new Skill("전기쇼크", "전기", 40, "마비 10%");
			case "방전":            return new Skill("방전", "전기", 80, "마비 10%");
			// 얼음
			case "냉동펀치":        return new Skill("냉동펀치", "얼음", 75, "동상 10%");
			case "눈사태":          return new Skill("눈사태", "얼음", 60, "동상 10%");
			// 격투
			case "인파이트":        return new Skill("인파이트", "격투", 120, "없음");
			case "오라구":          return new Skill("오라구", "격투", 80, "없음");
			// 독
			case "맹독":            return new Skill("맹독", "독", 0, "독 100%");
			case "오물폭탄":        return new Skill("오물폭탄", "독", 90, "독 30%");
			// 땅
			case "지진":            return new Skill("지진", "땅", 100, "없음");
			// 비행
			case "에어슬래시":      return new Skill("에어슬래시", "비행", 75, "없음");
			case "폭풍":            return new Skill("폭풍", "비행", 110, "없음");
			// 에스퍼
			case "사이코키네시스":  return new Skill("사이코키네시스", "에스퍼", 90, "없음");
			case "미래예지":        return new Skill("미래예지", "에스퍼", 120, "없음");
			// 벌레
			case "시저크로스":      return new Skill("시저크로스", "벌레", 80, "없음");
			// 바위
			case "스톤샤워":        return new Skill("스톤샤워", "바위", 75, "없음");
			// 고스트
			case "섀도볼":          return new Skill("섀도볼", "고스트", 80, "없음");
			case "섀도다이브":      return new Skill("섀도다이브", "고스트", 120, "없음");
			// 드래곤
			case "드래곤클로":      return new Skill("드래곤클로", "드래곤", 80, "없음");
			case "드래곤다이브":    return new Skill("드래곤다이브", "드래곤", 100, "없음");
			case "역린":            return new Skill("역린", "드래곤", 120, "혼란 100%");
			// 악
			case "깜짝베기":        return new Skill("깜짝베기", "악", 70, "없음");
			case "악의파동":        return new Skill("악의파동", "악", 80, "없음");
			// 페어리
			case "문포스":          return new Skill("문포스", "페어리", 95, "없음");
			case "매지컬샤인":      return new Skill("매지컬샤인", "페어리", 80, "없음");
			default:               return null;
		}
	}

	// ─────────────────────────────────────────────────────────────
	// 이하 기존 메서드 (변경 없음)
	// ─────────────────────────────────────────────────────────────

	private Pokemon switchPokemon(ArrayList<Pokemon> party, Pokemon current) {
		System.out.println("\n╔═══════════════════════════════════╗");
		System.out.println("포켓몬을 선택하세요:");
		for (int i = 0; i < party.size(); i++) {
			Pokemon p = party.get(i);
			if (p == current) {
				System.out.printf("  [%d] %-8s HP: %3d / %3d  (출전 중)%n", i + 1, p.getName(), p.getHp(), p.getMaxHp());
			} else if (p.isFainted()) {
				System.out.printf("  [%d] %-8s [기절]%n", i + 1, p.getName());
			} else {
				System.out.printf("  [%d] %-8s HP: %3d / %3d%n", i + 1, p.getName(), p.getHp(), p.getMaxHp());
			}
		}
		System.out.println("╚════════════════════════════════════╝\n");

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

	private void doAttack(Pokemon attacker, Skill move, Pokemon defender) {
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
		// 데미지 수치 확인하고 싶으면 아래 주석 해제
		// System.out.println("→ " + defender.getName() + "에게 " + dmg + "의 데미지!");

		tryInflictStatus(move, defender);
	}

	private void tryInflictStatus(Skill move, Pokemon target) {
		if (move.getStatusEffect().equals(StatusEffect.NONE)) return;
		if (!target.getStatusEffect().getStatus().equals(StatusEffect.NONE)) return;
		int roll = (int) (Math.random() * 100);
		if (roll < move.getStatusChance()) {
			target.getStatusEffect().apply(move.getStatusEffect());
			System.out.println(target.getName() + "은(는) " + move.getStatusEffect() + " 상태가 됐다!");
		}
	}

	private int calculateDamage(Pokemon attacker, Skill move, double multiplier) {
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

	private Skill chooseSkill(Pokemon pokemon) {
		List<Skill> moves = pokemon.getSkills();
		System.out.println("기술을 선택하세요:");
		for (int i = 0; i < moves.size(); i++) {
			Skill m = moves.get(i);
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

	private Skill getRandomSkill(Pokemon pokemon) {
		List<Skill> moves = pokemon.getSkills();
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
