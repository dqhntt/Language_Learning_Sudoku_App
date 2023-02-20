package ca.sfu.cmpt276.sudokulang.ui.game.board;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * State holder for SudokuCell UI element.
 *
 * @implNote Default state: <p>
 * Row index = -1.          <p>
 * Column index = -1.       <p>
 * Is user fillable.        <p>
 * Is not an error cell.
 */
public class SudokuCellViewModel extends ViewModel {
    private final int mRowIndex, mColIndex;
    private final MutableLiveData<Boolean> mIsPrefilled, mIsErrorCell;
    private final MutableLiveData<String> mText;

    SudokuCellViewModel() {
        this(-1, -1);
    }

    SudokuCellViewModel(int rowIndex, int colIndex) {
        this("", false, false, rowIndex, colIndex);
    }

    SudokuCellViewModel(@NonNull String text, boolean prefilled, boolean isErrorCell) {
        this(text, prefilled, isErrorCell, -1, -1);
    }

    private SudokuCellViewModel(@NonNull String text, boolean prefilled, boolean isErrorCell, int rowIndex, int colIndex) {
        super();
        mText = new MutableLiveData<>();
        mIsPrefilled = new MutableLiveData<>();
        mIsErrorCell = new MutableLiveData<>();
        mRowIndex = rowIndex;
        mColIndex = colIndex;
        setProperties(text, prefilled, isErrorCell);
    }

    void setProperties(@NonNull String text, boolean prefilled, boolean isErrorCell) {
        if (prefilled && isErrorCell) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        setText(text);
        mIsPrefilled.setValue(prefilled);
        mIsErrorCell.setValue(isErrorCell);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(@NonNull String value) {
        mText.setValue(value);
    }

    public int getRowIndex() {
        return mRowIndex;
    }

    public int getColIndex() {
        return mColIndex;
    }

    public LiveData<Boolean> isErrorCell() {
        return mIsErrorCell;
    }

    public void setAsErrorCell(boolean isErrorCell) {
        if (isErrorCell && mIsPrefilled.getValue()) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mIsErrorCell.setValue(isErrorCell);
    }

    public LiveData<Boolean> isPrefilled() {
        return mIsPrefilled;
    }

    public void setPrefilled(boolean prefilled) {
        if (prefilled && mIsErrorCell.getValue()) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mIsPrefilled.setValue(prefilled);
    }
}
