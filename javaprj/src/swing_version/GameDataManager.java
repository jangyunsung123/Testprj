package swing_version;

import java.util.Arrays;

class GameDataManager {
    public static Pokedex createDefaultPokedex() {
        Pokedex pokedex = new Pokedex();

        Skill quickAttack   = new Skill("전광석화", "노말", 40, "없음");
        Skill tackle        = new Skill("몸통박치기", "노말", 40, "없음");
        Skill eruption      = new Skill("분화", "불", 150, "없음");
        Skill flamethrower  = new Skill("화염방사", "불", 90, "화상 10%");
        Skill hydroPump     = new Skill("하이드로펌프", "물", 110, "없음");
        Skill surf          = new Skill("파도타기", "물", 90, "없음");
        Skill sleepPowder   = new Skill("수면가루", "풀", 0, "수면 100%");
        Skill gigaDrain     = new Skill("기가드레인", "풀", 90, "흡수");
        Skill thunderbolt   = new Skill("10만볼트", "전기", 90, "마비 10%");
        Skill thunderPunch  = new Skill("번개펀치", "전기", 75, "마비 10%");
        Skill icyWind       = new Skill("얼어붙은 바람", "얼음", 55, "동상 30%");
        Skill icePunch      = new Skill("냉동펀치", "얼음", 75, "동상 10%");
        Skill forcePalm     = new Skill("발경", "격투", 60, "마비 30%");
        Skill closeCombat   = new Skill("인파이트", "격투", 120, "없음");
        Skill sludgeBomb    = new Skill("오물폭탄", "독", 90, "독 30%");
        Skill toxic         = new Skill("맹독", "독", 0, "독 100%");
        Skill earthquake    = new Skill("지진", "땅", 100, "없음");
        Skill dragonClaw    = new Skill("드래곤클로", "드래곤", 80, "없음");
        Skill hurricane     = new Skill("폭풍", "비행", 110, "없음");
        Skill confusionWave = new Skill("염동력", "에스퍼", 50, "혼란 30%");
        Skill psychic       = new Skill("사이코키네시스", "에스퍼", 90, "없음");
        Skill xScissor      = new Skill("시저크로스", "벌레", 80, "없음");
        Skill airSlash      = new Skill("에어슬래시", "비행", 75, "없음");
        Skill rockSlide     = new Skill("스톤샤워", "바위", 75, "없음");
        Skill bulldoze      = new Skill("땅고르기", "땅", 80, "없음");
        Skill shadowBall    = new Skill("섀도볼", "고스트", 80, "없음");
        Skill dragonDive    = new Skill("드래곤다이브", "드래곤", 100, "없음");
        Skill twister       = new Skill("회오리", "드래곤", 40, "혼란 30%");
        Skill discharge     = new Skill("방전", "전기", 80, "마비 10%");
        Skill thunderWave   = new Skill("전기자석파", "전기", 0, "마비 100%");
        Skill nightSlash    = new Skill("깜짝베기", "악", 70, "없음");
        Skill bite          = new Skill("물기", "악", 60, "혼란 20%");
        Skill moonblast     = new Skill("문포스", "페어리", 95, "없음");
        Skill charm         = new Skill("애교부리기", "페어리", 0, "혼란 100%");
        Skill shadowDive    = new Skill("섀도다이브", "고스트", 120, "없음");
        Skill dragonPulse   = new Skill("용의파동", "드래곤", 85, "혼란 20%");

        // 스타팅용 스킬 추가
        Skill vineWhip      = new Skill("덩굴채찍", "풀", 45, "없음");
        Skill razorLeaf     = new Skill("잎날가르기", "풀", 55, "없음");
        Skill waterGun      = new Skill("물대포", "물", 40, "없음");
        Skill bubble        = new Skill("거품", "물", 40, "없음");
        Skill ember         = new Skill("불꽃세례", "불", 40, "화상 10%");
        Skill scratch       = new Skill("할퀴기", "노말", 40, "없음");

        Skill[] allSkills = {
                quickAttack, tackle, eruption, flamethrower, hydroPump, surf,
                sleepPowder, gigaDrain, thunderbolt, thunderPunch, icyWind, icePunch,
                forcePalm, closeCombat, sludgeBomb, toxic, earthquake, dragonClaw,
                hurricane, confusionWave, psychic, xScissor, airSlash, rockSlide,
                bulldoze, shadowBall, dragonDive, twister, discharge, thunderWave,
                nightSlash, bite, moonblast, charm, shadowDive, dragonPulse,
                vineWhip, razorLeaf, waterGun, bubble, ember, scratch
        };
        for (Skill s : allSkills) pokedex.addSkill(s);

        pokedex.addPokemon(new Pokemon("이브이", 12, 95, 35, "노말", "Wild", Arrays.asList(quickAttack, tackle)));
        pokedex.addPokemon(new Pokemon("브케인", 12, 100, 40, "불", "Wild", Arrays.asList(eruption, flamethrower)));
        pokedex.addPokemon(new Pokemon("별가사리", 12, 100, 38, "물", "Wild", Arrays.asList(hydroPump, surf)));
        pokedex.addPokemon(new Pokemon("치코리타", 12, 105, 34, "풀", "Wild", Arrays.asList(sleepPowder, gigaDrain)));
        pokedex.addPokemon(new Pokemon("피카츄", 12, 90, 42, "전기", "Wild", Arrays.asList(thunderbolt, thunderPunch)));
        pokedex.addPokemon(new Pokemon("포푸니", 13, 95, 45, "얼음/악", "Wild", Arrays.asList(icyWind, icePunch)));
        pokedex.addPokemon(new Pokemon("리오르", 13, 100, 46, "격투", "Wild", Arrays.asList(forcePalm, closeCombat)));
        pokedex.addPokemon(new Pokemon("또가스", 12, 110, 36, "독", "Wild", Arrays.asList(sludgeBomb, toxic)));
        pokedex.addPokemon(new Pokemon("딥상어동", 14, 120, 50, "땅/드래곤", "Wild", Arrays.asList(earthquake, dragonClaw)));
        pokedex.addPokemon(new Pokemon("구구", 10, 85, 28, "비행/노말", "Wild", Arrays.asList(hurricane, quickAttack)));
        pokedex.addPokemon(new Pokemon("야돈", 13, 130, 32, "에스퍼/물", "Wild", Arrays.asList(confusionWave, psychic)));
        pokedex.addPokemon(new Pokemon("스라크", 14, 110, 48, "벌레/비행", "Wild", Arrays.asList(xScissor, airSlash)));
        pokedex.addPokemon(new Pokemon("꼬마돌", 12, 115, 44, "바위/땅", "Wild", Arrays.asList(rockSlide, bulldoze)));
        pokedex.addPokemon(new Pokemon("고오스", 12, 90, 43, "고스트/독", "Wild", Arrays.asList(shadowBall, sludgeBomb)));
        pokedex.addPokemon(new Pokemon("미뇽", 14, 115, 47, "드래곤", "Wild", Arrays.asList(dragonDive, twister)));
        pokedex.addPokemon(new Pokemon("코일", 12, 95, 39, "강철/전기", "Wild", Arrays.asList(discharge, thunderWave)));
        pokedex.addPokemon(new Pokemon("나옹", 11, 88, 37, "악", "Wild", Arrays.asList(nightSlash, bite)));
        pokedex.addPokemon(new Pokemon("삐삐", 12, 100, 33, "페어리", "Wild", Arrays.asList(moonblast, charm)));

        // 스타팅 3마리 추가
        pokedex.addPokemon(new Pokemon("이상해씨", 5, 100, 35, "풀", "Starter", Arrays.asList(vineWhip, razorLeaf)));
        pokedex.addPokemon(new Pokemon("꼬부기", 5, 105, 34, "물", "Starter", Arrays.asList(waterGun, bubble)));
        pokedex.addPokemon(new Pokemon("파이리", 5, 95, 38, "불", "Starter", Arrays.asList(ember, scratch)));

        pokedex.addPokemon(new Pokemon("기라티나", 30, 250, 70, "고스트/드래곤", "Legendary", Arrays.asList(shadowDive, dragonPulse)));
        return pokedex;
    }
}