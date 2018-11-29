package com.ww.controller.rival;

import com.ww.game.replay.Replay;
import com.ww.model.container.MapModel;
import com.ww.service.ReplayService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Map;

@RestController
@RequestMapping(value = "/_replay")
@AllArgsConstructor
public class ReplayController {

    private final ReplayService replayService;
    private final ProfileService profileService;

    @Transactional
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

    @Transactional
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map<String, Object> list() {
        return replayService.list();
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.GET)
    public Map<String, Object> cancel(@RequestParam(required = false) Long targetProfileId) {
        if (targetProfileId == null) {
            targetProfileId = profileService.getProfileId();
        }
        replayService.cancel(targetProfileId);
        return new MapModel("canceled", true).get();
    }

    @RequestMapping(value = "/cancelAll", method = RequestMethod.GET)
    public Map<String, Object> cancelAll() {
        for (Replay replay : ReplayService.activeReplays) {
            replayService.disposeReplay(replay);
        }
        return new MapModel("canceledAll", true).get();
    }

}
