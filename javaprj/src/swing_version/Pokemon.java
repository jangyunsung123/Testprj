package swing_version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Pokemon implements Serializable {
    private String name;
    private int level;
    private int maxHp;
    private int currentHp;
    private int attack;
    private String type;
    private String rarity;
    private List<Skill> skills;
    private StatusEffect statusEffect = new StatusEffect();

    public Pokemon(String name, int level, int maxHp, int attack, String type, String rarity, List<Skill> skills) {
        this.name = name;
        this.level = level;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.type = type;
        this.rarity = rarity;
        this.skills = new ArrayList<>(skills);
    }

    public Pokemon copy() {
        Pokemon copied = new Pokemon(name, level, maxHp, attack, type, rarity, skills);
        copied.currentHp = this.currentHp;
        return copied;
    }

    public void healFull() {
        currentHp = maxHp;
        statusEffect.clear();
    }

    public void takeDamage(int dmg) {
        currentHp = Math.max(0, currentHp - dmg);
    }

    public boolean isFainted() {
        return currentHp <= 0;
    }

    public String getType1() {
        return type.contains("/") ? type.split("/")[0] : type;
    }

    public String getType2() {
        return type.contains("/") ? type.split("/")[1] : "없음";
    }

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getMaxHp() { return maxHp; }
    public int getHp() { return currentHp; }
    public int getCurrentHp() { return currentHp; }
    public int getAttack() { return attack; }
    public String getType() { return type; }
    public String getRarity() { return rarity; }
    public List<Skill> getSkills() { return skills; }
    public ArrayList<Skill> getMoves() { return new ArrayList<>(skills); }
    public StatusEffect getStatusEffect() { return statusEffect; }
}
