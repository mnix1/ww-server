package com.ww.controller.rival;

import com.ww.service.SessionService;
import com.ww.service.rival.battle.BattleFastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/battle")
public class BattleController {

    @Autowired
    SessionService sessionService;

    @Autowired
    BattleFastService battleFastService;

    @RequestMapping(value = "/startFast", method = RequestMethod.POST)
    public Map startFast() {
        return battleFastService.startFast();
    }
    @RequestMapping(value = "/cancelFast", method = RequestMethod.POST)
    public Map cancelFast() {
        return battleFastService.cancelFast();
    }
}
