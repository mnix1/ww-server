package com.ww.controller;

import com.ww.model.constant.Language;
import com.ww.model.constant.wisie.WisorType;
import com.ww.model.dto.book.ProfileBookDTO;
import com.ww.model.dto.social.ExtendedProfileResourcesDTO;
import com.ww.service.book.ProfileBookService;
import com.ww.service.social.AuthProfileService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ww.helper.ModelHelper.putErrorCode;

@RestController
@RequestMapping(value = "/profile")
@AllArgsConstructor
public class ProfileController {

    private final AuthProfileService authProfileService;
    private final ProfileService profileService;
    private final ProfileBookService profileBookService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("username") || !payload.containsKey("email")) {
            throw new IllegalArgumentException();
        }
        try {
            String username = (String) payload.get("username");
            String email = (String) payload.get("email");
            Language lang = Language.valueOf((String) payload.get("lang"));
            return authProfileService.createOutsideProfile(username.trim(), email.trim(), lang);
        } catch (Exception e) {
        }
        return putErrorCode(new HashMap<>());
    }

    @RequestMapping(value = "/changeWisor", method = RequestMethod.POST)
    public Map<String, Object> changeWisor(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("wisor")) {
            throw new IllegalArgumentException();
        }
        WisorType type = WisorType.valueOf((String) payload.get("wisor"));
        return profileService.changeWisor(type);
    }

    @RequestMapping(value = "/changeName", method = RequestMethod.POST)
    public Map<String, Object> changeName(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("name")) {
            throw new IllegalArgumentException();
        }
        String name = (String) payload.get("name");
        return profileService.changeName(name);
    }

    @RequestMapping(value = "/changeLanguage", method = RequestMethod.POST)
    public Map<String, Object> changeLanguage(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("lang")) {
            throw new IllegalArgumentException();
        }
        Language lang = Language.valueOf((String) payload.get("lang"));
        return profileService.changeLanguage(lang);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete() {
        return profileService.delete();
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ExtendedProfileResourcesDTO profile(Principal user) {
        return authProfileService.authProfile(user);
    }

    @RequestMapping(value = "/testSignIn", method = RequestMethod.GET)
    public Boolean testSignIn() {
        return true;
    }

    @RequestMapping(value = "/listBook", method = RequestMethod.GET)
    public List<ProfileBookDTO> listBook() {
        return profileBookService.listBook();
    }

    @RequestMapping(value = "/startReadBook", method = RequestMethod.POST)
    public Map<String, Object> startReadBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.startReadBook(profileBookId, profileService.getProfileId());
    }

    @RequestMapping(value = "/stopReadBook", method = RequestMethod.POST)
    public Map<String, Object> stopReadBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.stopReadBook(profileBookId);
    }

    @RequestMapping(value = "/discardBook", method = RequestMethod.POST)
    public Map<String, Object> discardBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.discardBook(profileBookId);
    }

    @RequestMapping(value = "/claimRewardBook", method = RequestMethod.POST)
    public Map<String, Object> claimRewardBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.claimRewardBook(profileBookId, profileService.getProfileId());
    }

    @RequestMapping(value = "/speedUpBook", method = RequestMethod.POST)
    public Map<String, Object> speedUpBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.speedUpBook(profileBookId, profileService.getProfileId());
    }

}
