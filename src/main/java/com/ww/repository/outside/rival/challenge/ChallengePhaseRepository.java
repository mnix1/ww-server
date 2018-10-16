package com.ww.repository.outside.rival.challenge;

import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengePhaseRepository extends CrudRepository<ChallengePhase, Long> {
    List<ChallengePhase> findAllByChallenge_Id(Long challengeId);

}
