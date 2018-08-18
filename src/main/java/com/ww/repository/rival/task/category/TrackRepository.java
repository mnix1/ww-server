package com.ww.repository.rival.task.category;

import com.ww.model.constant.Language;
import com.ww.model.entity.rival.task.Track;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends CrudRepository<Track, Long> {
    List<Track> findAllByLang(Language lang);
}
