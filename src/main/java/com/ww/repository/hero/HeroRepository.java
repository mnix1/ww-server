package com.ww.repository.hero;

import com.ww.model.entity.hero.Hero;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroRepository extends CrudRepository<Hero, Long> {
    List<Hero> findAll();
}
