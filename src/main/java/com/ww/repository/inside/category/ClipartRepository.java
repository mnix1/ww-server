package com.ww.repository.inside.category;

import com.ww.model.entity.inside.task.Clipart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipartRepository extends CrudRepository<Clipart, Long> {
    List<Clipart> findAll();
}
