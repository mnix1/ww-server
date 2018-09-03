package com.ww.model.entity.book;

import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Instant createDate;
    private Instant inProgressDate;
    private Instant closeDate;
    private Long alreadyReadInterval = 0L;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false, updatable = false)
    private Book book;

    public ProfileBook(Profile profile, Book book) {
        this.profile = profile;
        this.book = book;
        this.createDate = Instant.now();
    }

    public Long inProgressInterval() {
        if (isInProgress()) {
            return Instant.now().toEpochMilli() - inProgressDate.toEpochMilli();
        }
        return 0L;
    }

    public Boolean isInProgress() {
        return inProgressDate != null;
    }

    public Boolean isReadingFinished() {
        return inProgressInterval() + alreadyReadInterval > book.getReadTime();
    }

    public Boolean isRewardClaimed() {
        return closeDate != null;
    }

    public Boolean canClaimReward() {
        if (isRewardClaimed()) {
            return false;
        }
        return isReadingFinished();
    }

    public long timeToRead(){
        return book.getReadTime() - inProgressInterval() - alreadyReadInterval;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((ProfileBook) obj).id);
    }

}
