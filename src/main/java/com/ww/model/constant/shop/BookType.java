package com.ww.model.constant.shop;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

public enum BookType {
    //0
    LEAFLET,
    FAIRY_TALE,
    //1
    TV_PROGRAM,
    SPORT_MAGAZINE,
    COLORFUL_MAGAZINE,
    NEWSPAPER,
    //2,
    ROMANCE_NOVEL,
    USER_MANUAL,
    BIOGRAPHY,
    //3,
    HISTORICAL_NOVEL,
    CROSSWORD,
    COMPANY_FINANCIAL_REPORT,
    //4
    WORLD_ATLAS,
    STUDENT_BOOK,
    //5
    ENCYCLOPEDIA,
    SCIENCE_ARTICLE,
    //6
    MYSTERIOUS_BOOK,
    SECRET_BOOK;

    public static BookType random() {
        List<BookType> possible = Arrays.asList(values());
        return randomElement(possible);
    }
}
