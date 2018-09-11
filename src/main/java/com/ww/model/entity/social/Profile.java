package com.ww.model.entity.social;

import com.ww.helper.TagHelper;
import com.ww.model.constant.Language;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.entity.book.ProfileBook;
import com.ww.model.entity.rival.campaign.ProfileCampaign;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.language.bm.Lang;

import javax.persistence.*;
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
    private Language language;
    private Long level;
    private Long experience;
    private Long gold;
    private Long crystal;
    private Long wisdom;
    private Long elixir;
    private Boolean teamInitialized;
    private Long battleElo;
    private Long warElo;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileFriend> friends = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileWisie> wisies = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileBook> books = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileCampaign> campaigns = new HashSet<>();

    public Profile(String authId, String name, Language language) {
        this.tag = TagHelper.randomTag();
        this.name = name;
        this.authId = authId;
        this.language = language;
        this.level = 0L;
        this.experience = 0L;
        this.gold = 10L;
        this.crystal = 10L;
        this.wisdom = 0L;
        this.elixir = 0L;
        this.wisorType = WisorType.random();
        this.teamInitialized = false;
        this.battleElo = 0L;
        this.warElo = 0L;
    }

    public Profile(Long id) {
        this.id = id;
    }

    public Profile(String tag, String name, Long level) {
        this.tag = tag;
        this.name = name;
        this.level = level;
        this.wisorType = WisorType.random();
    }

    public void changeResources(Long gold, Long crystal, Long wisdom, Long elixir) {
        if (gold != null) {
            this.gold += gold;
        }
        if (crystal != null) {
            this.crystal += crystal;
        }
        if (wisdom != null) {
            this.wisdom += wisdom;
        }
        if (elixir != null) {
            this.elixir += elixir;
        }
    }

    public boolean hasEnoughResources(Long gold, Long crystal, Long wisdom, Long elixir) {
        if (gold != null && this.gold < gold) {
            return false;
        }
        if (crystal != null && this.crystal < crystal) {
            return false;
        }
        if (wisdom != null && this.wisdom < wisdom) {
            return false;
        }
        if (elixir != null && this.elixir < elixir) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Profile) obj).id);
    }
}
