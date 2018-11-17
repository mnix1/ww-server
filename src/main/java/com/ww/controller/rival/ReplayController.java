package com.ww.controller.rival;

import com.ww.service.ReplayService;
import com.ww.service.social.FriendService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/replay")
@AllArgsConstructor
public class ReplayController {

    private final ReplayService replayService;
    private final ProfileService profileService;

    @RequestMapping(value = "/play", method = RequestMethod.GET)
    public Map play(@RequestParam Long rivalId, @RequestParam Long perspectiveProfileId) {
        replayService.replay(rivalId, perspectiveProfileId, profileService.getProfileId());
        return Collections.emptyMap();
    }

}
