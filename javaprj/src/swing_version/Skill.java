package swing_version;

import java.io.Serializable;

class Skill implements Serializable {
    private String name;
    private String type;
    private int power;
    private String effect;

    public Skill(String name, String type, int power, String effect) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.effect = effect;
    }

    public boolean isStatusMove() {
        return power == 0;
    }

    public String getStatusEffect() {
        if (effect.equals("없음") || effect.equals("흡수")) return StatusEffect.NONE;
        String[] parts = effect.split(" ");
        if (parts.length >= 2) return parts[0];
        return StatusEffect.NONE;
    }

    public int getStatusChance() {
        if (effect.equals("없음") || effect.equals("흡수")) return 0;
        String[] parts = effect.split(" ");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1].replace("%", ""));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getPower() { return power; }
    public String getEffect() { return effect; }
}
