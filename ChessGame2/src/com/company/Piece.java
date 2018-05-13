package com.company;

public class Piece {
    //attributes
    private PieceType type;
    private Color color;

    //constructor
    public Piece(PieceType type, Color color){

        this.type = type;
        this.color = color;
    }

    //methods
    public PieceType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public String printType() {

        String label = " ";

        switch(getType()) {
            case P: label = "P";
                break;
            case R: label = "R";
                break;
            case K: label = "K";
                break;
            case B: label = "B";
                break;
            case Q: label = "Q";
                break;
            case G: label = "G";
                break;
            case O: label = " ";
                break;
        }
        return label;
    }

    public String printColor() {

        String col = " ";

        switch(getColor()) {
            case W: col = "W";
                break;
            case B: col = "B";
                break;
            case O: col = " ";
                break;
        }
        return col;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
