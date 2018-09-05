package com.ww.database;

import com.ww.model.constant.wisie.WisieType;
import com.ww.model.entity.wisie.Wisie;
import com.ww.repository.wisie.WisieRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Service
public class InitWisiesService {

    @Autowired
    private WisieRepository wisieRepository;

    public void initWisies() {
        List<Wisie> wisies = new ArrayList<>();
        wisies.add(new Wisie("Wilku", "Wolf", WisieType.WOLF));
        wisies.add(new Wisie("Skorupny", "Turtle", WisieType.TURTLE));
        wisies.add(new Wisie("Zdzigrys", "Tiger", WisieType.TIGER));
        wisies.add(new Wisie("Jaduś", "Snake", WisieType.SNAKE));
        wisies.add(new Wisie("Szopuś", "Raccoon", WisieType.RACCOON));
        wisies.add(new Wisie("Wełnuś", "Sheep", WisieType.SHEEP));
        wisies.add(new Wisie("Ząbek", "Shark", WisieType.SHARK));
        wisies.add(new Wisie("Kicek", "Rabbit", WisieType.RABBIT));
        wisies.add(new Wisie("Misiaczek", "Polar Bear", WisieType.POLAR_BEAR));
        wisies.add(new Wisie("Zgapka", "Parrot", WisieType.PARROT));
        wisies.add(new Wisie("Pandziu", "Panda", WisieType.PANDA_EAT));
        wisies.add(new Wisie("Strusior", "Ostrich", WisieType.OSTRICH));
        wisies.add(new Wisie("Bujnogrzyw", "Lion", WisieType.LION));
        wisies.add(new Wisie("Skoczka", "Kangaroo", WisieType.KANGAROO));
        wisies.add(new Wisie("Rumo", "Horse", WisieType.HORSE));
        wisies.add(new Wisie("Goruś", "Gorilla", WisieType.GORILLA));
        wisies.add(new Wisie("Lizuś", "Fox", WisieType.FOX_MAN));
        wisies.add(new Wisie("Lisiczka", "Foxie", WisieType.FOX));
        wisies.add(new Wisie("Trąbcia", "Elephant", WisieType.ELEPHANT));
        wisies.add(new Wisie("Orłuś", "Eagle", WisieType.EAGLE));
        wisies.add(new Wisie("Grubełło", "Fat Dragon", WisieType.DRAGON_FAT));
        wisies.add(new Wisie("Supełło", "Blue Dragon", WisieType.DRAGON_BLUE));
        wisies.add(new Wisie("Pikełło", "Green Dragon", WisieType.DRAGON));
        wisies.add(new Wisie("Pulszek", "Fat Dog", WisieType.DOG_FAT));
        wisies.add(new Wisie("Bystruś", "Idea Dog", WisieType.DOG));
        wisies.add(new Wisie("Kroczek", "Crocodile", WisieType.CROCODILE));
        wisies.add(new Wisie("Kicia", "Cat Teacher", WisieType.CAT_TEACHER));
        wisies.add(new Wisie("Kituś", "Apple Cat", WisieType.CAT_PRESENTER));
        wisies.add(new Wisie("Mruczka", "Kitty", WisieType.CAT_BLUE));
        wisies.add(new Wisie("Wielobłąd", "Camel", WisieType.CAMEL));
        wisies.add(new Wisie("Pudziuś", "Bulldog", WisieType.BULLDOG));
        wisies.add(new Wisie("Byku", "Bull", WisieType.BULL));
        wisies.add(new Wisie("Dźwiedzior", "Bear", WisieType.BEAR));
        wisies.add(new Wisie("Żądłolot", "Bee", WisieType.BEE));
        wisies.add(new Wisie("Żubrowar", "Aurochs", WisieType.AUROCHS));
        wisies.add(new Wisie("Mrówkacz", "Ant", WisieType.ANT));
        wisies.add(new Wisie("Pardzio", "Lampard", WisieType.LAMPARD));
        wisies.add(new Wisie("Smakełło", "Red Dragon", WisieType.DRAGON_RED));
        wisies.add(new Wisie("Słodzik", "Sweet Dog", WisieType.DOG_SWEET));
        wisies.add(new Wisie("Sowcia", "Owl", WisieType.OWL));
        wisies.add(new Wisie("Żabcia", "Frog", WisieType.FROG));
        wisies.add(new Wisie("Wiewcia", "Squirrel", WisieType.SQUIRREL));
        wisies.add(new Wisie("Pinguś", "Penguin", WisieType.PENGUIN));
        wisies.add(new Wisie("Morsu", "Walrus", WisieType.WALRUS));
        wisieRepository.saveAll(wisies);
    }

}
