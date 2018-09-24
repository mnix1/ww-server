package com.ww.repository.inside.category;

import com.ww.model.entity.inside.task.Element;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementRepository extends CrudRepository<Element, Long> {
    List<Element> findAll();
}
