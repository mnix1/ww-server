package com.ww.repository.inside.social;

import com.ww.model.entity.inside.social.InsideProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsideProfileRepository extends CrudRepository<InsideProfile, Long> {
    Optional<InsideProfile> findFirstByUsername(String user);
    List<InsideProfile> findAllByAuto(boolean auto);
}
