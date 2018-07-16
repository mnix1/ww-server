package com.ww.repository.rival.task;

import com.ww.model.constant.Category;
import com.ww.model.entity.rival.task.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {

    List<Question> findAllByCategory(Category category);

//    @Query(value = "SELECT * FROM Question WHERE category=:category ORDER BY RAND() LIMIT 1", nativeQuery = true)
//    List<Question> findRandomByCategory(@Param("category") Category category);

    Question findByCategory(Category category);
}
