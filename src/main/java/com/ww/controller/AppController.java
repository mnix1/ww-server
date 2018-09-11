package com.ww.controller;

import com.ww.service.SessionService;
import com.ww.service.book.ProfileBookService;
import com.ww.service.social.ProfileService;
import com.ww.service.wisie.WisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class AppController {

    @Autowired
    ProfileService profileService;

    @Autowired
    ProfileBookService profileBookService;

    @Autowired
    WisieService wisieService;

    @Autowired
    SessionService sessionService;

//    @RequestMapping(value = "/")
//    public String index() {
//        return "index.html";
//    }

}
