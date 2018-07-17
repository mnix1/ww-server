package com.ww.repository.rival.task.category;

import com.ww.model.constant.Language;
import com.ww.model.entity.rival.task.category.MusicTrack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicTrackRepository extends CrudRepository<MusicTrack, Long> {
    List<MusicTrack> findAllByLang(Language lang);
}
