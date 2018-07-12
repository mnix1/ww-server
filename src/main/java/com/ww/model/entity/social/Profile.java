package com.ww.model.entity.social;

import com.ww.helper.TagHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private Integer level;
    private Integer experience;
    private Integer gold;
    private Integer diamond;

    public Profile(String authId) {
        this.tag = TagHelper.randomTag();
        this.authId = authId;
        this.level = 0;
        this.experience = 0;
        this.gold = 0;
        this.diamond = 0;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Profile) obj).id);
    }
}
