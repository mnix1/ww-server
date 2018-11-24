package com.ww.service.auto.command;

import com.ww.model.container.Resources;
import com.ww.model.entity.outside.book.Book;
import com.ww.model.entity.outside.book.ProfileBook;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.book.ProfileBookService;
import com.ww.service.shop.ShopService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.success;
import static com.ww.helper.RandomHelper.randomElement;

@Service
@AllArgsConstructor
public class AutoManageBooksService {
    private static Logger logger = LoggerFactory.getLogger(AutoManageBooksService.class);

    private final ProfileBookService profileBookService;
    private final ShopService shopService;
    private final ProfileService profileService;

    public void manage(Profile profile) {
        List<ProfileBook> books = profileBookService.list(profile.getId());
        if (collectReward(books)) {
            books = profileBookService.list(profile.getId());
            profile = profileService.getProfile(profile.getId());
        }
        if (books.size() > 2) {
            maybeSpeedUp(books, profile);
        }
        maybeBuyAndSpeedUp(profile);
        if (profileBookService.readingInProgress(books)) {
            return;
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

    public void maybeSpeedUp(List<ProfileBook> books, Profile profile) {
        books = books.stream().filter(ProfileBook::isInProgress).collect(Collectors.toList());
        for (ProfileBook book : books) {
            if (profile.hasEnoughResources(book.speedUpCost())) {
                speedUp(book.getId(), profile, book.speedUpCost(), book.getBook().getGainResources());
            }
        }
    }

    private void speedUp(Long profileBookId, Profile profile, Resources costResources, Resources gainResources) {
        profileBookService.speedUpBook(profileBookId, profile.getId());
        profile.subtractResources(costResources);
        profile.addResources(gainResources);
    }

    public void maybeBuyAndSpeedUp(Profile profile) {
        if (profile.getResources().getGold() < 20 || profile.getResources().getCrystal() < 10) {
            return;
        }
        Book crystalBook = findBestForCrystalsBook();
        while (profile.hasEnoughResources(crystalBook.getCostResources())) {
            Map<String, Object> result = shopService.buyBook(crystalBook.getId(), profile.getId());
            if (!success(result)) {
                logger.error("maybeBuyAndSpeedUp error profile={}, profileResources={}, bookCost={}", profile, profile.getResources(), crystalBook.getCostResources());
                return;
            }
            profile.subtractResources(crystalBook.getCostResources());
            Long profileBookId = (Long) result.get("id");
            if (profile.hasEnoughResources(crystalBook.speedUpCost())) {
                speedUp(profileBookId, profile, crystalBook.speedUpCost(), crystalBook.getGainResources());
            }
        }
        Book elixirBook = findBestForElixirBook();
        if (!profile.hasEnoughResources(new Resources(elixirBook.getCostResources()).multiply(2))) {
            return;
        }
        Map<String, Object> result = shopService.buyBook(elixirBook.getId(), profile.getId());
        if (!success(result)) {
            logger.error("maybeBuyAndSpeedUp error profile={}, profileResources={}, bookCost={}", profile, profile.getResources(), elixirBook.getCostResources());
            return;
        }
        profile.subtractResources(elixirBook.getCostResources());
        Long profileBookId = (Long) result.get("id");
        if (profile.hasEnoughResources(elixirBook.speedUpCost())) {
            speedUp(profileBookId, profile, elixirBook.speedUpCost(), elixirBook.getGainResources());
        }
    }

    private Book findBestForCrystalsBook() {
        List<Book> books = shopService.list();
        return books.stream().filter(Book::getCanBuyByGold).max(Comparator.comparing(book -> {
            double speedUpCost = book.speedUpCost().getCrystal().doubleValue();
            double crystalGain = book.getCrystalGain().doubleValue();
            double wisdomGain = book.getWisdomGain().doubleValue();
            return (crystalGain + wisdomGain * 0.85) / speedUpCost;
        })).get();
    }

    private Book findBestForElixirBook() {
        List<Book> books = shopService.list();
        return books.stream().filter(Book::getCanBuyByCrystal).max(Comparator.comparing(book -> {
            double speedUpCost = book.speedUpCost().getCrystal().doubleValue();
            double elixirGain = book.getElixirGain().doubleValue();
            double wisdomGain = book.getWisdomGain().doubleValue();
            return (elixirGain + wisdomGain * 0.85) / speedUpCost;
        })).get();
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
