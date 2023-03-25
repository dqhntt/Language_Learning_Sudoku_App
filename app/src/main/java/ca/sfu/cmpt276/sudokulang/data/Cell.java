package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;

public interface Cell {
    /**
     * @return Value of the cell, 0 if it's empty.
     */
    int getValue();

    /**
     * @return Text of the cell, "" if it's empty.
     */
    @NonNull
    String getText();

    boolean isPrefilled();

    boolean isErrorCell();

    boolean isEmpty();
}