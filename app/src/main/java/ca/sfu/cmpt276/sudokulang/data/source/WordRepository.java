package ca.sfu.cmpt276.sudokulang.data.source;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Word;

public interface WordRepository {
    long getIdOfWord(String word);

    void insert(Word word);

    void insert(List<Word> words);

    long getIdOfLanguage(String language);

    long getIdOfLanguageLevel(String languageLevel);
}