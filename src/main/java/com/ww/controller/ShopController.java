package com.ww.controller;

import com.ww.model.dto.book.ShopBookDTO;
import com.ww.service.SessionService;
import com.ww.service.shop.ShopService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/shop")
public class ShopController {

    @Autowired
    ProfileService profileService;

    @Autowired
    SessionService sessionService;

    @Autowired
    ShopService shopService;

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
        return shopService.buyBook(bookId);
    }
}
