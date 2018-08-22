package com.ww.model.dto.book;

import com.ww.model.constant.book.BookType;
import com.ww.model.entity.book.Book;
import com.ww.model.entity.book.ProfileBook;
import lombok.Getter;

@Getter
public class ProfileBookDTO {

    private Long id;
    private BookType type;

    private Long readTime;
    private Integer level;

    private Long gainCrystal;
    private Long gainElixir;
    private Long gainWisdom;

    private Boolean isInProgress;
    private Long alreadyReadInterval;
    private Boolean canClaimReward;

    private String namePolish;
    private String nameEnglish;

    public ProfileBookDTO(Book book) {
        this.type = book.getType();
        this.readTime = book.getReadTime();
        this.level = book.getLevel();
        this.gainCrystal = book.getGainCrystal();
        this.gainElixir = book.getGainElixir();
        this.gainWisdom = book.getGainWisdom();
        this.namePolish = book.getNamePolish();
        this.nameEnglish = book.getNameEnglish();
    }

    public ProfileBookDTO(ProfileBook profileBook) {
        this(profileBook.getBook());
        this.id = profileBook.getId();
        this.isInProgress = profileBook.isInProgress();
        this.alreadyReadInterval = Math.min(profileBook.inProgressInterval() + profileBook.getAlreadyReadInterval(), this.readTime);
        this.canClaimReward = profileBook.canClaimReward();
    }
}
