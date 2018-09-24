package com.ww.repository.outside.social;

import com.ww.model.entity.outside.social.Auto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutoRepository extends CrudRepository<Auto, Long> {
    Optional<Auto> findFirstByUsername(String user);
}
