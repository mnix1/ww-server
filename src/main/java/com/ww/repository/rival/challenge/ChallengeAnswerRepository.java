package com.ww.repository.rival.challenge;

import com.ww.model.entity.rival.challenge.ChallengeAnswer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeAnswerRepository extends CrudRepository<ChallengeAnswer, Long> {

}
