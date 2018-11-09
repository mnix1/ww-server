package com.ww.model.container.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import lombok.Getter;
import lombok.ToString;
import org.apache.xpath.operations.Bool;

@Getter
@ToString
public class RivalDecisions {
    protected Category category;
    protected boolean chosenCategory;
    protected DifficultyLevel difficultyLevel;
    protected boolean chosenDifficulty;

    protected Long answeredProfileId;
    protected Long chosenAnswerId;

    public void defaultTaskProps() {
        this.category = Category.RANDOM;
        this.chosenCategory = false;
        this.difficultyLevel = DifficultyLevel.NORMAL;
        this.chosenDifficulty = false;
    }

    public void chosenCategory(Category category) {
        this.category = category;
        this.chosenCategory = true;
    }

    public void chosenDifficulty(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        this.chosenDifficulty = true;
    }

    public void defaultAnswered() {
        this.answeredProfileId = null;
        this.chosenAnswerId = null;
    }

    public void answered(Long answeredProfileId, Long chosenAnswerId) {
        this.answeredProfileId = answeredProfileId;
        this.chosenAnswerId = chosenAnswerId;
    }


}
