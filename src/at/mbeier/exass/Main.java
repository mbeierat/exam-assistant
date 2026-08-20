package at.mbeier.exass;

public class Main {

    public static Main instance;

    public Main() {
        Main.instance = this;
    }

    static void main() {
        new Main();
    }
}
