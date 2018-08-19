package com.ww.repository.rival.task.category;

import com.ww.model.entity.rival.task.Color;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends CrudRepository<Color, Long> {
    List<Color> findAll();
}
