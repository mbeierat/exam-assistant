package at.mbeier.exass;

import at.mbeier.exass.view.WelcomeFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static Main instance;

    public Main() {
        Main.instance = this;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            new WelcomeFrame().setVisible(true);
        });
    }
}
