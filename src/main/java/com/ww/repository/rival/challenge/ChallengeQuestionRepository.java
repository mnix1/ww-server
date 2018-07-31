package com.ww.repository.rival.challenge;

import com.ww.model.entity.rival.challenge.ChallengeQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeQuestionRepository extends CrudRepository<ChallengeQuestion, Long> {

}
