package com.ww.repository.outside.rival;

import com.ww.model.entity.outside.rival.Rival;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RivalRepository extends CrudRepository<Rival, Long> {
}
