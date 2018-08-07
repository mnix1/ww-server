package com.ww.repository.rival.task;

import com.ww.model.entity.rival.task.TaskWisdomAttribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskWisdomAttributeRepository extends CrudRepository<TaskWisdomAttribute, Long> {

}
