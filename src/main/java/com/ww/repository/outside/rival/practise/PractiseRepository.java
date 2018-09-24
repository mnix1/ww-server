package com.ww.repository.outside.rival.practise;

import com.ww.model.entity.outside.rival.practise.Practise;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PractiseRepository extends CrudRepository<Practise, Long> {
}
