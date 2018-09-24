package com.ww.repository.outside.rival.task;

import com.ww.model.entity.outside.rival.task.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
}
