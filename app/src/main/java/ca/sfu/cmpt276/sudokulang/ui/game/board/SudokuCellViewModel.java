package ca.sfu.cmpt276.sudokulang.ui.game.board;

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

    SudokuCellViewModel(String text, boolean prefilled, boolean isErrorCell) {
        super();
        mRowIndex = new MutableLiveData<>(-1);
        mColIndex = new MutableLiveData<>(-1);
        mText = new MutableLiveData<>(text);
        mIsPrefilled = new MutableLiveData<>(prefilled);
        mIsErrorCell = new MutableLiveData<>(isErrorCell);
    }

    void setProperties(String text, boolean prefilled, boolean isErrorCell) {
        setText(text);
        setPrefilled(prefilled);
        setAsErrorCell(isErrorCell);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String value) {
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
        if (isErrorCell) {
            assert (!mIsPrefilled.getValue());
        }
        mIsErrorCell.setValue(isErrorCell);
    }

    public LiveData<Boolean> isPrefilled() {
        return mIsPrefilled;
    }

    public void setPrefilled(boolean prefilled) {
        if (mIsErrorCell.getValue()) {
            assert (!prefilled);
        }
        mIsPrefilled.setValue(prefilled);
    }
}
