package com.ww.model.entity.rival.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long profileId;
    private Long questionId;

    public ProfileQuestion(Long profileId, Long questionId) {
        this.profileId = profileId;
        this.questionId = questionId;
    }
}
