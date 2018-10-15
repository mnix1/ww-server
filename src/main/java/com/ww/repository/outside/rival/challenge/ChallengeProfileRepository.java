package com.ww.repository.outside.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileType;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import org.apache.xpath.operations.Bool;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeProfileRepository extends CrudRepository<ChallengeProfile, Long> {
    List<ChallengeProfile> findAllByProfile_IdAndJoinedAndChallenge_StatusOrderByChallenge_CloseDateDesc(Long profileId, Boolean joined, ChallengeStatus status);

    List<ChallengeProfile> findAllByProfile_IdAndChallenge_Status(Long profileId, ChallengeStatus challengeStatus);
    Optional<ChallengeProfile> findByProfile_IdAndChallenge_Id(Long profileId, Long challengeId);
    List<ChallengeProfile> findAllByChallenge_Id(Long challengeId);

    List<ChallengeProfile> findAllByProfile_IdAndChallenge_IdIn(Long profileId, List<Long> challengeIds);


}
