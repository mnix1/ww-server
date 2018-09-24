package com.ww.repository.outside.rival.challenge;

import com.ww.model.entity.outside.rival.challenge.ChallengeQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeQuestionRepository extends CrudRepository<ChallengeQuestion, Long> {
    List<ChallengeQuestion> findAllByChallenge_Id(Long challengeId);

}
