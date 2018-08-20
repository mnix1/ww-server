package com.ww.service.book;

import com.ww.model.dto.book.ProfileBookDTO;
import com.ww.model.entity.book.Book;
import com.ww.model.entity.book.ProfileBook;
import com.ww.model.entity.social.Profile;
import com.ww.repository.book.ProfileBookRepository;
import com.ww.service.SessionService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.service.book.BookService.BOOK_SHELF_COUNT;

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

    public synchronized  Map<String, Object> startReadBook(Long profileBookId) {
        Map<String, Object> model = new HashMap<>();
        List<ProfileBook> profileBooks = profileBookRepository.findByProfile_Id(sessionService.getProfileId());
        if (!checkIfProfileReadingBook(profileBooks)) {
            model.put("code", -1);
            return model;
        }
        Optional<ProfileBook> optionalProfileChest = profileBooks.stream().filter(profileBook -> profileBook.getId().equals(profileBookId)).findFirst();
        if (!optionalProfileChest.isPresent()) {
            model.put("code", -1);
            return model;
        }
        ProfileBook profileBook = optionalProfileChest.get();
        profileBook.setInProgressDate(Instant.now());
        profileBookRepository.save(profileBook);
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
        addRewardForBook(profileBook);
        model.put("code", 1);
        remove(profileBook);
        return model;
    }

    public void addRewardForBook(ProfileBook profileBook) {
        Profile profile = profileBook.getProfile();
        Book b = profileBook.getBook();
        profile.changeResources(null, b.getGainDiamond(), b.getGainWisdom(), b.getGainElixir());
        profileService.save(profile);
    }

    public void remove(ProfileBook profileBook) {
        profileBookRepository.delete(profileBook);
    }

    public void giveBook(String profileTag) {
        Profile profile = profileService.getProfile(profileTag);
        if (profileBookRepository.findByProfile_Id(profile.getId()).size() >= BOOK_SHELF_COUNT) {
            return;
        }
        Book book = bookService.findRandomBook();
        ProfileBook profileBook = new ProfileBook(profile, book);
        profileBookRepository.save(profileBook);
    }
}
