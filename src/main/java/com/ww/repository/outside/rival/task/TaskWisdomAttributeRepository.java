package com.ww.repository.outside.rival.task;

import com.ww.model.entity.outside.rival.task.TaskType;
import com.ww.model.entity.outside.rival.task.TaskWisdomAttribute;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TaskWisdomAttributeRepository extends CrudRepository<TaskWisdomAttribute, Long> {
    @Cacheable("taskWisdomAttributeByType")
    public Set<TaskWisdomAttribute> findAllByType(TaskType taskType);

}
