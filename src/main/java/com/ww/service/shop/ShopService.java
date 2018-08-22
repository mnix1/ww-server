package com.ww.service.shop;

import com.ww.model.dto.book.ShopBookDTO;
import com.ww.model.entity.book.Book;
import com.ww.model.entity.social.Profile;
import com.ww.repository.book.BookRepository;
import com.ww.service.book.ProfileBookService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.service.book.BookService.BOOK_SHELF_COUNT;

@Service
public class ShopService {

    @Autowired
    ProfileService profileService;

    @Autowired
    RewardService rewardService;
    @Autowired
    ProfileBookService profileBookService;

    @Autowired
    BookRepository bookRepository;

    public List<ShopBookDTO> listBook() {
        return bookRepository.findAllByCanBuyByCrystalOrCanBuyByGold(true, true).stream().map(ShopBookDTO::new).collect(Collectors.toList());
    }

    public synchronized Map<String, Object> buyBook(Long id) {
        Map<String, Object> model = new HashMap<>();
        Optional<Book> optionalBook = bookRepository.findOneById(id);
        if (!optionalBook.isPresent()) {
            model.put("code", -1);
            return model;
        }
        Book book = optionalBook.get();
        Profile profile = profileService.getProfile();
        boolean isEnoughResources = false;
        if (book.getCanBuyByCrystal() && profile.getCrystal() >= book.getCrystalCost()) {
            profile.changeResources(null, -book.getCrystalCost(), null, null);
            isEnoughResources = true;
        } else if (book.getCanBuyByGold() && profile.getGold() >= book.getGoldCost()) {
            profile.changeResources(-book.getGoldCost(), null, null, null);
            isEnoughResources = true;
        }
        if (!isEnoughResources) {
            model.put("code", -3);//no resources
            return model;
        }
        if (profileBookService.isProfileBookShelfFull(profile.getId())) {
            model.put("code", -2);//no space at shelf
            return model;
        }
        profileService.save(profile);
        profileBookService.giveBook(profile, book);
        model.put("code", 1);
        model.put("bookType", book.getType());
        return model;
    }

}
