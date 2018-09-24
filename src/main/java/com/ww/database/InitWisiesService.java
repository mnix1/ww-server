package com.ww.database;

import com.ww.model.constant.wisie.WisieType;
import com.ww.model.entity.outside.wisie.Wisie;
import com.ww.repository.outside.wisie.WisieRepository;
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
        wisies.add(new Wisie("Ali", "Ali", WisieType.ALLIGATOR));
        wisies.add(new Wisie("Mrówkacz", "Anton", WisieType.ANT));
        wisies.add(new Wisie("Żubrowar", "Biz-on", WisieType.AUROCHS));
        wisies.add(new Wisie("Żądłolot", "Beellie", WisieType.BEE));
        wisies.add(new Wisie("Dźwiedzior", "Teddo", WisieType.BEAR));
        wisies.add(new Wisie("Byku", "Bullo", WisieType.BULL));
        wisies.add(new Wisie("Pudziuś", "Athlete", WisieType.BULLDOG));
        wisies.add(new Wisie("Wielobłąd", "Cameleoman", WisieType.CAMEL));
        wisies.add(new Wisie("Mruczka", "Frisky", WisieType.CAT_BLUE));
        wisies.add(new Wisie("Kituś", "Kit-O", WisieType.CAT_PRESENTER));
        wisies.add(new Wisie("Kicia", "Kitty", WisieType.CAT_TEACHER));
        wisies.add(new Wisie("Piskacz", "Shyriek", WisieType.CHICK));
        wisies.add(new Wisie("Kroczek", "Gator", WisieType.CROCODILE));
        wisies.add(new Wisie("Bystruś", "Smartie", WisieType.DOG));
        wisies.add(new Wisie("Pulszek", "Cuddle", WisieType.DOG_FAT));
        wisies.add(new Wisie("Słodzik", "Cutie", WisieType.DOG_SWEET));
        wisies.add(new Wisie("Pikełło", "Scary-Gary", WisieType.DRAGON));
        wisies.add(new Wisie("Supełło", "Kink-knot", WisieType.DRAGON_BLUE));
        wisies.add(new Wisie("Grubełło", "Chubbo", WisieType.DRAGON_FAT));
        wisies.add(new Wisie("Smakełło", "Foodie", WisieType.DRAGON_RED));
        wisies.add(new Wisie("Orłuś", "Tomeagle", WisieType.EAGLE));
        wisies.add(new Wisie("Trąbcia", "Bugber", WisieType.ELEPHANT));
        wisies.add(new Wisie("Słoniu", "Ellie", WisieType.ELEPHANT_CHILD));
        wisies.add(new Wisie("Lisiczka", "Foxy-Roxy", WisieType.FOX));
        wisies.add(new Wisie("Lizuś", "Brownosie", WisieType.FOX_MAN));
        wisies.add(new Wisie("Żabcia", "Missfroggie", WisieType.FROG));
        wisies.add(new Wisie("Rafcia", "Giralla", WisieType.GIRAFFE));
        wisies.add(new Wisie("Goruś", "Strongie", WisieType.GORILLA));
        wisies.add(new Wisie("Rumo", "Pony", WisieType.HORSE));
        wisies.add(new Wisie("Skoczka", "Cheerful", WisieType.KANGAROO));
        wisies.add(new Wisie("Kolo", "Bro", WisieType.KOALA));
        wisies.add(new Wisie("Pardzio", "Lurk", WisieType.LAMPARD));
        wisies.add(new Wisie("Bujnogrzyw", "Lux-mane", WisieType.LION));
        wisies.add(new Wisie("Mały", "Shortie", WisieType.MONKEY));
        wisies.add(new Wisie("Ośmiornik", "Octopush", WisieType.OCTOPUS));
        wisies.add(new Wisie("Strusior", "Ost-rich", WisieType.OSTRICH));
        wisies.add(new Wisie("Sowcia", "Owlie", WisieType.OWL));
        wisies.add(new Wisie("Mądruś", "Bighead", WisieType.OWL_HAPPY));
        wisies.add(new Wisie("Pandziu", "Pandice", WisieType.PANDA_EAT));
        wisies.add(new Wisie("Zgapka", "Sloppy", WisieType.PARROT));
        wisies.add(new Wisie("Pinguś", "Pengpong", WisieType.PENGUIN));
        wisies.add(new Wisie("Misiaczek", "Littlebear", WisieType.POLAR_BEAR));
        wisies.add(new Wisie("Kicek", "Kitter", WisieType.RABBIT));
        wisies.add(new Wisie("Szopuś", "Racco", WisieType.RACCOON));
        wisies.add(new Wisie("Wędrek", "Walker", WisieType.RACCOON_BROWN));
        wisies.add(new Wisie("Ząbek", "Bittero", WisieType.SHARK));
        wisies.add(new Wisie("Wełnuś", "Woolly", WisieType.SHEEP));
        wisies.add(new Wisie("Jaduś", "Snakiee", WisieType.SNAKE));
        wisies.add(new Wisie("Bociek", "Storkie", WisieType.STORK));
        wisies.add(new Wisie("Wiewcia", "Speedo", WisieType.SQUIRREL));
        wisies.add(new Wisie("Zdzigrys", "Petiger", WisieType.TIGER));
        wisies.add(new Wisie("Induś", "Turk", WisieType.TURKEY));
        wisies.add(new Wisie("Skorupny", "Shello", WisieType.TURTLE));
        wisies.add(new Wisie("Morsu", "Walrus", WisieType.WALRUS));
        wisies.add(new Wisie("Robcio", "Robo", WisieType.WORM));
        wisies.add(new Wisie("Wilku", "Wolfart", WisieType.WOLF));
        wisieRepository.saveAll(wisies);
    }

}
