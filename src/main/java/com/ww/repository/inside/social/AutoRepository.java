package com.ww.repository.inside.social;

import com.ww.model.entity.inside.social.Auto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutoRepository extends CrudRepository<Auto, Long> {
    Optional<Auto> findFirstByUsername(String user);
    List<Auto> findAllByAuto(boolean auto);
}
