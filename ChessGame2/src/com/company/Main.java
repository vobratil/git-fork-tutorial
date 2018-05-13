package com.company;

public class Main {

    public static void main(String[] args) {

        System.out.println("===================================");
        System.out.println("   Welcome to the game of chess!");
        System.out.println("===================================\n");

        Board board = new Board();

        System.out.println("\nHOW TO PLAY: Type the coordinates of the piece you want to move and press enter. Then type the coordinates of where you want the piece moved and press enter again.");
        System.out.println("             Type in small letters.\n");
        System.out.println("             You can restart the game by typing 'reset' or display these instructions by typing 'help'.\n");

        board.setUpNewGame();
    }
}
