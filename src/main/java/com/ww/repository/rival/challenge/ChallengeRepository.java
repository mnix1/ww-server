package com.ww.repository.rival.challenge;

import com.ww.model.entity.rival.challenge.Challenge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends CrudRepository<Challenge, Long> {

}
