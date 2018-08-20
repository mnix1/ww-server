package com.ww.controller;

import com.ww.model.dto.book.ShopBookDTO;
import com.ww.service.SessionService;
import com.ww.service.shop.ShopService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/shop")
public class ShopController {

    @Autowired
    ProfileService profileService;

    @Autowired
    SessionService sessionService;

    @Autowired
    ShopService shopService;

    @RequestMapping(value = "/listBooks", method = RequestMethod.GET)
    public List<ShopBookDTO> listBooks() {
        return shopService.listBooks();
    }

}
