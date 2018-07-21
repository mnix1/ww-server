package com.ww.repository.rival.task.category;

import com.ww.model.entity.rival.task.TaskColor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskColorRepository extends CrudRepository<TaskColor, Long> {
    List<TaskColor> findAll();
}
