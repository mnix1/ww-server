package com.ww.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(value = "/_log")
public class LogController {

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@RequestParam(required = false, defaultValue = "application.log") String name) {
        String tmpdir = System.getProperty("java.io.tmpdir");
        File file = new File(tmpdir + "/" + name);
        Path path = Paths.get(file.getAbsolutePath());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        String tmpdir = System.getProperty("java.io.tmpdir");
        File dir = new File(tmpdir);
        return StringUtils.join(dir.list(), System.lineSeparator());
    }
}
