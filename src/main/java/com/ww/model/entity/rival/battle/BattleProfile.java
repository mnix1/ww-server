package com.ww.model.entity.rival.battle;

import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class BattleProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BattleProfileStatus status = BattleProfileStatus.OPEN;
    private Date inProgressDate;
    private Date closeDate;
    @ManyToOne
    @JoinColumn(name = "battle_id", nullable = false, updatable = false)
    private Battle battle;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;

    public BattleProfile(Battle battle, Profile profile, BattleProfileStatus status) {
        this.battle = battle;
        this.profile = profile;
        this.status = status;
        if (status == BattleProfileStatus.IN_PROGRESS) {
            this.inProgressDate = new Date();
        }
    }

    public Long inProgressInterval() {
        return closeDate.getTime() - inProgressDate.getTime();
    }
}
