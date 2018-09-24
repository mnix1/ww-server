package com.ww.repository.outside.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.entity.outside.rival.task.TaskType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTypeRepository extends CrudRepository<TaskType, Long> {

    List<TaskType> findAllByCategory(Category category);
}
