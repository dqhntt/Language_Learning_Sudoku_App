package ca.sfu.cmpt276.sudokulang.data.source;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.Language;
import ca.sfu.cmpt276.sudokulang.data.Word;

public interface WordRepository {

    void insert(Word word);

    void insert(List<Word> words);

    Language getLanguageByName(String languageName);
}