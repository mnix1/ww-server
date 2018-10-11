package com.ww.model.entity.outside.social;

import com.ww.helper.TagHelper;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.book.ProfileBook;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String authId;
    private String tag;
    private String name;
    private WisorType wisorType;
    private Language language = Language.NONE;
    private Long level;
    private Long experience;
    private Long gold;
    private Long crystal;
    private Long wisdom;
    private Long elixir;
    private Boolean introductionCompleted;
    private Integer introductionStepIndex;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileFriend> friends = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileWisie> wisies = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileBook> books = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileCampaign> campaigns = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileSeason> seasons = new HashSet<>();

    public Profile(String authId, String name, Language language) {
        this.tag = TagHelper.randomTag();
        this.name = name;
        this.authId = authId;
        this.language = language;
        this.level = 0L;
        this.experience = 0L;
        this.gold = 10L;
        this.crystal = 40L;
        this.wisdom = 30L;
        this.elixir = 20L;
        this.wisorType = WisorType.random();
        this.introductionCompleted = false;
        this.introductionStepIndex = 0;
    }

    public Profile(Long id) {
        this.id = id;
    }

    public boolean hasEnoughResources(Resources resources) {
        return getResources().hasNotLessThan(resources);
    }

    public Resources getResources() {
        return new Resources(gold, crystal, wisdom, elixir);
    }

    public void setResources(Resources resources) {
        this.gold = resources.getGold();
        this.crystal = resources.getCrystal();
        this.wisdom = resources.getWisdom();
        this.elixir = resources.getElixir();
    }

    public Profile addResources(Resources resources) {
        setResources(getResources().add(resources));
        return this;
    }

    public void subtractResources(Resources resources) {
        setResources(getResources().subtract(resources));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        return id.equals(((Profile) obj).id);
    }
}
