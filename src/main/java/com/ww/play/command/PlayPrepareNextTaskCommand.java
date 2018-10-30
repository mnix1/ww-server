package com.ww.play.command;

import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.play.container.PlayContainer;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.lang3.builder.Diff;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class PlayPrepareNextTaskCommand extends PlayCommand {
    private Category category;
    private DifficultyLevel difficultyLevel;
    private Language language;

    public PlayPrepareNextTaskCommand(PlayContainer container, Category category, DifficultyLevel difficultyLevel, Language language) {
        super(container);
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.language = language;
    }

    @Override
    public void execute() {
        container.getTasks().prepareNext(category, difficultyLevel, language);
    }
}
