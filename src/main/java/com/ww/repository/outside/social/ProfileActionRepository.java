package com.ww.repository.outside.social;

import com.ww.model.entity.outside.social.ProfileAction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileActionRepository extends CrudRepository<ProfileAction, Long> {
}
