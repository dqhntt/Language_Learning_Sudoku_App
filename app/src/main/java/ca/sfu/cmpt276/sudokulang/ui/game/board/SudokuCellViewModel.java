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
    private final MutableLiveData<Integer> mRowIndex, mColIndex;
    private final MutableLiveData<Boolean> mIsPrefilled, mIsErrorCell;
    private final MutableLiveData<String> mText;

    SudokuCellViewModel() {
        this("", false, false);
    }

    SudokuCellViewModel(@NonNull String text, boolean prefilled, boolean isErrorCell) {
        super();
        mRowIndex = new MutableLiveData<>(-1);
        mColIndex = new MutableLiveData<>(-1);
        mText = new MutableLiveData<>();
        mIsPrefilled = new MutableLiveData<>();
        mIsErrorCell = new MutableLiveData<>();
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

    public LiveData<Integer> getRowIndex() {
        return mRowIndex;
    }

    void setRowIndex(int rowIndex) {
        mRowIndex.setValue(rowIndex);
    }

    public LiveData<Integer> getColIndex() {
        return mColIndex;
    }

    void setColIndex(int colIndex) {
        mColIndex.setValue(colIndex);
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
