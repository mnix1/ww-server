package com.ww.repository.outside.rival;

import com.ww.model.entity.outside.rival.Rival;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RivalRepository extends CrudRepository<Rival, Long> {
    List<Rival> findAllByModelsJSONCompressedIsNotNull();
}
