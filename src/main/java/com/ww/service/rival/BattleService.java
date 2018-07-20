package com.ww.service.rival;

import com.ww.repository.rival.battle.BattleRepository;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BattleService {

    @Autowired
    private BattleRepository battleRepository;

    @Autowired
    private SessionService sessionService;

}
