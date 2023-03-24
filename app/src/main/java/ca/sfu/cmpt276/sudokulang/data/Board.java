package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface Board {
    int getBoardSize();

    int getSubgridHeight();

    int getSubgridWidth();

    int getSelectedRowIndex();

    int getSelectedColIndex();

    /**
     * @return The selected cell or {@code null} if none.
     */
    @Nullable
    Cell getSelectedCell();

    @NonNull
    Cell[][] getCells();

    boolean isSolvedBoard();
}