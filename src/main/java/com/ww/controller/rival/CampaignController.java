package com.ww.controller.rival;

import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.dto.rival.campaign.CampaignDTO;
import com.ww.model.dto.rival.campaign.ProfileCampaignDTO;
import com.ww.service.SessionService;
import com.ww.service.rival.campaign.CampaignService;
import com.ww.service.rival.campaign.CampaignWarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/campaign")
public class CampaignController {
    @Autowired
    SessionService sessionService;

    @Autowired
    CampaignWarService campaignWarService;

    @Autowired
    CampaignService campaignService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public Map start(@RequestBody Map<String, Object> payload) {
        return campaignWarService.start();
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public Map init(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type") || !payload.containsKey("destination") || !payload.containsKey("ids")) {
            throw new IllegalArgumentException();
        }
        CampaignType type = CampaignType.valueOf((String) payload.get("type"));
        CampaignDestination destination = CampaignDestination.valueOf((String) payload.get("destination"));
        List<Long> ids = ((List<Integer>) payload.get("ids")).stream().map(Integer::longValue).collect(Collectors.toList());
        return campaignService.init(type, destination, ids);
    }

    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public Map close(@RequestBody Map<String, Object> payload) {
        return campaignService.close();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<CampaignDTO> list() {
        return campaignService.list();
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ProfileCampaignDTO active() {
        return campaignService.activeDTO();
    }
}
