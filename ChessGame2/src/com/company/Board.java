package com.company;

import java.util.Scanner;
import java.lang.Math;

public class Board {
    //attributes
    private Piece[][] board;
    private boolean whiteTurn;
    private boolean whiteCheck; //keeps track of whether white player needs to move their king
    private boolean blackCheck; //keeps track of whether black player needs to move their king

    //constructor
    public Board() {

        board = new Piece[8][8];
        System.out.println("Chessboard initialized.");
    }

    //methods
    /*
    TO DO:
    - Implement checking for checks and checkmates
    - Implement pawn promotion
    - (Create graphical interface)
     */

    public void setUpNewGame(){

        //initialization of black pieces
        board[0][0] = new Piece(PieceType.R, Color.B);
        board[0][1] = new Piece(PieceType.K, Color.B);
        board[0][2] = new Piece(PieceType.B, Color.B);
        board[0][3] = new Piece(PieceType.Q, Color.B);
        board[0][4] = new Piece(PieceType.G, Color.B);
        board[0][5] = new Piece(PieceType.B, Color.B);
        board[0][6] = new Piece(PieceType.K, Color.B);
        board[0][7] = new Piece(PieceType.R, Color.B);

        for (int i = 0; i < 8; i++)
            board[1][i] = new Piece(PieceType.P, Color.B);

        //initialization of white pieces
        board[7][0] = new Piece(PieceType.R, Color.W);
        board[7][1] = new Piece(PieceType.K, Color.W);
        board[7][2] = new Piece(PieceType.B, Color.W);
        board[7][3] = new Piece(PieceType.G, Color.W);
        board[7][4] = new Piece(PieceType.Q, Color.W);
        board[7][5] = new Piece(PieceType.B, Color.W);
        board[7][6] = new Piece(PieceType.K, Color.W);
        board[7][7] = new Piece(PieceType.R, Color.W);

        for (int i = 0; i < 8; i++) {
            board[6][i] = new Piece(PieceType.P, Color.W);
        }

        //initialization of empty space
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Piece(PieceType.O, Color.O);
            }
        }

        //the game begins with a turn of the white player
        whiteTurn = true;

        //no player's king is in danger
        whiteCheck = false;
        blackCheck = false;

        System.out.println("New game ready.");

        playTurn();
    }

    public void playTurn(){

        drawBoard();
        waitForInput();

        return;
    }

    public void drawBoard(){

        System.out.println("+--+--+--+--+--+--+--+--+--+");
        System.out.println("|  |A |B |C |D |E |F |G |H |");
        System.out.println("+--+--+--+--+--+--+--+--+--+");

        for (int i = 0; i < 8; i++) {
            int j = 8 - i;
            System.out.println("|" + "" + j + "" + " |" + board[i][0].printColor() + "" + board[i][0].printType() + "|" + board[i][1].printColor() + "" + board[i][1].printType() + "|" + board[i][2].printColor() + "" + board[i][2].printType() + "|" + board[i][3].printColor() + "" + board[i][3].printType() + "|" + board[i][4].printColor() + "" + board[i][4].printType() + "|" + board[i][5].printColor() + "" + board[i][5].printType() + "|" + board[i][6].printColor() + "" + board[i][6].printType() + "|" + board[i][7].printColor() + "" + board[i][7].printType() + "|");
        }

        System.out.println("+--+--+--+--+--+--+--+--+--+");

        if (whiteTurn) {
            System.out.println("\nIt's the white player's turn.\n");
        } else {
            System.out.println("\nIt's the black player's turn.\n");
        }
    }

    public void waitForInput(){

        Scanner input = new Scanner(System.in);
        String moveFrom = input.next();

        if(moveFrom.equals("reset")){

            setUpNewGame();
            return;
        }
        else {
            if (moveFrom.equals("help")) {

                System.out.println("\nHOW TO PLAY: Type the coordinates of the piece you want to move and press enter. Then type the coordinates of where you want the piece moved and press enter again.");
                System.out.println("             Type in small letters.\n");
                System.out.println("             You can restart the game by typing 'reset' or display these instructions by typing 'help'.\n");

                waitForInput();
            } else {

                System.out.println("to");

                String moveTo = input.next();

                System.out.println("" + moveFrom + " to " + moveTo);

                int fromRow = interpretOrderAsRow(moveFrom);
                int fromColumn = interpretOrderAsColumn(moveFrom);
                int toRow = interpretOrderAsRow(moveTo);
                int toColumn = interpretOrderAsColumn(moveTo);

                movePiece(fromRow, fromColumn, toRow, toColumn);
            }
        }
    }

    public void movePiece(int fromRow, int fromColumn, int toRow, int toColumn){

        if(checkIfEmpty(fromRow, fromColumn)){
            if(checkIfCorrectPlayer(fromRow, fromColumn)){
                if(checkIfLegal(fromRow,fromColumn,toRow,toColumn)){
                    Piece temp = new Piece(PieceType.O, Color.O);

                    board[toRow][toColumn] = board[fromRow][fromColumn];
                    board[fromRow][fromColumn] = temp;

                    if (whiteTurn) {
                        whiteTurn = false;
                    } else {
                        whiteTurn = true;
                    }

                    playTurn();
                }
                else{
                    System.out.println("This is an illegal move.");
                    waitForInput();
                }
            }
            else{
                System.out.println("This is not your piece.");
                waitForInput();
            }
        }
        else{
            System.out.println("You have chosen an empty field.");
            waitForInput();
        }
    }

    public boolean checkIfEmpty(int fromRow, int fromColumn){

        return (board[fromRow][fromColumn].getType() != (PieceType.O));
    }

    public boolean checkIfCorrectPlayer(int fromRow, int fromColumn){

        if((whiteTurn && board[fromRow][fromColumn].getColor() == (Color.W)) || (!whiteTurn && board[fromRow][fromColumn].getColor() == (Color.B))){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfLegal(int fromRow, int fromColumn, int toRow, int toColumn){

        PieceType rules = board[fromRow][fromColumn].getType();

        //checks if move according to rules
        switch (rules) {
            //rules for pawns
            case P:
                //checks if the move is blocked by another piece
                if(checkIfBlocked(fromRow,fromColumn,toRow,toColumn))
                {

                    System.out.println("A");
                    waitForInput();
                    return false;
                }
                //checks if move according to rules
                else {
                    if((whiteTurn && fromRow - toRow == 1 && fromColumn - toColumn == 0 && board[toRow][toColumn].getColor() == (Color.O)) || (whiteTurn && (fromRow - toRow == 2) && fromRow == 6 && fromColumn - toColumn == 0 && board[toRow][toColumn].getColor() == (Color.O)) || (whiteTurn && fromColumn - toColumn == 1 && fromColumn - toColumn == Math.abs(1) && board[toRow][toColumn].getColor() == (Color.B)) || (!whiteTurn && toRow - fromRow == 1 && fromColumn - toColumn == 0 && board[toRow][toColumn].getColor() == (Color.O)) || (!whiteTurn && (toRow - fromRow == 2) && fromRow == 1 && fromColumn - toColumn == 0 && board[toRow][toColumn].getColor() == (Color.O)) || (!whiteTurn && toRow - fromRow == 1 && fromColumn - toColumn == Math.abs(1) && board[toRow][toColumn].getColor() == (Color.W)))
                    {
                        return true;
                    }
                    else
                    {
                        System.out.println("B");
                        return false;
                    }
                }
            case R:
                //checks if the move is blocked by another piece
                if(checkIfBlocked(fromRow,fromColumn,toRow,toColumn))
                {

                    System.out.println("A");
                    waitForInput();
                    return false;
                }
                //checks if move according to rules
                else {
                    if((fromColumn - toColumn == 0 || fromRow - toRow == 0) && board[fromRow][fromColumn].getColor() != board[toRow][toColumn].getColor()){
                        return true;
                    }
                    else{
                        System.out.println("B");
                        return false;
                    }
                }
            case K:
                if(board[fromRow][fromColumn].getColor() != board[toRow][toColumn].getColor() && ((Math.abs(fromRow-toRow) == 2 && Math.abs(fromColumn-toColumn) == 1) || (Math.abs(fromRow-toRow) == 1 && Math.abs(fromColumn-toColumn) == 2))){
                    return true;
                }
                else{
                    System.out.println("B");
                    return false;
                }
            case B:
                //checks if the move is blocked by another piece
                if(checkIfBlocked(fromRow,fromColumn,toRow,toColumn))
                {

                    System.out.println("A");
                    waitForInput();
                    return false;
                }
                //checks if move according to rules
                else {
                    if (board[fromRow][fromColumn].getColor() != board[toRow][toColumn].getColor() && Math.abs(fromRow - toRow) == Math.abs(fromColumn - toColumn)) {
                        return true;
                    } else {
                        System.out.println("B");
                        return false;
                    }
                }
            case Q:
                //checks if the move is blocked by another piece
                if(checkIfBlocked(fromRow,fromColumn,toRow,toColumn))
                {

                    System.out.println("A");
                    waitForInput();
                    return false;
                }
                //checks if move according to rules
                else {
                    if (board[fromRow][fromColumn].getColor() != board[toRow][toColumn].getColor() && ((Math.abs(fromRow - toRow) == Math.abs(fromColumn - toColumn)) || (fromColumn - toColumn == 0 || fromRow - toRow == 0))) {
                        return true;
                    } else {
                        System.out.println("B");
                        return false;
                    }
                }
            case G:
                //checks if move according to rules
                if (board[fromRow][fromColumn].getColor() != board[toRow][toColumn].getColor() && ((Math.abs(fromRow - toRow) == 1 && Math.abs(fromColumn - toColumn) == 1) || (fromColumn - toColumn == 0 && Math.abs(fromRow - toRow) == 1) || (Math.abs(fromColumn - toColumn) == 1 && fromRow - toRow == 0))) {
                    return true;
                } else {
                    System.out.println("B");
                    return false;
                }
            default:
                System.out.println("C");
                return false;
        }
    }

    public boolean checkIfBlocked(int fromRow, int fromColumn, int toRow, int toColumn){

        //finds out whether the move is up, down, left or right

        //the piece is going up
        if(fromRow > toRow && fromColumn == toColumn){
            for(int i = fromRow - 1; i > toRow; i--){
                if(board[i][fromColumn].getType() != (PieceType.O)){
                    System.out.println("The path is blocked. E");
                    return true;
                }
            }
        }

        //the piece is going down
        if(fromRow < toRow && fromColumn == toColumn){
            for(int i = fromRow + 1; i < toRow; i++){
                if(board[i][fromColumn].getType() != (PieceType.O)){
                    System.out.println("The path is blocked. F");
                    return true;
                }
            }
        }

        //the piece is going left
        if(fromRow == toRow && fromColumn > toColumn){
            for(int i = fromColumn - 1; i > toColumn; i--){
                if(board[fromRow][i].getType() != (PieceType.O)){
                    System.out.println("The path is blocked. G");
                    return true;
                }
            }
        }

        //the piece is going right
        if(fromRow == toRow && fromColumn < toColumn){
            for(int i = fromColumn + 1; i < toColumn; i++){
                if(board[fromRow][i].getType() != (PieceType.O)){
                    System.out.println("The path is blocked. H");
                    return true;
                }
            }
        }

        //the piece is going up-left
        if(fromRow > toRow && fromColumn > toColumn){
            for(int i = fromRow - 1; i > toRow; i--){
                for(int j = fromColumn - 1; j > toColumn; j--)
                    if(board[i][j].getType() != (PieceType.O)){
                        System.out.println("The path is blocked. I");
                        return true;
                }
            }
        }

        //the piece is going up-right
        if(fromRow > toRow && fromColumn < toColumn){
            for(int i = fromRow - 1; i > toRow; i--){
                for(int j = fromColumn + 1; j < toColumn; j++)
                    if(board[i][j].getType() != (PieceType.O)){
                        System.out.println("The path is blocked. J");
                        return true;
                    }
            }
        }

        //the piece is going down-left
        if(fromRow < toRow && fromColumn > toColumn){
            for(int i = fromRow + 1; i < toRow; i++){
                for(int j = fromColumn - 1; j > toColumn; j--)
                    if(board[i][j].getType() != (PieceType.O)){
                        System.out.println("The path is blocked. K");
                        return true;
                    }
            }
        }

        //the piece is going down-right
        if(fromRow < toRow && fromColumn < toColumn){
            for(int i = fromRow + 1; i < toRow; i++){
                for(int j = fromColumn + 1; j < toColumn; j++)
                    if(board[i][j].getType() != (PieceType.O)){
                        System.out.println("The path is blocked. L");
                        return true;
                    }
            }
        }

        return false;
    }

    public int interpretOrderAsColumn(String input){

        int result = 10;

        switch(input) {
            case "a1": case "a2": case "a3": case "a4": case "a5": case "a6": case "a7": case "a8":
                result = 0;
                break;
            case "b1": case "b2": case "b3": case "b4": case "b5": case "b6": case "b7": case "b8":
                result = 1;
                break;
            case "c1": case "c2": case "c3": case "c4": case "c5": case "c6": case "c7": case "c8":
                result = 2;
                break;
            case "d1": case "d2": case "d3": case "d4": case "d5": case "d6": case "d7": case "d8":
                result = 3;
                break;
            case "e1": case "e2": case "e3": case "e4": case "e5": case "e6": case "e7": case "e8":
                result = 4;
                break;
            case "f1": case "f2": case "f3": case "f4": case "f5": case "f6": case "f7": case "f8":
                result = 5;
                break;
            case "g1": case "g2": case "g3": case "g4": case "g5": case "g6": case "g7": case "g8":
                result = 6;
                break;
            case "h1": case "h2": case "h3": case "h4": case "h5": case "h6": case "h7": case "h8":
                result = 7;
                break;

            default: System.out.println("This is not a valid coordinate.");
                waitForInput();
                break;
        }
        return result;
    }

    public int interpretOrderAsRow(String input){
        int result = 10;

        switch(input) {
            case "a1": case "b1": case "c1": case "d1": case "e1": case "f1": case "g1": case "h1":
                result = 7;
                break;
            case "a2": case "b2": case "c2": case "d2": case "e2": case "f2": case "g2": case "h2":
                result = 6;
                break;
            case "a3": case "b3": case "c3": case "d3": case "e3": case "f3": case "g3": case "h3":
                result = 5;
                break;
            case "a4": case "b4": case "c4": case "d4": case "e4": case "f4": case "g4": case "h4":
                result = 4;
                break;
            case "a5": case "b5": case "c5": case "d5": case "e5": case "f5": case "g5": case "h5":
                result = 3;
                break;
            case "a6": case "b6": case "c6": case "d6": case "e6": case "f6": case "g6": case "h6":
                result = 2;
                break;
            case "a7": case "b7": case "c7": case "d7": case "e7": case "f7": case "g7": case "h7":
                result = 1;
                break;
            case "a8": case "b8": case "c8": case "d8": case "e8": case "f8": case "g8": case "h8":
                result = 0;
                break;
            default: System.out.println("This is not a valid coordinate.");
                waitForInput();
                break;
        }
        return result;
    }
}
