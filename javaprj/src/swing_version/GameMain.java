package swing_version;

public class GameMain {
    public static void main(String[] args) {
        MapPlayer player = StartGame.start();
        new MapMain(player);
    }
}