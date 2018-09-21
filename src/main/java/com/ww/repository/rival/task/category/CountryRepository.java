package com.ww.repository.rival.task.category;

import com.ww.model.entity.rival.task.Country;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    @Cacheable("allCountries")
    List<Country> findAll();
}
