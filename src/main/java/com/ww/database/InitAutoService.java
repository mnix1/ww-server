package com.ww.database;

import com.ww.model.entity.inside.social.Auto;
import com.ww.repository.inside.social.AutoRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Service
public class InitAutoService {

    @Autowired
    private AutoRepository autoRepository;

    public void initAutos() {
        List<Auto> autos = new ArrayList<>();
        autos.add(new Auto("admin1992", "$2a$12$IZ.dAL3M92Q6XEk9K1YpY.878FBFZZb7qw.oFmXkre7aq8435Oa7u", true));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        autos.add(new Auto("Murro", encoder));
        autos.add(new Auto("Rambo", encoder));
        autos.add(new Auto("Krejzol", encoder));
        autos.add(new Auto("Tom", encoder));
        autos.add(new Auto("qtoś", encoder));
        autos.add(new Auto("rapid", encoder));
        autos.add(new Auto("Stefan", encoder));
        autos.add(new Auto("Paździoch", encoder));
        autos.add(new Auto("mistrzu", encoder));
        autos.add(new Auto("łuki", encoder));
        autos.add(new Auto("szybkiPL", encoder));
        autos.add(new Auto("fred", encoder));
        autos.add(new Auto("wredny89", encoder));
        autos.add(new Auto("sympatyk futbolu 82", encoder));
        autos.add(new Auto("niewiadomy", encoder));
        autos.add(new Auto("samuel L", encoder));
        autos.add(new Auto("bandzior", encoder));
        autos.add(new Auto("karaTEKA", encoder));
        autos.add(new Auto("AJGOR", encoder));
        autos.add(new Auto("SMERFny", encoder));
        autos.add(new Auto("bond007", encoder));
        autos.add(new Auto("strike", encoder));
        autos.add(new Auto("UWAŻNY", encoder));
        autos.add(new Auto("suzuki", encoder));
        autos.add(new Auto("Robert", encoder));
        autos.add(new Auto("qwerty", encoder));
        autos.add(new Auto("gdańszcznin123", encoder));
        autos.add(new Auto("niepoważny2002", encoder));
        autoRepository.saveAll(autos);
    }

}
