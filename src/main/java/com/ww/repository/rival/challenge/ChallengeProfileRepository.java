package com.ww.repository.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.entity.rival.challenge.ChallengeProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeProfileRepository extends CrudRepository<ChallengeProfile, Long> {
    List<ChallengeProfile> findAllByProfile_IdAndStatus(Long profileId, ChallengeProfileStatus status);
}
