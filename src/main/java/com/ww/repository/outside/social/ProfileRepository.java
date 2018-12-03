package com.ww.repository.outside.social;

import com.ww.model.entity.outside.social.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Optional<Profile> findByAuthId(String authId);

    Profile findByTag(String tag);

    List<Profile> findAllByIdNotIn(List<Long> ids);

    List<Profile> findAll();
    List<Profile> findAllByAuthIdContains(String authId);
}
