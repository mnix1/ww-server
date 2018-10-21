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
    private String content;
    private Long gold;
    private Long crystal;
    private Long wisdom;
    private Long elixir;

    public ProfileMailDTO(ProfileMail profileMail) {
        this.id = profileMail.getId();
        this.type = profileMail.getType();
        this.displayed = profileMail.getDisplayed();
        this.claimed = profileMail.getClaimed();
        this.hasResources = profileMail.getHasResources();
        this.creationDate = profileMail.getCreationDate();
        this.title = profileMail.getTitle();
        this.content = profileMail.getContent();
        this.gold = profileMail.getGoldGain();
        this.crystal = profileMail.getCrystalGain();
        this.wisdom = profileMail.getWisdomGain();
        this.elixir = profileMail.getElixirGain();
    }
}
