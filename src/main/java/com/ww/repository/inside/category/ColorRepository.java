package com.ww.repository.inside.category;

import com.ww.model.entity.inside.task.Color;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends CrudRepository<Color, Long> {
    List<Color> findAll();
}
