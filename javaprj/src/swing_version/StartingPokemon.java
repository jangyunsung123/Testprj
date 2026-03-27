package swing_version;

public class StartingPokemon {
	String name;
    String type;
    double height;
    double weight;
    String description;

    public StartingPokemon(String name, String type, double height, double weight, String description){
        this.name = name;
        this.type = type;
        this.height = height;
        this.weight = weight;
        this.description = description;
    }

	public void showInfo() {
		// TODO Auto-generated method stub
		System.out.println("이름: " + name);
        System.out.println("타입: " + type);
        System.out.println("키: " + height + "m");
        System.out.println("몸무게: " + weight + "kg");
        System.out.println("설명: " + description);
        System.out.println("---------------------");
	}

	public String getName(){
        return name;
    }

}
