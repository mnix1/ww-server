package com.ww.repository.outside.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeProfileRepository extends CrudRepository<ChallengeProfile, Long> {
    List<ChallengeProfile> findAllByProfile_IdAndStatusAndChallenge_Status(Long profileId, ChallengeProfileStatus challengeProfileStatus, ChallengeStatus challengeStatus);
    List<ChallengeProfile> findAllByProfile_IdAndChallenge_Status(Long profileId, ChallengeStatus challengeStatus);
    ChallengeProfile findByProfile_IdAndChallenge_Id(Long profileId, Long challengeId);
    List<ChallengeProfile> findAllByChallenge_Id(Long challengeId);
}
