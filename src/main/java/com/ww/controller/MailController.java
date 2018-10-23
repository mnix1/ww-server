package com.ww.controller;

import com.ww.model.dto.social.ProfileMailDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.wisie.ProfileWisieRepository;
import com.ww.service.social.FriendService;
import com.ww.service.social.MailService;
import com.ww.service.social.ProfileService;
import com.ww.service.wisie.ProfileWisieService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/mail")
@AllArgsConstructor
public class MailController {

    private final MailService mailService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<ProfileMailDTO> list() {
        return mailService.list();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long deleteId = ((Integer) payload.get("id")).longValue();
        return mailService.delete(deleteId);
    }

    @RequestMapping(value = "/claimReward", method = RequestMethod.POST)
    public Map<String, Object> claimReward(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long claimRewardId = ((Integer) payload.get("id")).longValue();
        return mailService.claimReward(claimRewardId);
    }
    @RequestMapping(value = "/displayed", method = RequestMethod.GET)
    public Map<String, Object> displayed() {
        return mailService.displayed();
    }
}
