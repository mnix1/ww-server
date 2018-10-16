package com.ww.repository.outside.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.constant.rival.challenge.ChallengeType;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeProfileRepository extends CrudRepository<ChallengeProfile, Long> {
    List<ChallengeProfile> findAllByProfile_IdAndChallenge_TypeAndJoinedAndChallenge_StatusOrderByChallenge_CloseDateDesc(Long profileId, ChallengeType type, Boolean joined, ChallengeStatus status);

    List<ChallengeProfile> findAllByProfile_IdAndChallenge_TypeAndChallenge_Status(Long profileId, ChallengeType type, ChallengeStatus status);
    Optional<ChallengeProfile> findByProfile_IdAndChallenge_Id(Long profileId, Long challengeId);
    List<ChallengeProfile> findAllByChallenge_Id(Long challengeId);
    List<ChallengeProfile> findAllByChallenge_IdAndResponseStatus(Long challengeId, ChallengeProfileResponse responseStatus);

    List<ChallengeProfile> findAllByProfile_IdAndChallenge_IdIn(Long profileId, List<Long> challengeIds);


}
