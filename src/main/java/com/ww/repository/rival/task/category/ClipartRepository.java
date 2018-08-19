package com.ww.repository.rival.task.category;

import com.ww.model.entity.rival.task.Clipart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClipartRepository extends CrudRepository<Clipart, Long> {
    List<Clipart> findAll();
}
