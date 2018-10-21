package com.ww.model.entity.outside.social;

import com.ww.model.constant.social.ProfileActionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ProfileActionType type;
    private Instant actionDate = Instant.now();
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    protected Profile profile;

    public ProfileAction(ProfileActionType type, Profile profile) {
        this.type = type;
        this.profile = profile;
    }
}
