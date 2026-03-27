package Pokemon;

import java.util.HashMap;
import java.util.Map;

/**
 * [D파트 신규] 레벨업 진화 데이터 관리
 * - 진화 조건: 레벨업만
 * - BattleEngine에서 레벨업 후 canEvolve() 호출
 */
public class BT_Evolution {

	// key: 진화 전 포켓몬 이름
	private static final Map<String, Integer> EVO_LEVEL  = new HashMap<>();
	private static final Map<String, String>  EVO_TARGET = new HashMap<>();

	static {
		// register(진화 전 이름, 진화 레벨, 진화 후 이름)
		register("이브이",    16, "부스터");
		register("브케인",    17, "마그케인");
		register("별가사리",  15, "아쿠스타");
		register("치코리타",  16, "베이리프");
		register("피카츄",    16, "라이츄");
		register("포푸니",    32, "눈설왕");
		register("리오르",    20, "루카리오");
		register("또가스",    35, "또도가스");
		register("딥상어동",  30, "상어왕");
		register("구구",      18, "피죤");
		register("야돈",      37, "야도란");
		register("꼬마돌",    25, "딱구리");
		register("고오스",    25, "고우스트");
		register("미뇽",      30, "신뇽");
		register("코일",      30, "레어코일");
		register("나옹",      28, "페르시온");
		// 스라크, 삐삐, 기라티나 → 진화 없음 (등록하지 않음)
	}

	private static void register(String name, int level, String target) {
		EVO_LEVEL.put(name, level);
		EVO_TARGET.put(name, target);
	}

	/**
	 * 진화 가능 여부 확인
	 * @param pokemonName 포켓몬 이름
	 * @param currentLevel 현재 레벨
	 * @return true면 진화 가능
	 */
	public static boolean canEvolve(String pokemonName, int currentLevel) {
		if (!EVO_LEVEL.containsKey(pokemonName)) return false;
		return currentLevel >= EVO_LEVEL.get(pokemonName);
	}

	/**
	 * 진화 후 포켓몬 이름 반환
	 * @param pokemonName 진화 전 이름
	 * @return 진화 후 이름 (없으면 빈 문자열)
	 */
	public static String getEvolvedName(String pokemonName) {
		return EVO_TARGET.getOrDefault(pokemonName, "");
	}

	/**
	 * 진화 레벨 반환 (도감 표시용)
	 * @param pokemonName 포켓몬 이름
	 * @return 진화 레벨, 진화 없으면 -1
	 */
	public static int getEvoLevel(String pokemonName) {
		return EVO_LEVEL.getOrDefault(pokemonName, -1);
	}
}
