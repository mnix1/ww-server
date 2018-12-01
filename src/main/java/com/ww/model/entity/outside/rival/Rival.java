package com.ww.model.entity.outside.rival;

import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.helper.CompressHelper;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

import static com.ww.config.security.AuthIdProvider.isAutoProfile;
import static com.ww.config.security.AuthIdProvider.isHumanProfile;
import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Rival {
    public static boolean storeHumanInfo = true;
    public static boolean storeAutoInfo = false;
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
    @JoinColumn(name = "winner_id")
    private Profile winner;
    private Instant openDate;
    private Instant closeDate;
    @Lob
    private byte[] modelsJSONCompressed;
    @Lob
    private byte[] managerStringCompressed;

    public Rival(PlayContainer container) {
        this.openDate = Instant.now();
        this.type = container.getInit().getType();
        this.importance = container.getInit().getImportance();
        this.creator = container.getInit().getCreatorProfile();
        if (type != RivalType.CAMPAIGN_WAR && type != RivalType.CHALLENGE && importance != RivalImportance.TRAINING) {
            this.opponent = container.getInit().getOpponentProfile();
        }
    }

    public void update(PlayManager manager) {
        PlayContainer container = manager.getContainer();
        this.closeDate = Instant.now();
        this.draw = container.getResult().getDraw();
        Profile winner = container.getResult().getWinner();
        if (winner != null && !winner.getId().equals(BOT_PROFILE_ID)) {
            this.winner = winner;
        }
        if (storeInfo()) {
            this.modelsJSONCompressed = CompressHelper.compress(container.modelsToJSON());
            this.managerStringCompressed = CompressHelper.compress(manager.toString());
        }
    }

    private boolean storeInfo() {
        if (storeAutoInfo && (isAutoProfile(creator) || isAutoProfile(opponent))) {
            return true;
        }
        if (storeHumanInfo && (isHumanProfile(creator) || isHumanProfile(opponent))) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Rival{" +
                "id=" + id +
                ", type=" + type +
                ", importance=" + importance +
                ", creator=" + creator +
                ", opponent=" + opponent +
                ", draw=" + draw +
                ", winner=" + winner +
                ", openDate=" + openDate +
                ", closeDate=" + closeDate +
                '}';
    }
}
