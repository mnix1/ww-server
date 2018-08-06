package com.ww.model.entity.social;

import com.ww.helper.TagHelper;
import com.ww.model.constant.HeroType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private HeroType heroType;
    private Long level;
    private Long experience;
    private Long wisdomPoint;
    private Long diamond;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileFriend> friends;

    public Profile(String authId) {
        this.tag = TagHelper.randomTag();
        this.name = authId;
        this.authId = authId;
        this.level = 0L;
        this.experience = 0L;
        this.wisdomPoint = 0L;
        this.diamond = 0L;
        this.heroType = HeroType.random();
    }

    public Profile(Long id) {
        this.id = id;
    }

    public Profile(String tag, String name, Long level) {
        this.tag = tag;
        this.name = name;
        this.level = level;
        this.heroType = HeroType.random();
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Profile) obj).id);
    }
}
