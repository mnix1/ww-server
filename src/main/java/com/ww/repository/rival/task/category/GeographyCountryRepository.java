package com.ww.repository.rival.task.category;

import com.ww.model.entity.rival.task.GeographyCountry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeographyCountryRepository extends CrudRepository<GeographyCountry, Long> {
    List<GeographyCountry> findAll();
}
