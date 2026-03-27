package Pokemon;

public class BT_Dialog {
	
	// 호출 방법 : syso 대신 => BT_Dialog("입력할 내용"); 
	
	/**
     * 명령어 하나로 완벽하게 정렬된 대화창을 출력하는 메서드
     * 'public static'으로 선언했기 때문에, 객체 생성 없이 어디서든 바로 쓸 수 있습니다.
     * * @param message 대화창에 띄울 텍스트
     */
    public static void show(String message) {
        // 대화창 안쪽의 총 가로 길이 (포켓몬 대화창 비율에 맞춤)
        int innerWidth = 40; 
        
        // 1. 한글과 영문의 출력 폭(길이)을 정확히 계산하는 로직
        // 한글은 화면에서 2칸을 차지하고, 영문/숫자/공백은 1칸을 차지하므로 이를 분리해서 더함
        int displayLength = 0;
        for (char c : message.toCharArray()) {
            if (c >= '\uAC00' && c <= '\uD7A3') { // 한글 유니코드 범위인 경우
                displayLength += 2; // 2칸으로 계산
            } else {
                displayLength += 1; // 그 외(영어, 띄어쓰기 등)는 1칸으로 계산
            }
        }
        
        // 2. 우측에 채워넣을 빈 여백(공백) 개수 계산
        // 총 너비 - 좌측 기본 여백(2칸) - 실제 글자들이 차지하는 길이
        int rightPadding = innerWidth - 2 - displayLength;
        if (rightPadding < 0) rightPadding = 0; // 메시지가 너무 길 경우 테두리가 깨지는 것을 방지
        
        // 3. 우측 여백만큼 공백 문자열(" ") 만들기
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < rightPadding; i++) {
            spaces.append(" ");
        }
        
        // 4. 최종 대화창 출력 (명령어 실행만 하면 아래 양식으로 대화창 출력 가능)
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│                                        │");
        // 앞쪽 공백 2칸("  ") + 전달받은 메시지 + 뒤쪽 공백(계산된 spaces) + 우측 테두리("│")
        System.out.println("│  " + message + spaces.toString() + "│");
        System.out.println("│                                        │");
        System.out.println("└───────────────────────────────── ▼ ────┘");
        System.out.println(); // 
    }

}
