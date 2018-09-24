package com.ww.repository.outside.rival.challenge;

import com.ww.model.entity.outside.rival.challenge.Challenge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends CrudRepository<Challenge, Long> {
}
