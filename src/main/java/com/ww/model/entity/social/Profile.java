package com.ww.model.entity.social;

import com.ww.helper.TagHelper;
import com.ww.model.entity.book.ProfileBook;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String wisorType;
    private Long level;
    private Long experience;
    private Long gold;
    private Long crystal;
    private Long wisdom;
    private Long elixir;
    private Boolean teamInitialized;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileFriend> friends = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileWisie> wisies = new HashSet<>();

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileBook> books = new HashSet<>();

    public Profile(String authId) {
        this.tag = TagHelper.randomTag();
        this.name = authId;
        this.authId = authId;
        this.level = 0L;
        this.experience = 0L;
        this.gold = 10L;
        this.crystal = 10L;
        this.wisdom = 0L;
        this.elixir = 0L;
        this.wisorType = "robot";
        this.teamInitialized = false;
    }

    public Profile(Long id) {
        this.id = id;
    }

    public Profile(String tag, String name, Long level) {
        this.tag = tag;
        this.name = name;
        this.level = level;
        this.wisorType = "robot";
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

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Profile) obj).id);
    }
}
