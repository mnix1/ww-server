package com.ww.repository.inside.category;

import com.ww.model.constant.Language;
import com.ww.model.entity.inside.task.Track;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends CrudRepository<Track, Long> {
    List<Track> findAllByLang(Language lang);
}
