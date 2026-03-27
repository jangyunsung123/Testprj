package swing_version;

import java.util.Scanner;

public class StartGame {

    public static MapPlayer start() {
        Scanner sc = new Scanner(System.in);
        Pokedex pokedex = GameDataManager.createDefaultPokedex();

        sleep();
        System.out.println(".");
        sleep();
        System.out.println(".");
        sleep();
        System.out.println(".");
        sleep();
        System.out.println("여기는 태초마을");
        sleep();
        System.out.println("태초는 새하얀 근원의 색");
        sleep();
        sleep();

        System.out.println("\n오박사: 만나서 반갑다!");
        sleep();
        System.out.println("포켓몬스터의 세계에 잘왔단다!");
        sleep();
        System.out.println("내 이름은 오박사, 포켓몬 연구를 하고 있단다!");
        sleep();

        int genderChoice;
        while (true) {
            System.out.println("\n자네는 남자인가? 아니면 여자인가?");
            System.out.println("1. 남자  2. 여자");
            System.out.print("번호 선택: ");

            try {
                genderChoice = Integer.parseInt(sc.nextLine());
                if (genderChoice == 1 || genderChoice == 2) break;
            } catch (Exception e) {
            }

            System.out.println("잘못된 입력입니다! 다시 선택해주세요.");
        }

        String gender = (genderChoice == 1) ? "남자" : "여자";
        System.out.println("자네는 " + gender + "로구나!");
        sleep();

        System.out.println("\n슬슬 너의 이름을 가르쳐다오!");
        System.out.print("당신의 이름은? : ");
        String playerName = sc.nextLine().trim();

        while (playerName.isEmpty()) {
            System.out.print("이름을 다시 입력해주세요: ");
            playerName = sc.nextLine().trim();
        }

        System.out.println("흠...");
        sleep();
        System.out.println("너는 " + playerName + "이라고 하는구나!");
        sleep();

        StartingPokemon[] starters = {
                new StartingPokemon("이상해씨", "풀", 0.7, 6.9, "태어났을 때부터 등에 이상한 씨앗이 심어져 있으며 몸과 함께 자란다고 한다."),
                new StartingPokemon("꼬부기", "물", 0.5, 9.0, "등껍질에 숨어 몸을 보호한다. 상대의 빈틈을 놓치지 않고 물을 뿜어내어 반격한다."),
                new StartingPokemon("파이리", "불", 0.6, 8.5, "꼬리의 불꽃은 파이리의 생명력의 상징이다. 건강할 때 왕성하게 불타오른다.")
        };

        StartingPokemon selected;

        while (true) {
            System.out.println("\n바깥은 혼자 돌아다니기엔 위험하단다!");
            sleep();
            System.out.println("이 아이들 중 하나를 데려가렴!");
            sleep();
            System.out.println("좋아하는 걸 1마리를 주겠다!");
            sleep();
            System.out.println("......자 골라보렴!");

            for (int i = 0; i < starters.length; i++) {
                System.out.println("\n" + (i + 1) + ". " + starters[i].getName());
                starters[i].showInfo();
            }

            int choice;
            while (true) {
                System.out.print("번호 선택: ");
                try {
                    choice = Integer.parseInt(sc.nextLine());
                    if (choice >= 1 && choice <= 3) break;
                } catch (Exception e) {
                }
                System.out.println("잘못된 선택입니다! 다시 입력해주세요.");
            }

            selected = starters[choice - 1];

            if (choice == 1) {
                System.out.println("그렇구나! 이상해씨가 맘에 드는구나!");
                System.out.println("이 애는 엄청 키우기 쉽단다!");
            } else if (choice == 2) {
                System.out.println("흠, 꼬부기가 맘에 드는구나!");
                System.out.println("키우는 보람이 있는 포켓몬이지!");
            } else {
                System.out.println("그래! 파이리가 맘에 드는구나!");
                System.out.println("천천히 키우면 좋단다!");
            }

            sleep();

            int confirm;
            while (true) {
                System.out.println("\n" + playerName + "은(는) " + selected.type + " 포켓몬 " + selected.getName() + "가 맘에 드는 거니?");
                System.out.println("1. 예  2. 아니요");
                System.out.print("번호 선택: ");

                try {
                    confirm = Integer.parseInt(sc.nextLine());
                    if (confirm == 1 || confirm == 2) break;
                } catch (Exception e) {
                }

                System.out.println("잘못된 입력입니다!");
            }

            if (confirm == 1) break;
            System.out.println("다시 선택하게 해주마!");
        }
        System.out.println("\n" + playerName + "은(는) 오박사에게 " + selected.getName() + "를(을) 받았다!");
        sleep();

        MapPlayer player = new MapPlayer();
        player.setName(playerName);

        Pokemon starterPokemon = pokedex.getPokemon(selected.getName());
        if (starterPokemon != null) {
            starterPokemon = starterPokemon.copy();

            int nickChoice;
            while (true) {
                System.out.println("\n" + selected.getName() + "에게 별명을 붙이겠습니까?");
                System.out.println("1. 예  2. 아니요");
                System.out.print("번호 선택: ");

                try {
                    nickChoice = Integer.parseInt(sc.nextLine());
                    if (nickChoice == 1 || nickChoice == 2) break;
                } catch (Exception e) {
                }

                System.out.println("잘못된 입력입니다!");
            }

            if (nickChoice == 1) {
                System.out.print("닉네임 입력: ");
                String nickname = sc.nextLine().trim();

                while (nickname.isEmpty()) {
                    System.out.print("닉네임을 다시 입력해주세요: ");
                    nickname = sc.nextLine().trim();
                }

                starterPokemon.setName(nickname);
            }

            player.addPokemon(starterPokemon);
            System.out.println("\n" + starterPokemon.getName() + "은(는) " + playerName + "의 포켓몬이 되었다!");
            sleep();
        }
        System.out.println("\n" + playerName + "! 준비는 되었는가?");
        sleep();
        System.out.println("이제부터 너만의 이야기가 시작된다!");
        sleep();
        System.out.println("꿈과 모험과!");
        sleep();
        System.out.println("포켓몬스터의 세계로!");
        sleep();
        System.out.println("레츠 고-!");
        sleep();

        return player;
    }

    private static void sleep() {
        try {
            Thread.sleep(800);
        } catch (Exception e) {
        }
    }
}