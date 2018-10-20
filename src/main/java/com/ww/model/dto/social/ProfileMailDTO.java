package com.ww.model.dto.social;

import com.ww.model.constant.social.MailType;
import com.ww.model.entity.outside.social.ProfileMail;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ProfileMailDTO {

    private Long id;
    private MailType type;
    private Boolean displayed;
    private Boolean claimed;
    private Boolean hasResources;
    private Instant creationDate;
    private String title;
    private String htmlContent;
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;

    public ProfileMailDTO(ProfileMail profileMail) {
        this.id = profileMail.getId();
        this.type = profileMail.getType();
        this.displayed = profileMail.getDisplayed();
        this.claimed = profileMail.getClaimed();
        this.hasResources = profileMail.getHasResources();
        this.creationDate = profileMail.getCreationDate();
        this.title = profileMail.getTitle();
        this.htmlContent = profileMail.getContent();
        this.goldGain = profileMail.getGoldGain();
        this.crystalGain = profileMail.getCrystalGain();
        this.wisdomGain = profileMail.getWisdomGain();
        this.elixirGain = profileMail.getElixirGain();
    }
}
