package com.ww.controller;

import com.ww.model.dto.social.ProfileMailDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.wisie.ProfileWisieRepository;
import com.ww.service.social.FriendService;
import com.ww.service.social.MailService;
import com.ww.service.social.ProfileService;
import com.ww.service.wisie.ProfileWisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/mail")
public class MailController {

    @Autowired
    private MailService mailService;


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<ProfileMailDTO> list() {
        return mailService.list();
    }
}
