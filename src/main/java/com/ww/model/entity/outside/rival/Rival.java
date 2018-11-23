package com.ww.model.entity.outside.rival;

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

import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Rival {
    public static boolean storeModel = true;
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

    public Rival(PlayContainer container) {
        this.openDate = Instant.now();
        this.type = container.getInit().getType();
        this.importance = container.getInit().getImportance();
        this.creator = container.getInit().getCreatorProfile();
        if (type != RivalType.CAMPAIGN_WAR && type != RivalType.CHALLENGE) {
            this.opponent = container.getInit().getOpponentProfile();
        }
    }

    public void update(PlayContainer container) {
        this.closeDate = Instant.now();
        this.draw = container.getResult().getDraw();
        Profile winner = container.getResult().getWinner();
        if (winner != null && !winner.getId().equals(BOT_PROFILE_ID)) {
            this.winner = winner;
        }
        if (storeModel) {
            this.modelsJSONCompressed = CompressHelper.compress(container.modelsToJSON());
        }
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
