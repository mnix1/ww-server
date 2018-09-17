package com.ww.model.entity.rival;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

import static com.ww.manager.rival.campaign.CampaignWarManager.BOT_PROFILE_ID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Rival {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private RivalType type;
    private RivalImportance importance;
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false, updatable = false)
    private Profile creator;
    @ManyToOne
    @JoinColumn(name = "opponent_id", updatable = false)
    private Profile opponent;
    private Boolean draw;
    @ManyToOne
    @JoinColumn(name = "winner_id", updatable = false)
    private Profile winner;
    private Instant closeDate = Instant.now();

    public Rival(RivalType type, RivalImportance importance, Profile creator, Profile opponent, Boolean draw, Profile winner) {
        this.type = type;
        this.importance = importance;
        this.creator = creator;
        this.draw = draw;
        if (type != RivalType.CAMPAIGN_WAR && type != RivalType.CHALLENGE) {
            this.opponent = opponent;
        }
        if (winner != null && !winner.getId().equals(BOT_PROFILE_ID)) {
            this.winner = winner;
        }
    }
}
