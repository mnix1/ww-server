package com.ww.model.entity.social;

import com.ww.helper.TagHelper;
import com.ww.model.constant.social.Avatar;
import com.ww.model.entity.rival.task.ProfileQuestion;
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
    private Avatar avatar;
    private Long level;
    private Long experience;
    private Long wisdomPoint;
    private Long diamond;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileFriend> friends;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private Set<ProfileQuestion> questions;

    public Profile(String authId) {
        this.tag = TagHelper.randomTag();
        this.name = authId;
        this.authId = authId;
        this.level = 0L;
        this.experience = 0L;
        this.wisdomPoint = 0L;
        this.diamond = 0L;
        this.avatar = Avatar.random();
    }

    public Profile(Long id) {
        this.id = id;
    }

    public Profile(String tag, String name, Long level) {
        this.tag = tag;
        this.name = name;
        this.level = level;
        this.avatar = Avatar.random();
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Profile) obj).id);
    }
}
