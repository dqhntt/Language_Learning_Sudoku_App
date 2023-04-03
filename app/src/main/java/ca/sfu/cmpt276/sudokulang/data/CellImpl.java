package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;

public class CellImpl implements Cell {
    @NonNull
    private String mText;
    private int mValue;
    private boolean mIsPrefilled, mIsErrorCell;

    /**
     * @implNote Default state:         <p>
     * {@link #getText()}     == ""     <p>
     * {@link #getValue()}    == 0      <p>
     * {@link #isPrefilled()} == false  <p>
     * {@link #isErrorCell()} == false
     */
    public CellImpl() {
        this(0, "", false, false);
    }

    private CellImpl(int value, @NonNull String text, boolean isPrefilled, boolean isErrorCell) {
        if (isPrefilled && isErrorCell) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mValue = value;
        mText = text;
        mIsPrefilled = isPrefilled;
        mIsErrorCell = isErrorCell;
    }

    @Override
    public int getValue() {
        return mValue;
    }

    @NonNull
    public CellImpl setValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Cell value cannot be negative");
        }
        mValue = value;
        return this;
    }

    @NonNull
    @Override
    public String getText() {
        return mText;
    }

    @NonNull
    public CellImpl setText(@NonNull String text) {
        mText = text;
        return this;
    }

    @Override
    public boolean isPrefilled() {
        assert !mIsPrefilled || !isEmpty(); // Empty cell can't be prefilled.
        return mIsPrefilled;
    }

    @NonNull
    public CellImpl setPrefilled(boolean isPrefilled) {
        if (isPrefilled && mIsErrorCell) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mIsPrefilled = isPrefilled;
        return this;
    }

    @Override
    public boolean isErrorCell() {
        assert !mIsErrorCell || !isEmpty(); // Empty cell can't be error cell.
        return mIsErrorCell;
    }

    @NonNull
    public CellImpl setErrorCell(boolean isErrorCell) {
        if (mIsPrefilled && isErrorCell) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mIsErrorCell = isErrorCell;
        return this;
    }

    @Override
    public boolean isEmpty() {
        assert mValue != 0 || mText.isBlank(); // Can't have empty text if value is 0.
        return mValue == 0;
    }

    @NonNull
    @Override
    public Cell clone() {
        try {
            return (CellImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}