package com.ww.model.dto.rival.campaign;

import com.ww.model.constant.book.BookType;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.dto.rival.TeamMemberDTO;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.TeamHelper.preparePresentIndexes;
import static com.ww.helper.TeamHelper.prepareTeamMembers;

@Getter
public class ProfileCampaignDTO {

    private Long id;
    private Integer phase;
    private Integer phases;
    private CampaignType type;
    private CampaignDestination destination;
    private List<TeamMemberDTO> team;
    private List<Integer> presentIndexes;
    private ProfileCampaignStatus status;
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
    private BookType bookGain;

    public ProfileCampaignDTO(ProfileCampaign profileCampaign) {
        this.id = profileCampaign.getId();
        this.phase = profileCampaign.getPhase();
        this.phases = profileCampaign.getCampaign().getPhases();
        this.type = profileCampaign.getCampaign().getType();
        this.destination = profileCampaign.getCampaign().getDestination();
        this.status = profileCampaign.getStatus();
        if (this.status == ProfileCampaignStatus.FINISHED) {
            this.goldGain = profileCampaign.getGoldGain();
            this.crystalGain = profileCampaign.getCrystalGain();
            this.wisdomGain = profileCampaign.getWisdomGain();
            this.elixirGain = profileCampaign.getElixirGain();
            this.bookGain = profileCampaign.getBookGain();
        }
        initTeam(profileCampaign);
    }

    private void initTeam(ProfileCampaign profileCampaign) {
        List<TeamMember> teamMembers = prepareTeamMembers(profileCampaign.getProfile(), new ArrayList(profileCampaign.getWisies()), RivalImportance.FAST, RivalType.CAMPAIGN_WAR);
        this.team = teamMembers.stream()
                .map(TeamMemberDTO::new)
                .collect(Collectors.toList());
        this.presentIndexes = preparePresentIndexes(profileCampaign, teamMembers);
    }

    public Boolean getRewardNotEmpty(){
        return goldGain != null || crystalGain != null || wisdomGain != null || elixirGain != null || bookGain != null;
    }
}
