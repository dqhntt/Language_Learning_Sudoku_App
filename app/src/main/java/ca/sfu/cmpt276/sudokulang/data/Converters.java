package ca.sfu.cmpt276.sudokulang.data;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.Locale;

// Cite: https://developer.android.com/training/data-storage/room/referencing-data
public class Converters {
    @TypeConverter
    public static Locale localeFromString(String value) {
        return new Locale(value);
    }

    @TypeConverter
    public static String localeToString(Locale locale) {
        return locale.getLanguage();
    }

    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static CellImpl[][] cellsFromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        final var cellVals = value.split(",");
        final int boardSize = (int) Math.sqrt(cellVals.length);
        if (cellVals.length != boardSize * boardSize) {
            throw new IllegalArgumentException("Board's not square");
        }
        final var cells = new CellImpl[boardSize][boardSize];
        for (int i = 0; i < cellVals.length; i++) {
            final var currVal = cellVals[i];
            final char firstChar = currVal.charAt(0);
            cells[i / boardSize][i % boardSize] = new CellImpl()
                    .setValue(Math.abs(Integer.parseInt(currVal)))
                    .setErrorCell(currVal.startsWith("-"))
                    .setPrefilled(firstChar != '0' && Character.isDigit(firstChar));
        }
        return cells;
    }

    /**
     * @implNote If cell is 2,        <p>
     * error & not prefilled: -2      <p>
     * not error & not prefilled: +2  <p>
     * not error & prefilled: 2       <p>
     * <p>
     * Can't have error and prefilled simultaneously. <p>
     * 0 if cell is empty.
     */
    @TypeConverter
    public static String cellsToString(Cell[][] cells) {
        if (cells == null) {
            return null;
        }
        final var values = new String[cells.length * cells[0].length];
        int i = 0;
        for (var row : cells) {
            for (var cell : row) {
                values[i] = (cell.isPrefilled() ? "" : cell.isErrorCell() ? "-" : "+")
                        + cell.getValue();
                i++;
            }
        }
        return String.join(",", values);
    }
}