package com.ww.controller;

import com.ww.model.dto.shop.BookDTO;
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

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<BookDTO> list() {
        return shopService.list();
    }

    @RequestMapping(value = "/open", method = RequestMethod.POST)
    public Map open(@RequestBody Map<String, Object> payload) {
        if(!payload.containsKey("id")){
            throw new IllegalArgumentException();
        }
        Long profileChestId = ((Integer) payload.get("id")).longValue();
        return shopService.open(profileChestId);
    }

}
