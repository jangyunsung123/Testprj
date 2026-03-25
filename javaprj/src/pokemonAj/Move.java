package pokemonAj;

public class Move {

	private String name;
	private String type;
	private int power;
	private String statusEffect;
	private int statusChance;

	public Move(String name, String type, int power, String statusEffect, int statusChance) {
		this.name = name;
		this.type = type;
		this.power = power;
		this.statusEffect = statusEffect;
		this.statusChance = statusChance;
	}

	public Move(String name, String type, int power) {
		this(name, type, power, StatusEffect.NONE, 0);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getPower() {
		return power;
	}

	public String getStatusEffect() {
		return statusEffect;
	}

	public int getStatusChance() {
		return statusChance;
	}

	public boolean isStatusMove() {
		return power == 0;
	}
}
