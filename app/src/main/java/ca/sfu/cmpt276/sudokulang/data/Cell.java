package ca.sfu.cmpt276.sudokulang.data;

public interface Cell {
    /**
     * @return Value of the cell, 0 if it's empty.
     */
    int getValue();

    boolean isPrefilled();

    boolean isErrorCell();

    boolean isEmpty();
}