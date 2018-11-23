package com.ww.controller.rival;

import com.ww.model.container.MapModel;
import com.ww.service.ReplayService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/_replay")
@AllArgsConstructor
public class ReplayController {

    private final ReplayService replayService;
    private final ProfileService profileService;

    @RequestMapping(value = "/play", method = RequestMethod.GET)
    public Map<String, Object> play(@RequestParam Long rivalId,
                                    @RequestParam(required = false) Double speed,
                                    @RequestParam(required = false) Long perspectiveProfileId,
                                    @RequestParam(required = false) Long targetProfileId) {
        if (targetProfileId == null) {
            targetProfileId = profileService.getProfileId();
        }
        if (speed == null) {
            speed = 1.0;
        }
        return replayService.replay(rivalId, speed, perspectiveProfileId, targetProfileId);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public Map<String, Object> cancel(@RequestParam(required = false) Long targetProfileId) {
        if (targetProfileId == null) {
            targetProfileId = profileService.getProfileId();
        }
        replayService.cancel(targetProfileId);
        return new MapModel("canceled", true).get();
    }

}
