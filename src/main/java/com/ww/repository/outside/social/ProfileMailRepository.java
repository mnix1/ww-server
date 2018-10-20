package com.ww.repository.outside.social;

import com.ww.model.entity.outside.social.ProfileMail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileMailRepository extends CrudRepository<ProfileMail, Long> {
    List<ProfileMail> findAllByProfile_Id(Long profileId);
}
