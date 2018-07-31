package com.ww.model.entity.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creator_profile_id", nullable = false, updatable = false)
    private Profile creatorProfile;
    private ChallengeStatus status = ChallengeStatus.IN_PROGRESS;
    private Date inProgressDate = new Date();
    private Date closeDate;

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
    private Set<ChallengeProfile> profiles;

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
    private Set<ChallengeQuestion> questions;

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
    private Set<ChallengeAnswer> answers;



}
