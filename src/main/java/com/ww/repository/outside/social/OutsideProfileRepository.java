package com.ww.repository.outside.social;

import com.ww.model.entity.outside.social.OutsideProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OutsideProfileRepository extends CrudRepository<OutsideProfile, Long> {
    Optional<OutsideProfile> findFirstByUsernameOrEmail(String username, String email);
    Optional<OutsideProfile> findFirstByUsername(String username);
}
