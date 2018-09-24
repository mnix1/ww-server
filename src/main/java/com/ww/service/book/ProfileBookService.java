package com.ww.service.book;

import com.ww.model.constant.book.BookType;
import com.ww.model.dto.book.ProfileBookDTO;
import com.ww.model.entity.outside.book.Book;
import com.ww.model.entity.outside.book.ProfileBook;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.book.ProfileBookRepository;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.*;
import static com.ww.service.book.BookService.BOOK_SHELF_COUNT;

@Service
public class ProfileBookService {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileBookRepository profileBookRepository;
    @Autowired
    private BookService bookService;

    public List<ProfileBookDTO> listBook() {
        return profileService.getProfile().getBooks().stream()
                .filter(profileBook -> !profileBook.isRewardClaimed())
                .map(ProfileBookDTO::new)
                .collect(Collectors.toList());
    }

    public boolean checkIfProfileReadingBook(List<ProfileBook> profileBooks) {
        return profileBooks.stream().noneMatch(profileBook -> profileBook.isInProgress() && !profileBook.canClaimReward());
    }

    public synchronized Map<String, Object> startReadBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        List<ProfileBook> profileBooks = profileBookRepository.findByProfile_Id(profileService.getProfileId());
        if (!checkIfProfileReadingBook(profileBooks)) {
            //reading another book
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

    public synchronized Map<String, Object> stopReadBook(Long profileBookId) {
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

    public synchronized Map<String, Object> discardBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileBook = profileBookRepository.findByIdAndProfile_Id(profileBookId, profileService.getProfileId());
        if (!optionalProfileBook.isPresent()) {
            return putErrorCode(model);
        }
        ProfileBook profileBook = optionalProfileBook.get();
        profileBookRepository.delete(profileBook);
        return putSuccessCode(model);
    }

    public synchronized Map<String, Object> claimRewardBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileChest = profileBookRepository.findByIdAndProfile_Id(profileBookId, profileService.getProfileId());
        if (!optionalProfileChest.isPresent()) {
            return putErrorCode(model);
        }
        ProfileBook profileBook = optionalProfileChest.get();
        if (!profileBook.canClaimReward()) {
            return putErrorCode(model);
        }
        profileBook.setCloseDate(Instant.now());
        addRewardFromBook(profileBook);
        remove(profileBook);
        return putSuccessCode(model);
    }

    public synchronized Map<String, Object> speedUpBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileChest = profileBookRepository.findByIdAndProfile_Id(profileBookId, profileService.getProfileId());
        if (!optionalProfileChest.isPresent()) {
            return putErrorCode(model);
        }
        ProfileBook profileBook = optionalProfileChest.get();
        if (profileBook.isReadingFinished()) {
            return putErrorCode(model);
        }
        Profile profile = profileService.getProfile();
//        long crystalCost = (long) Math.floor((profileBook.timeToRead() - 1) / 1000d / 3600d);
        long crystalCost = (long) Math.ceil((profileBook.timeToRead() - 1) / 1000d / 3600d);
        if (profile.getCrystal() < crystalCost) {
            return putCode(model, -2);
        }
        profile.changeResources(null, -crystalCost, null, null);
        profileService.save(profile);
        profileBook.setCloseDate(Instant.now());
        addRewardFromBook(profileBook);
        remove(profileBook);
        return putSuccessCode(model);
    }

    public void addRewardFromBook(ProfileBook profileBook) {
        Profile profile = profileBook.getProfile();
        Book b = profileBook.getBook();
        profile.changeResources(null, b.getCrystalGain(), b.getWisdomGain(), b.getElixirGain());
        profileService.save(profile);
    }

    public void remove(ProfileBook profileBook) {
        profileBookRepository.delete(profileBook);
    }

    public void giveBook(Profile profile, Book book) {
        ProfileBook profileBook = new ProfileBook(profile, book);
        profileBookRepository.save(profileBook);
    }

    public void giveBook(Profile profile, BookType bookType) {
        giveBook(profile, bookService.findBookByType(bookType));
    }

    public boolean isProfileBookShelfFull(Long profileId) {
        return profileBookRepository.countByProfile_Id(profileId) >= BOOK_SHELF_COUNT;
    }

}
