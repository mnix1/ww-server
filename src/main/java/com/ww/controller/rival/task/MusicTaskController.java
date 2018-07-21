package com.ww.controller.rival.task;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.MusicTaskType;
import com.ww.service.rival.task.music.MusicTaskService;
import com.ww.service.rival.task.music.MusicTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/musicTask")
public class MusicTaskController {

    @Autowired
    MusicTrackService musicTrackService;
    @Autowired
    MusicTaskService musicTaskService;

    @RequestMapping(value = "/addTrack", method = RequestMethod.GET)
    public Map addTrack(@RequestParam String author, @RequestParam String name, @RequestParam String url, @RequestParam(required = false) Language lang) {
        if (lang == null) {
            lang = Language.ALL;
        }
        Map<String, Object> model = new HashMap<>();
        model.put("result", musicTrackService.addTrack(author, name, url, lang));
        return model;
    }

//    @RequestMapping(value = "/generate", method = RequestMethod.GET)
//    public Map generate(@RequestParam(required = false) Language lang, @RequestParam(required = false) MusicTaskType type) {
//        if (lang == null) {
//            lang = Language.ALL;
//        }
//        if (type == null) {
//            type = MusicTaskType.random();
//        }
//        Map<String, Object> model = new HashMap<>();
//        model.put("question", musicTaskService.generate(lang, type));
//        return model;
//    }

}
