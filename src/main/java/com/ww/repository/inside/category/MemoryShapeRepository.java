package com.ww.repository.inside.category;

import com.ww.model.entity.inside.task.MemoryShape;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryShapeRepository extends CrudRepository<MemoryShape, Long> {
    List<MemoryShape> findAll();
}
