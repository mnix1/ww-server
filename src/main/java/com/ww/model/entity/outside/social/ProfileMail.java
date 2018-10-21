package com.ww.model.entity.outside.social;

import com.ww.model.constant.social.MailType;
import com.ww.model.container.Resources;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileMail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private MailType type;
    private Boolean displayed = false;
    private Boolean claimed = false;
    private Boolean hasResources;
    private Instant creationDate = Instant.now();
    @Column(length = 4000)
    private String title;
    @Column(length = 4000)
    private String content;
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    protected Profile profile;

    public ProfileMail(Profile profile, Resources resources, String title, String content) {
        this.hasResources = true;
        this.profile = profile;
        this.title = title;
        this.content = content;
        setGainResources(resources);
    }

    public ProfileMail(Profile profile, MailType type, Resources resources, String content) {
        this.hasResources = true;
        this.profile = profile;
        this.type = type;
        this.content = content;
        setGainResources(resources);
    }

    public ProfileMail(Profile profile, String title, String content) {
        this.hasResources = false;
        this.profile = profile;
        this.title = title;
        this.content = content;
    }

    public ProfileMail(Profile profile, MailType type, String content) {
        this.hasResources = false;
        this.profile = profile;
        this.type = type;
        this.content = content;
    }

    public void setGainResources(Resources resources) {
        this.goldGain = resources.getGold();
        this.crystalGain = resources.getCrystal();
        this.wisdomGain = resources.getWisdom();
        this.elixirGain = resources.getElixir();
    }

    public Resources getGainResources() {
        return new Resources(goldGain, crystalGain, wisdomGain, elixirGain);
    }

}
