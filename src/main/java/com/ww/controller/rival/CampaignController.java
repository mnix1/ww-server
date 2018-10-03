package com.ww.controller.rival;

import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.container.rival.init.RivalCampaignWarInit;
import com.ww.model.dto.rival.campaign.CampaignDTO;
import com.ww.model.dto.rival.campaign.ProfileCampaignDTO;
import com.ww.service.rival.campaign.CampaignService;
import com.ww.service.rival.campaign.RivalCampaignWarService;
import com.ww.service.rival.init.RivalRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@RestController
@RequestMapping(value = "/campaign")
public class CampaignController {
    @Autowired
    private RivalCampaignWarService rivalCampaignWarService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private RivalRunService rivalRunService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public Map start() {
        Map<String, Object> model = new HashMap<>();
        Optional<RivalCampaignWarInit> init = rivalCampaignWarService.init();
        init.ifPresent(rivalCampaignWarInit -> rivalRunService.run(rivalCampaignWarInit));
        if (!init.isPresent()) {
            return putErrorCode(model);
        }
        return putSuccessCode(model);
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
    public Map close() {
        return campaignService.close();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<CampaignDTO> list() {
        return campaignService.list();
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ProfileCampaignDTO active() {
        return campaignService.activeDTO().orElse(null);
    }
}
