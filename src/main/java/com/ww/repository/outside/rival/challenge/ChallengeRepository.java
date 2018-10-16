package com.ww.repository.outside.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeAccess;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.constant.rival.challenge.ChallengeType;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends CrudRepository<Challenge, Long> {
    Optional<Challenge> findFirstByTypeAndStatus(ChallengeType type, ChallengeStatus status);
    List<Challenge> findAllByStatusAndTimeoutDateLessThanEqual(ChallengeStatus status, Instant timeoutDate);
    List<Challenge> findAllByTypeAndStatusAndAccessIn(ChallengeType type, ChallengeStatus status, List<ChallengeAccess> accesses);
}
