package com.ww.repository.outside.social;

import com.ww.model.entity.outside.social.ProfileIntro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileIntroRepository extends CrudRepository<ProfileIntro, Long> {
    Optional<ProfileIntro> findByProfile_Id(Long profileId);
}
