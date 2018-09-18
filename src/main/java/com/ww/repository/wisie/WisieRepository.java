package com.ww.repository.wisie;

import com.ww.model.constant.wisie.WisieType;
import com.ww.model.entity.wisie.Wisie;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WisieRepository extends CrudRepository<Wisie, Long> {
    @Cacheable("allWisies")
    List<Wisie> findAll();

    Wisie findByType(WisieType type);
    List<Wisie> findByTypeIn(List<WisieType> types);
}
