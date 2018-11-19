package com.ww.controller;

import com.ww.model.dto.book.ShopBookDTO;
import com.ww.service.shop.ShopService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/shop")
@AllArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ProfileService profileService;

    @RequestMapping(value = "/listBook", method = RequestMethod.GET)
    public List<ShopBookDTO> listBook() {
        return shopService.listBook();
    }

    @RequestMapping(value = "/buyBook", method = RequestMethod.POST)
    public Map buyBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long bookId = ((Integer) payload.get("id")).longValue();
        return shopService.buyBook(bookId, profileService.getProfileId());
    }
}
