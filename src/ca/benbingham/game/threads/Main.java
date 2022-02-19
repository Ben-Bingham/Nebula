package ca.benbingham.game.threads;

import static ca.benbingham.engine.util.Printing.print;

public class Main {
    public static void main(String[] args) {
        print("Program started");
        Game game = new Game();
        game.initialize();
    }
}
