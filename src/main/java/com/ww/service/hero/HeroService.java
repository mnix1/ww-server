package com.ww.service.hero;

import com.ww.model.dto.hero.HeroDTO;
import com.ww.model.entity.hero.Hero;
import com.ww.repository.hero.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class HeroService {

    @Autowired
    private HeroRepository heroRepository;

    public List<HeroDTO> list() {
        return heroRepository.findAll().stream()
                .map(HeroDTO::new)
                .collect(Collectors.toList());
    }

    public Hero random() {
        return randomElement(heroRepository.findAll());
    }

}
