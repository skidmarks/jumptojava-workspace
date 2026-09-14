package InterfacePractice;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Animal> animals = new ArrayList<>();
        animals.add(new Dog());
        animals.add(new Cat());
        animals.add(new Dog());

        for (Animal animal : animals) {
            System.out.println(animal.sound());
        }
    }
}
