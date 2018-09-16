package com.ww.controller;

import com.ww.manager.rival.RivalManager;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.service.SessionService;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.social.AuthProfileService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.ModelHelper.putSuccessCode;

@RequestMapping(value = "/auto")
@Controller
public class AutoController {

    @Autowired
    private RivalWarService rivalWarService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AuthProfileService authProfileService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String page(Model model) {
        model.addAttribute("profile", new ProfileDTO(profileService.getProfile()));
        return "page.html";
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String auth(Principal user) {
        authProfileService.authProfile(user);
        return "redirect:/auto/page/";
    }

    @ResponseBody
    @RequestMapping(value = "/correctAnswer", method = RequestMethod.GET)
    public Map<String, Object> correctAnswer() {
        Map<String, Object> model = new HashMap<>();
        RivalManager rivalManager = rivalWarService.getGlobalRivalService().get(sessionService.getProfileId());
        model.put("id", rivalManager.getRivalContainer().findCurrentCorrectAnswerId());
        return putSuccessCode(model);
    }
}
