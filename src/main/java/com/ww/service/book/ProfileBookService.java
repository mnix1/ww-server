package com.ww.service.book;

import com.ww.model.dto.book.ProfileBookDTO;
import com.ww.model.entity.book.Book;
import com.ww.model.entity.book.ProfileBook;
import com.ww.model.entity.social.Profile;
import com.ww.repository.book.ProfileBookRepository;
import com.ww.service.SessionService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import com.ww.websocket.message.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.service.book.BookService.BOOK_SHELF_COUNT;
import static com.ww.websocket.message.Message.REWARD;

@Service
public class ProfileBookService {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    ProfileBookRepository profileBookRepository;
    @Autowired
    BookService bookService;

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
        List<ProfileBook> profileBooks = profileBookRepository.findByProfile_Id(sessionService.getProfileId());
        if (!checkIfProfileReadingBook(profileBooks)) {
            model.put("code", -2);//reading another book
            return model;
        }
        Optional<ProfileBook> optionalProfileBook = profileBooks.stream().filter(profileBook -> profileBook.getId().equals(profileBookId)).findFirst();
        if (!optionalProfileBook.isPresent()) {
            model.put("code", -1);
            return model;
        }
        ProfileBook profileBook = optionalProfileBook.get();
        profileBook.setInProgressDate(Instant.now());
        profileBookRepository.save(profileBook);
        model.put("code", 1);
        return model;
    }

    public synchronized Map<String, Object> stopReadBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileBook = profileBookRepository.findByIdAndProfile_Id(profileBookId, sessionService.getProfileId());
        if (!optionalProfileBook.isPresent()) {
            model.put("code", -1);
            return model;
        }
        ProfileBook profileBook = optionalProfileBook.get();
        Long alreadyReadInterval = profileBook.inProgressInterval() + profileBook.getAlreadyReadInterval();
        profileBook.setAlreadyReadInterval(alreadyReadInterval);
        profileBook.setInProgressDate(null);
        profileBookRepository.save(profileBook);
        model.put("code", 1);
        return model;
    }

    public synchronized Map<String, Object> discardBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileBook = profileBookRepository.findByIdAndProfile_Id(profileBookId, sessionService.getProfileId());
        if (!optionalProfileBook.isPresent()) {
            model.put("code", -1);
            return model;
        }
        ProfileBook profileBook = optionalProfileBook.get();
        profileBookRepository.delete(profileBook);
        model.put("code", 1);
        return model;
    }

    public synchronized Map<String, Object> claimRewardBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileChest = profileBookRepository.findByIdAndProfile_Id(profileBookId, sessionService.getProfileId());
        if (!optionalProfileChest.isPresent()) {
            model.put("code", -1);
            return model;
        }
        ProfileBook profileBook = optionalProfileChest.get();
        if (!profileBook.canClaimReward()) {
            model.put("code", -1);
            return model;
        }
        profileBook.setCloseDate(Instant.now());
        addRewardFromBook(profileBook);
        model.put("code", 1);
        remove(profileBook);
        return model;
    }

    public synchronized Map<String, Object> speedUpBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ProfileBook> optionalProfileChest = profileBookRepository.findByIdAndProfile_Id(profileBookId, sessionService.getProfileId());
        if (!optionalProfileChest.isPresent()) {
            model.put("code", -1);
            return model;
        }
        ProfileBook profileBook = optionalProfileChest.get();
        if (profileBook.isReadingFinished()) {
            model.put("code", -1);
            return model;
        }
        Profile profile = profileService.getProfile();
        long crystalCost = (long) Math.floor((profileBook.timeToRead() - 1) / 1000d / 3600d);
        if (profile.getCrystal() < crystalCost) {
            model.put("code", -2);
            return model;
        }
        profile.changeResources(null, -crystalCost, null, null);
        profileService.save(profile);
        profileBook.setCloseDate(Instant.now());
        addRewardFromBook(profileBook);
        model.put("code", 1);
        remove(profileBook);
        return model;
    }

    public void addRewardFromBook(ProfileBook profileBook) {
        Profile profile = profileBook.getProfile();
        Book b = profileBook.getBook();
        profile.changeResources(null, b.getGainCrystal(), b.getGainWisdom(), b.getGainElixir());
        profileService.save(profile);
    }

    public void remove(ProfileBook profileBook) {
        profileBookRepository.delete(profileBook);
    }

    public void giveBook(Profile profile, Book book) {
        ProfileBook profileBook = new ProfileBook(profile, book);
        profileBookRepository.save(profileBook);
    }


    public boolean isProfileBookShelfFull(Long profileId) {
        return profileBookRepository.countByProfile_Id(profileId) >= BOOK_SHELF_COUNT;
    }

}
