package pokemonAj;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class Pokedex {
	private Map<String, Skill> skillDb;
	private Map<String, Pokemon> pokemonDb;

	public Pokedex() {
		skillDb = new LinkedHashMap<>();
		pokemonDb = new LinkedHashMap<>();
	}

	public void addSkill(Skill skill)     { skillDb.put(skill.getName(), skill); }
	public void addPokemon(Pokemon pokemon) { pokemonDb.put(pokemon.getName(), pokemon); }
	public Skill getSkill(String skillName)     { return skillDb.get(skillName); }
	public Pokemon getPokemon(String pokemonName) { return pokemonDb.get(pokemonName); }

	public List<Pokemon> getAllPokemon() {
		return new ArrayList<>(pokemonDb.values());
	}

	public List<Pokemon> getByRarity(String rarity) {
		List<Pokemon> result = new ArrayList<>();
		for (Pokemon p : pokemonDb.values()) {
			if (p.getRarity().equalsIgnoreCase(rarity)) result.add(p);
		}
		return result;
	}

	public void displayAll() {
		System.out.println("\n==== 포켓몬 도감 ====");
		int idx = 1;
		for (Pokemon p : pokemonDb.values()) {
			System.out.println(idx + ". " + p.getName() + " | 타입: " + p.getType()
					+ " | Lv." + p.getLevel() + " | HP " + p.getMaxHp()
					+ " | ATK " + p.getAttack() + " | 등급: " + p.getRarity());
			idx++;
		}
	}

	public void displayOne(String pokemonName) {
		Pokemon p = getPokemon(pokemonName);
		if (p == null) {
			System.out.println("해당 포켓몬이 존재하지 않습니다.");
			return;
		}
		System.out.println("\n==== " + p.getName() + " 상세 정보 ====");
		System.out.println("타입: " + p.getType());
		System.out.println("레벨: " + p.getLevel());
		System.out.println("HP: " + p.getCurrentHp() + "/" + p.getMaxHp());
		System.out.println("ATK: " + p.getAttack());
		System.out.println("등급: " + p.getRarity());
		System.out.println("스킬 목록:");
		for (Skill s : p.getSkills()) {
			System.out.println("  - " + s.getName() + " (" + s.getType() + ")"
					+ " | 위력: " + s.getPower() + " | 효과: " + s.getEffect());
		}
	}
}
