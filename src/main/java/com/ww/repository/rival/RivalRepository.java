package com.ww.repository.rival;

import com.ww.model.entity.rival.Rival;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RivalRepository extends CrudRepository<Rival, Long> {
}
