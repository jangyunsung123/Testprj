package Pokemon;

import java.util.Random;
import java.util.Scanner;

public class MapMain {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		Map map = new Map();
		MapPlayer player = new MapPlayer();
		
		System.out.println("포켓몬스터의 세계에 온 걸 환영한다!");
		
		while(true) {
			for(int i=0; i<20; i++) System.out.println();
			
			map.printMap(player.x, player.y);
			
			System.out.println("W/A/S/D로 이동 \nQ 종료");
			System.out.println("이동키를 입력 후 엔터를 누르세요");
			System.out.print("입력 : ");
			
			char input = sc.nextLine().toUpperCase().charAt(0);
			
			if(input == 'Q') {
				System.out.println("게임 종료");
				break;
			}
			
			map.move(player, input);
			
			char tile = map.getTile(player.x, player.y);
			
			if(tile == 'C') {
				Center(player, sc);
			}else if(tile == 'G') {
				Gym(player, sc);
			}else if(tile == 'T') {
				Town();
			}
			
		}
		sc.close();
	}
	private static void Center(MapPlayer player, Scanner sc) {
		System.out.println("간호사 조이: \"안녕하세요! 포켓몬센터입니다.\"");
		System.out.println("간호사 조이: \"당신의 포켓몬을 쉬게 해 주겠습니까?\"");
        System.out.println("1. 예   2. 아니요");

        int choice = sc.nextInt();

        if(choice == 1){
            System.out.println("간호사 조이: \"잠시 포켓몬을 맡겨주시겠어요?\"");
            System.out.println("간호사 조이: \"네, 맡아드리겠습니다!\"");

            try { Thread.sleep(1000); } catch(Exception e){}

            System.out.println("간호사 조이: \"오래 기다리셨습니다!\"");
            System.out.println("간호사 조이: \"맡겨 두신 포켓몬이 모두 건강해졌습니다!\"");
            System.out.println("간호사 조이: \"또 이용해 주세요!\"");

            player.healAll();
        }
	}
	

	private static void Gym(MapPlayer player, Scanner sc) {
		System.out.println("체육관 도착!");
		System.out.println("안녕하세요! 미래의 챔피언! 체육관에 도전해주셔서 감사합니다!");
		
		String name = player.getFirstPokemonName();
	    System.out.println("오호... " + name + "와(과) 함께 왔군요.");
	    System.out.println(name + "의 힘을 시험해보겠습니다!");

		
        System.out.println("체육관 관장이 승부를 걸어왔다!");
        System.out.println("싸우시겠습니까?");
        System.out.println("1. 예   2. 아니요");

        int choice = sc.nextInt();

        if(choice == 1){
            System.out.println("전설의 포켓몬이 나타났다!");
            
            System.out.println("어떻게 하시겠습니까?");
            System.out.println("1. 계속 싸운다");
            System.out.println("2. 도망친다");

            int battleChoice = sc.nextInt();

            if(battleChoice == 1){
                System.out.println("전투를 시작합니다!");
                // 여기서 전투 클래스 연결
                // battle.start(player, legendaryPokemon);
            }
            else if(battleChoice == 2){
                Random rand = new Random();
                int chance = rand.nextInt(100);

                if(chance < 2){  //2% 확률
                    System.out.println("안돼! 트레이너 배틀 중에 상대에게 등을 보일 수는 없다!");
                    System.out.println("체육관 관장이 당신을 막아섰다!");
                } else {
                    //System.out.println("당신은 재빨리 체육관을 빠져나왔다...");
                    System.out.println("무사히 도망쳤습니다!");
                }
            }
        }
		
	}

	private static void Town() {
		System.out.println("oo마을에 도착했습니다!");
		
	}

}
