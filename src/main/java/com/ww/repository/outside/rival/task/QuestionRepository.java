package com.ww.repository.outside.rival.task;

import com.ww.model.entity.outside.rival.task.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {
}
