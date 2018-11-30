package com.ww.model.entity.outside.social;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileIntro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer introductionStepIndex;
    private Integer shopStepIndex;
    private Integer profileStepIndex;
    private Integer warStepIndex;
    private Integer battleStepIndex;
    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    protected Profile profile;

    public ProfileIntro(Profile profile) {
        this.profile = profile;
        this.introductionStepIndex = 0;
        this.shopStepIndex = 0;
        this.profileStepIndex = 0;
        this.warStepIndex = 0;
        this.battleStepIndex = 0;
    }
}
