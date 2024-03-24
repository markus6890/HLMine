package com.gmail.markushygedombrowski.mines;

public enum MineType {
    TREE, MINE;

    public static MineType getMineType(String type) {
        if (type.equalsIgnoreCase("tree")) {
            return TREE;
        } else if (type.equalsIgnoreCase("mine")) {
            return MINE;
        }
        return null;
    }
}
