package pokemonAj;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BattleEngine {

	private BattleLogger logger = System.out::println;
	private Component parentComponent;

	public void setLogger(BattleLogger logger) {
		if (logger != null) {
			this.logger = logger;
		}
	}

	public void setParentComponent(Component parentComponent) {
		this.parentComponent = parentComponent;
	}

	public void setLogArea(JTextArea logArea) {
		if (logArea == null) return;

		setLogger(message -> {
			logArea.append(message + "\n");
			logArea.setCaretPosition(logArea.getDocument().getLength());
		});
	}

	private void printLog(String message) {
		logger.log(message);
	}

	public boolean startBattle(ArrayList<Pokemon> playerParty, Pokemon enemyPokemon) {
		printLog("");
		printLog("╔═══════════════════════════════╗");
		printLog(" 야생의 " + enemyPokemon.getName() + "이(가) 나타났다!");
		printLog("╚═══════════════════════════════╝");

		Pokemon mine = getFirstAlive(playerParty);
		if (mine == null) {
			printLog("싸울 수 있는 포켓몬이 없다!");
			return false;
		}

		printLog("가라! " + mine.getName() + "!");

		while (true) {
			printStatus(mine, enemyPokemon);

			int action = chooseAction(mine, playerParty);
			if (action == -1) {
				continue;
			}

			if (action == 0) { // 싸운다
				Skill move = chooseSkill(mine);
				if (move == null) continue;

				if (!mine.getStatusEffect().canAct(mine, logger)) {
					endOfTurn(mine, enemyPokemon);
					mine = checkMyFainted(mine, playerParty);
					if (mine == null) return false;
					if (enemyPokemon.isFainted()) {
						printWin(enemyPokemon);
						return true;
					}
					continue;
				}

				doAttack(mine, move, enemyPokemon);
				if (enemyPokemon.isFainted()) {
					printWin(enemyPokemon);
					return true;
				}

				if (!enemyPokemon.getStatusEffect().canAct(enemyPokemon, logger)) {
					endOfTurn(mine, enemyPokemon);
					mine = checkMyFainted(mine, playerParty);
					if (mine == null) return false;
					if (enemyPokemon.isFainted()) {
						printWin(enemyPokemon);
						return true;
					}
					continue;
				}

				enemyTurn(mine, enemyPokemon);
				mine = checkMyFainted(mine, playerParty);
				if (mine == null) return false;
			}
			else if (action == 1) { // 교체
				Pokemon next = switchPokemon(playerParty, mine);
				if (next != null && next != mine) {
					mine = next;
					printLog("가라! " + mine.getName() + "!");
					enemyTurn(mine, enemyPokemon);
					mine = checkMyFainted(mine, playerParty);
					if (mine == null) return false;
				}
			}
			else if (action == 2) { // 도망
				if (Math.random() < 0.5) {
					printLog("성공적으로 도망쳤다!");
					return false;
				} else {
					printLog("도망치지 못했다!");
					enemyTurn(mine, enemyPokemon);
					mine = checkMyFainted(mine, playerParty);
					if (mine == null) return false;
				}
			}

			endOfTurn(mine, enemyPokemon);

			if (enemyPokemon.isFainted()) {
				printWin(enemyPokemon);
				return true;
			}

			mine = checkMyFainted(mine, playerParty);
			if (mine == null) return false;
		}
	}

	private void doAttack(Pokemon attacker, Skill skill, Pokemon target) {
		printLog(attacker.getName() + "의 " + skill.getName() + "!");

		if (skill.isStatusMove()) {
			applySkillEffect(skill, target);
			return;
		}

		double multiplier = TypeEffect.getMultiplier(
				normalizeType(skill.getType()),
				normalizeType(target.getType1()),
				normalizeType(target.getType2())
		);

		int baseDamage = attacker.getAttack() + skill.getPower() / 5;
		int damage = Math.max(1, (int) (baseDamage * multiplier));

		target.takeDamage(damage);
		printLog(target.getName() + "에게 " + damage + "의 데미지!");

		String effectMessage = TypeEffect.getEffectMessage(multiplier);
		if (!effectMessage.isEmpty()) {
			printLog(effectMessage);
		}

		applySkillEffect(skill, target);
	}

	private void applySkillEffect(Skill skill, Pokemon target) {
		String effect = skill.getStatusEffect();
		int chance = skill.getStatusChance();

		if (!StatusEffect.NONE.equals(effect)) {
			if (Math.random() * 100 < chance) {
				if (StatusEffect.NONE.equals(target.getStatusEffect().getStatus())) {
					target.getStatusEffect().apply(effect);
					printLog(target.getName() + "은(는) " + effect + " 상태가 되었다!");
				}
			}
		}
	}

	private void enemyTurn(Pokemon mine, Pokemon enemy) {
		Skill enemySkill = enemy.getSkills().get((int) (Math.random() * enemy.getSkills().size()));
		printLog("야생의 " + enemy.getName() + "의 " + enemySkill.getName() + "!");

		if (enemySkill.isStatusMove()) {
			applySkillEffect(enemySkill, mine);
			return;
		}

		double multiplier = TypeEffect.getMultiplier(
				normalizeType(enemySkill.getType()),
				normalizeType(mine.getType1()),
				normalizeType(mine.getType2())
		);

		int baseDamage = enemy.getAttack() + enemySkill.getPower() / 5;
		int damage = Math.max(1, (int) (baseDamage * multiplier));

		mine.takeDamage(damage);
		printLog(mine.getName() + "은(는) " + damage + "의 데미지를 입었다!");

		String effectMessage = TypeEffect.getEffectMessage(multiplier);
		if (!effectMessage.isEmpty()) {
			printLog(effectMessage);
		}

		applySkillEffect(enemySkill, mine);
	}

	private void endOfTurn(Pokemon mine, Pokemon enemy) {
		if (mine != null) {
			mine.getStatusEffect().applyEndOfTurn(mine, logger);
		}
		if (enemy != null) {
			enemy.getStatusEffect().applyEndOfTurn(enemy, logger);
		}
	}

	private void printWin(Pokemon enemy) {
		printLog(enemy.getName() + "은(는) 쓰러졌다! 전투에서 승리했다!");
	}

	private Pokemon checkMyFainted(Pokemon mine, ArrayList<Pokemon> party) {
		if (mine != null && mine.isFainted()) {
			printLog(mine.getName() + "은(는) 쓰러졌다!");
			Pokemon next = getFirstAlive(party);
			if (next != null) {
				printLog("가라! " + next.getName() + "!");
			}
			return next;
		}
		return mine;
	}

	private Pokemon getFirstAlive(ArrayList<Pokemon> party) {
		for (Pokemon p : party) {
			if (!p.isFainted()) {
				return p;
			}
		}
		return null;
	}

	private void printStatus(Pokemon mine, Pokemon enemy) {
		printLog("──────────────────────────────");
		printLog(String.format("[적] %-8s HP: %3d / %3d", enemy.getName(), enemy.getHp(), enemy.getMaxHp()));
		printLog(String.format("[나] %-8s HP: %3d / %3d", mine.getName(), mine.getHp(), mine.getMaxHp()));
		printLog("──────────────────────────────");
	}

	private int chooseAction(Pokemon mine, ArrayList<Pokemon> party) {
		String[] options = {"싸운다", "포켓몬 교체", "도망간다"};
		return JOptionPane.showOptionDialog(
				parentComponent,
				"행동을 선택하세요.\n현재 포켓몬: " + mine.getName(),
				"배틀",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]
		);
	}

	private Skill chooseSkill(Pokemon pokemon) {
		List<Skill> moves = pokemon.getSkills();
		if (moves.isEmpty()) return null;

		String[] options = new String[moves.size()];
		for (int i = 0; i < moves.size(); i++) {
			Skill s = moves.get(i);
			options[i] = s.getName() + " | 타입: " + s.getType() + " | 위력: " + s.getPower();
		}

		int selected = JOptionPane.showOptionDialog(
				parentComponent,
				"기술을 선택하세요.",
				pokemon.getName() + "의 기술",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]
		);

		if (selected < 0 || selected >= moves.size()) {
			return null;
		}
		return moves.get(selected);
	}

	private Pokemon switchPokemon(ArrayList<Pokemon> party, Pokemon current) {
		ArrayList<Pokemon> selectable = new ArrayList<>();
		ArrayList<String> names = new ArrayList<>();

		for (Pokemon p : party) {
			if (!p.isFainted() && p != current) {
				selectable.add(p);
				names.add(p.getName() + " | HP " + p.getHp() + "/" + p.getMaxHp());
			}
		}

		if (selectable.isEmpty()) {
			printLog("교체할 수 있는 포켓몬이 없습니다.");
			return null;
		}

		int selected = JOptionPane.showOptionDialog(
				parentComponent,
				"교체할 포켓몬을 선택하세요.",
				"포켓몬 교체",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				names.toArray(),
				names.get(0)
		);

		if (selected < 0 || selected >= selectable.size()) {
			return null;
		}

		return selectable.get(selected);
	}

	private String normalizeType(String type) {
		if ("불".equals(type)) return "불꽃";
		return type;
	}
}