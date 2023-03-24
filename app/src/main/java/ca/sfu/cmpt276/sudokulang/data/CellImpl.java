package ca.sfu.cmpt276.sudokulang.data;

public class CellImpl implements Cell {
    private int mValue;
    private boolean mIsPrefilled, mIsErrorCell;

    /**
     * @implNote Default state:         <p>
     * {@link #getValue()}    == 0      <p>
     * {@link #isPrefilled()} == false  <p>
     * {@link #isErrorCell()} == false
     */
    public CellImpl() {
        this(0, false, false);
    }

    public CellImpl(int value, boolean isPrefilled, boolean isErrorCell) {
        if (isPrefilled && isErrorCell) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mValue = value;
        mIsPrefilled = isPrefilled;
        mIsErrorCell = isErrorCell;
    }

    @Override
    public int getValue() {
        return mValue;
    }

    public CellImpl setValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Cell value cannot be negative");
        }
        mValue = value;
        return this;
    }

    @Override
    public boolean isPrefilled() {
        return mIsPrefilled;
    }

    public CellImpl setPrefilled(boolean isPrefilled) {
        if (isPrefilled && mIsErrorCell) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mIsPrefilled = isPrefilled;
        return this;
    }

    @Override
    public boolean isErrorCell() {
        return mIsErrorCell;
    }

    public CellImpl setErrorCell(boolean isErrorCell) {
        if (mIsPrefilled && isErrorCell) {
            throw new IllegalArgumentException("Prefilled cells cannot be error cells");
        }
        mIsErrorCell = isErrorCell;
        return this;
    }

    @Override
    public boolean isEmpty() {
        return mValue == 0;
    }
}