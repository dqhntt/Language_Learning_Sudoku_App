package ca.sfu.cmpt276.sudokulang.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class WordPair {
    @ColumnInfo(name = "translationId")
    private final int mTranslationId;

    @NonNull
    @ColumnInfo(name = "originalWord")
    private final String mOriginalWord;

    @NonNull
    @ColumnInfo(name = "translatedWord")
    private final String mTranslatedWord;

    public WordPair(int translationId, @NonNull String originalWord, @NonNull String translatedWord) {
        mTranslationId = translationId;
        mOriginalWord = originalWord;
        mTranslatedWord = translatedWord;
    }

    public int getTranslationId() {
        return mTranslationId;
    }

    @NonNull
    public String getOriginalWord() {
        return mOriginalWord;
    }

    @NonNull
    public String getTranslatedWord() {
        return mTranslatedWord;
    }
}