package com.ww.model.entity.rival.battle;

import com.ww.model.constant.rival.battle.BattleStatus;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creator_profile_id", nullable = false, updatable = false)
    private Profile creatorProfile;
    private BattleStatus status = BattleStatus.IN_PROGRESS;
    private Date openDate = new Date();
    private Date closeDate;

    @OneToMany(mappedBy = "battle", fetch = FetchType.LAZY)
    private Set<BattleProfile> profiles;

    @OneToMany(mappedBy = "battle", fetch = FetchType.LAZY)
    private Set<BattleQuestion> questions;

    @OneToMany(mappedBy = "battle", fetch = FetchType.LAZY)
    private Set<BattleAnswer> answers;



}
