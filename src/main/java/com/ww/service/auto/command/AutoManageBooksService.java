package com.ww.service.auto.command;

import com.ww.model.entity.outside.book.Book;
import com.ww.model.entity.outside.book.ProfileBook;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.book.ProfileBookService;
import com.ww.service.shop.ShopService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.ww.helper.RandomHelper.randomElement;

@Service
@AllArgsConstructor
public class AutoManageBooksService {

    private final ProfileBookService profileBookService;
    private final ShopService shopService;

    public void manage(Profile profile) {
        List<ProfileBook> books = profileBookService.list(profile.getId());
        if (profileBookService.readingInProgress(books)) {
            return;
        }
        if (collectReward(books)) {
            books = profileBookService.list(profile.getId());
        }
        if (books.isEmpty()) {
            if (buyBook(profile)) {
                books = profileBookService.list(profile.getId());
            } else {
                return;
            }
        }
        profileBookService.startReadBook(randomElement(books).getId(), profile.getId());
    }

    public boolean collectReward(List<ProfileBook> books) {
        boolean claimed = false;
        for (ProfileBook book : books) {
            if (book.canClaimReward()) {
                profileBookService.claimRewardBook(book.getId(), book.getProfile().getId());
                claimed = true;
            }
        }
        return claimed;
    }

    public boolean buyBook(Profile profile) {
        List<Book> books = shopService.list();
        Collections.shuffle(books);
        for (Book book : books) {
            if (profile.hasEnoughResources(book.getCostResources())) {
                shopService.buyBook(book.getId(), profile.getId());
                return true;
            }
        }
        return false;
    }
}
