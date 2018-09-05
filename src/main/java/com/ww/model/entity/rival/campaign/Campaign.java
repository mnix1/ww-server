package com.ww.model.entity.rival.campaign;

import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.constant.rival.practise.PractiseResult;
import com.ww.model.entity.rival.task.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private CampaignType type;
    private Long goldCost;
    private Long crystalCost;
    private Long elixirCost;
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
}
