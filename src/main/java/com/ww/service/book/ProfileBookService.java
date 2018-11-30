package com.ww.service.book;

import com.ww.model.constant.book.BookType;
import com.ww.model.container.Resources;
import com.ww.model.dto.book.ProfileBookDTO;
import com.ww.model.entity.outside.book.Book;
import com.ww.model.entity.outside.book.ProfileBook;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.book.ProfileBookRepository;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.*;
import static com.ww.service.book.BookService.BOOK_SHELF_COUNT;

@Service
@AllArgsConstructor
public class ProfileBookService {

    private final ProfileService profileService;
    private final ProfileBookRepository profileBookRepository;
    private final BookService bookService;

    public List<ProfileBook> list(Long profileId) {
        return profileBookRepository.findByProfile_Id(profileId);
    }

    public List<ProfileBookDTO> listBook() {
        return list(profileService.getProfileId()).stream()
                .filter(profileBook -> !profileBook.isRewardClaimed())
                .map(ProfileBookDTO::new)
                .collect(Collectors.toList());
    }

    public boolean readingInProgress(List<ProfileBook> profileBooks) {
        return profileBooks.stream().anyMatch(profileBook -> profileBook.isInProgress() && !profileBook.canClaimReward());
    }

    @Transactional
    public Map<String, Object> startReadBook(Long profileBookId, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        List<ProfileBook> profileBooks = list(profileId);
        if (readingInProgress(profileBooks)) {
            return putCode(model, -2);
        }
        Optional<ProfileBook> optionalProfileBook = profileBooks.stream().filter(profileBook -> profileBook.getId().equals(profileBookId)).findFirst();
        if (!optionalProfileBook.isPresent()) {
            return putErrorCode(model);
        }
        ProfileBook profileBook = optionalProfileBook.get();
        profileBook.setInProgressDate(Instant.now());
        profileBookRepository.save(profileBook);
        return putSuccessCode(model);
    }

    @Transactional
    public Map<String, Object> stopReadBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileBook = profileBookRepository.findByIdAndProfile_Id(profileBookId, profileService.getProfileId());
        if (!optionalProfileBook.isPresent()) {
            return putErrorCode(model);
        }
        ProfileBook profileBook = optionalProfileBook.get();
        Long alreadyReadInterval = profileBook.inProgressInterval() + profileBook.getAlreadyReadInterval();
        profileBook.setAlreadyReadInterval(alreadyReadInterval);
        profileBook.setInProgressDate(null);
        profileBookRepository.save(profileBook);
        return putSuccessCode(model);
    }

    @Transactional
    public Map<String, Object> discardBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileBook = profileBookRepository.findByIdAndProfile_Id(profileBookId, profileService.getProfileId());
        if (!optionalProfileBook.isPresent()) {
            return putErrorCode(model);
        }
        ProfileBook profileBook = optionalProfileBook.get();
        profileBookRepository.delete(profileBook);
        return putSuccessCode(model);
    }

    @Transactional
    public Map<String, Object> claimRewardBook(Long profileBookId, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileBook = profileBookRepository.findByIdAndProfile_Id(profileBookId, profileId);
        if (!optionalProfileBook.isPresent()) {
            return putErrorCode(model);
        }
        ProfileBook profileBook = optionalProfileBook.get();
        if (!profileBook.canClaimReward()) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile(profileId);
        addRewardFromBook(profile, profileBook);
        model.put("resources", profile.getResources());
        return putSuccessCode(model);
    }

    @Transactional
    public Map<String, Object> speedUpBook(Long profileBookId, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileChest = profileBookRepository.findByIdAndProfile_Id(profileBookId, profileId);
        if (!optionalProfileChest.isPresent()) {
            return putErrorCode(model);
        }
        ProfileBook profileBook = optionalProfileChest.get();
        if (profileBook.isReadingFinished()) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile(profileId);
        Resources costResources = profileBook.speedUpCost();
        if (!profile.hasEnoughResources(costResources)) {
            return putCode(model, -2);
        }
        profile.subtractResources(costResources);
        addRewardFromBook(profile, profileBook);
        model.put("resources", profile.getResources());
        return putSuccessCode(model);
    }

    public void addRewardFromBook(Profile profile, ProfileBook profileBook) {
        profile.addResources(profileBook.getBook().getGainResources());
        profileService.save(profile);
        remove(profileBook);
    }

    public void remove(ProfileBook profileBook) {
        profileBookRepository.delete(profileBook);
    }

    public ProfileBook giveBook(Profile profile, Book book) {
        ProfileBook profileBook = new ProfileBook(profile, book);
        profileBookRepository.save(profileBook);
        return profileBook;
    }

    public ProfileBook giveBook(Profile profile, BookType bookType) {
        return giveBook(profile, bookService.findBookByType(bookType));
    }

    public boolean isProfileBookShelfFull(Long profileId) {
        return profileBookRepository.countByProfile_Id(profileId) >= BOOK_SHELF_COUNT;
    }

    public boolean isProfileBookShelfEmpty(Long profileId) {
        return profileBookRepository.countByProfile_Id(profileId) == 0;
    }

}
