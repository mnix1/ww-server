package com.ww.service.shop;

import com.ww.model.dto.book.ShopBookDTO;
import com.ww.model.entity.outside.book.Book;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.book.BookRepository;
import com.ww.service.book.ProfileBookService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
@AllArgsConstructor
public class ShopService {

    private final ProfileService profileService;
    private final ProfileBookService profileBookService;
    private final BookRepository bookRepository;

    public List<ShopBookDTO> listBook() {
        return bookRepository.findAllByCanBuyByCrystalOrCanBuyByGold(true, true).stream().map(ShopBookDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> buyBook(Long id) {
        Map<String, Object> model = new HashMap<>();
        Optional<Book> optionalBook = bookRepository.findOneById(id);
        if (!optionalBook.isPresent()) {
            return putErrorCode(model);
        }
        Book book = optionalBook.get();
        Profile profile = profileService.getProfile();
        if (!profile.hasEnoughResources(book.getCostResources())) {
            return putCode(model, -3);//no resources
        }
        if (profileBookService.isProfileBookShelfFull(profile.getId())) {
            return putCode(model, -2);//no space at shelf
        }
        profile.subtractResources(book.getCostResources());
        profileService.save(profile);
        profileBookService.giveBook(profile, book);
        model.put("bookType", book.getType());
        return putSuccessCode(model);
    }

}
