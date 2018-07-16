package com.ww.repository.rival.practise;

import com.ww.model.entity.rival.practise.PractiseQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PractiseQuestionRepository extends CrudRepository<PractiseQuestion, Long> {

}
