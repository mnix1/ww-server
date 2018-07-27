package com.ww.repository.social;

import com.ww.model.entity.social.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Profile findByAuthId(String authId);

    Profile findByTag(String tag);

    List<Profile> findAllByIdNotIn(List<Long> ids);
    List<Profile> findAll();
}
