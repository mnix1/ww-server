package com.ww.repository.rival.task.category;

import com.ww.model.entity.rival.task.Element;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementRepository extends CrudRepository<Element, Long> {
    List<Element> findAll();
}
