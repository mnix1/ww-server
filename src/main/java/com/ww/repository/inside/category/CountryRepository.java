package com.ww.repository.inside.category;

import com.ww.model.entity.inside.task.Country;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    @Cacheable("allCountries")
    List<Country> findAll();
}
