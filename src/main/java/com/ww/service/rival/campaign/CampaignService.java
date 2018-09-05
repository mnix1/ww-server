package com.ww.service.rival.campaign;

import com.ww.model.dto.rival.campaign.CampaignDTO;
import com.ww.repository.rival.campaign.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    public Map<String, Object> list() {
        Map<String, Object> model = new HashMap<>();
        model.put("campaigns", campaignRepository.findAll().stream().map(CampaignDTO::new).collect(Collectors.toList()));
        return model;
    }

}
