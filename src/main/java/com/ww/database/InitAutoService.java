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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(8);
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
        autos.add(new Auto("roxi", encoder));
        autos.add(new Auto("dżery", encoder));
        autos.add(new Auto("stanley", encoder));
        autos.add(new Auto("Piotruś55", encoder));
        autos.add(new Auto("walet", encoder));
        autos.add(new Auto("florence", encoder));
        autos.add(new Auto("giewont88", encoder));
        autos.add(new Auto("kris", encoder));
        autos.add(new Auto("ooolelele09", encoder));
        autos.add(new Auto("jejku88", encoder));
        autos.add(new Auto("znowu przegrałem", encoder));
        autos.add(new Auto("spoczi7", encoder));
        autos.add(new Auto("Pioter", encoder));
        autos.add(new Auto("Adrian", encoder));
        autos.add(new Auto("edzio listonosz", encoder));
        autos.add(new Auto("Ferdek", encoder));
        autos.add(new Auto("Halyna", encoder));
        autos.add(new Auto("Grażka", encoder));
        autos.add(new Auto("urwał", encoder));
        autos.add(new Auto("kolos20", encoder));
        autos.add(new Auto("mati", encoder));
        autos.add(new Auto("krzysio10", encoder));
        autos.add(new Auto("robercik", encoder));
        autos.add(new Auto("foka84", encoder));
        autos.add(new Auto("Łysy", encoder));
        autos.add(new Auto("John", encoder));
        autos.add(new Auto("mork", encoder));
        autos.add(new Auto("xander", encoder));
        autos.add(new Auto("Olek", encoder));
        autos.add(new Auto("Lucja11", encoder));
        autos.add(new Auto("szczęśliwa13", encoder));
        autos.add(new Auto("dorsz na obiad", encoder));
        autos.add(new Auto("odcedzone ziemniaki", encoder));
        autos.add(new Auto("leśna ściółka", encoder));
        autos.add(new Auto("smartTV", encoder));
        autos.add(new Auto("NoThanks", encoder));
        autos.add(new Auto("chrupki", encoder));
        autos.add(new Auto("dżony", encoder));
        autos.add(new Auto("gargamel", encoder));
        autos.add(new Auto("Johnny Cage", encoder));
        autos.add(new Auto("Rocky Balboa", encoder));
        autos.add(new Auto("Adam Małysz", encoder));
        autos.add(new Auto("Andrzej Gołota", encoder));
        autos.add(new Auto("Włodzimierz", encoder));
        autos.add(new Auto("Vladimir", encoder));
        autos.add(new Auto("Witalij", encoder));
        autos.add(new Auto("Sasha", encoder));
        autos.add(new Auto("Dawidek8", encoder));
        autos.add(new Auto("Joasia", encoder));
        autos.add(new Auto("Nina", encoder));
        autos.add(new Auto("różdżka", encoder));
        autos.add(new Auto("uzurpator podium", encoder));
        autos.add(new Auto("AK47", encoder));
        autos.add(new Auto("co to jest", encoder));
        autos.add(new Auto("tajnyAgent", encoder));
        autos.add(new Auto("lokator", encoder));
        autos.add(new Auto("romek", encoder));
        autos.add(new Auto("kolorowy", encoder));
        autos.add(new Auto("pani Zofia", encoder));
        autos.add(new Auto("Szanowny Ziomek", encoder));
        autos.add(new Auto("mieszkaniec metropolii", encoder));
        autos.add(new Auto("Jankes", encoder));
        autoRepository.saveAll(autos);
    }

}
