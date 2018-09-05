package com.ww.database;

import com.ww.model.entity.rival.task.Clipart;
import com.ww.repository.rival.task.category.ClipartRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.ImageHelper.convertSvgToPng;


@NoArgsConstructor
@Service
public class InitClipartsService {

    @Autowired
    private ClipartRepository clipartRepository;

    public void initCliparts() {
        List<Clipart> cliparts = new ArrayList<>();
        cliparts.add(new Clipart("pepper.svg", "Papryka", "Pepper"));
        cliparts.add(new Clipart("notebook.svg", "Notes", "Notebook"));
        cliparts.add(new Clipart("pickles.svg", "Ogórki konserwowe", "Pickles"));
        cliparts.add(new Clipart("scales.svg", "Waga", "Scales"));
        cliparts.add(new Clipart("cdDvd.svg", "Płyta CD/DVD", "CD/DVD"));
        cliparts.add(new Clipart("microscope.svg", "Mikroskop", "Microscope"));
        cliparts.add(new Clipart("basketball.svg", "Piłka do kosza", "Basketball ball"));
        cliparts.add(new Clipart("football.svg", "Piłka do nogi", "Football ball"));
        cliparts.add(new Clipart("fish.svg", "Ryba", "Fish"));
        cliparts.add(new Clipart("calculator.svg", "Kalkulator", "Calculator"));
        cliparts.add(new Clipart("hardDrive.svg", "Dysk twardy", "Hard drive"));
        cliparts.add(new Clipart("piano.svg", "Fortepian", "Piano"));
        cliparts.add(new Clipart("rubicCube.svg", "Kostka Rubika", "Rubic's cube"));
        cliparts.add(new Clipart("seat.svg", "Fotel", "Seat"));
        cliparts.add(new Clipart("car.svg", "Samochód", "Car"));
        cliparts.add(new Clipart("plumbersWrench.svg", "Klucz francuski", "Plumbers Wrench"));
        cliparts.add(new Clipart("thumbUp.svg", "Kciuk w górę", "Thumb up"));
        cliparts.add(new Clipart("thumbDown.svg", "Kciuk w dół", "Thumb down"));
        cliparts.add(new Clipart("gameController.svg", "Kontroler do gier", "Game controller"));
        cliparts.add(new Clipart("emoticon.svg", "Emotikona", "Emoticon"));
        cliparts.add(new Clipart("tree.svg", "Drzewo", "Tree"));
        cliparts.add(new Clipart("snowman.svg", "Bałwan", "Snowman"));
        cliparts.add(new Clipart("clouds.svg", "Chmury", "Clouds"));
        cliparts.add(new Clipart("podium.svg", "Podium", "Podium"));
        cliparts.add(new Clipart("medal.svg", "Medal", "Medal"));
        cliparts.add(new Clipart("speaker.svg", "Głośnik", "Speaker"));
        cliparts.add(new Clipart("helmet.svg", "Kask", "Helmet"));
        cliparts.add(new Clipart("scooter.svg", "Skuter", "Scooter"));
        cliparts.add(new Clipart("trashCan.svg", "Kosz na śmieci", "Trash can"));
        cliparts.add(new Clipart("balloon.svg", "Balon", "Balloon"));
        cliparts.add(new Clipart("aeroplan.svg", "Samolot", "Aeroplan"));
        cliparts.forEach(clipart -> convertSvgToPng(clipart.getResourcePath(), clipart.getPngResourcePath()));
        clipartRepository.saveAll(cliparts);
    }

}
