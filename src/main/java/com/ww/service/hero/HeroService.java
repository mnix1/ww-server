package com.ww.service.hero;

import com.ww.model.Session;
import com.ww.model.dto.hero.HeroDTO;
import com.ww.repository.hero.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HeroService {

    private Session session = new Session();

    @Autowired
    private HeroRepository heroRepository;

    public List<HeroDTO> list() {
        return heroRepository.findAll().stream().map(hero -> new HeroDTO(hero)).collect(Collectors.toList());
    }
}
