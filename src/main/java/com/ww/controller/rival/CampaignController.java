package com.ww.controller.rival;

import com.ww.service.SessionService;
import com.ww.service.rival.campaign.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/campaign")
public class CampaignController {
    @Autowired
    SessionService sessionService;

    @Autowired
    CampaignService campaignService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public Map start(@RequestBody Map<String, Object> payload) {
        return campaignService.start();
    }
}
