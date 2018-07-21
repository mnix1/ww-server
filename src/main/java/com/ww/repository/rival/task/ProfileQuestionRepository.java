package com.ww.repository.rival.task;

import com.ww.model.entity.rival.task.ProfileQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileQuestionRepository extends CrudRepository<ProfileQuestion, Long> {

    public ProfileQuestion findByProfileIdAndQuestionId(Long profileId, Long questionId);

}
