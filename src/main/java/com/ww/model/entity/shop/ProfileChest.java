package com.ww.model.entity.shop;


import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileChest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "chest_id", nullable = false, updatable = false)
    private Book book;

    public ProfileChest(Profile profile, Book book) {
        this.profile = profile;
        this.book = book;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((ProfileChest) obj).id);
    }

}
