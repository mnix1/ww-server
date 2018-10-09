package com.ww.service.rival.task.lyrics;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.TrackSource;
import com.ww.model.entity.inside.task.Track;
import com.ww.repository.inside.category.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.ww.helper.FileHelper.*;
import static com.ww.helper.NetworkHelper.downloadContent;

@Service
public class TrackService {

    @Autowired
    TrackRepository trackRepository;

    private static String VERSE = "_V_";
    private static String LINE = "_L_";

    public Boolean addTrack(String author, String name, String url, Language lang) {
        Track track = new Track();
        track.setAuthor(author);
        track.setName(name);
        track.setUrl(url);
        TrackSource source = TrackSource.fromUrl(url);
        track.setSource(source);
        track.setLanguage(lang);
        String content = loadOrDownloadContent(track);
        track.setContent(content);
        trackRepository.save(track);
        return true;
    }

    private String loadOrDownloadContent(Track track) {
        String content;
        File file = getResource(track.getContentResourcePath());
        if (file == null || !file.exists()) {
            content = downloadTransformSaveContent(track);
        } else {
            content = readFile(track.getContentResourcePath());
        }
        return content;
    }

    private String downloadTransformSaveContent(Track track) {
        String content = downloadContent(track.getUrl());
        if (content == null) {
            return null;
        }
        if (track.getSource() == TrackSource.TEKSTOWO) {
            content = transformContentTekstowo(content);
        }
        if (track.getSource() == TrackSource.ISING) {
            content = transformContentIsing(content);
        }
//        if (source == TrackSource.GROOVE) {
//            content = transformContentGroove(content);
//        }
        saveToFile(content, track.getContentResourcePath());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return content;
    }

    private String transformContentTekstowo(String content) {
        content = content.substring(content.indexOf("<div class=\"song-text\">"));
        return content.substring(126, content.indexOf("<p>&nbsp;</p>") - 24)
                .replaceAll("\\\\", "")
                .replaceAll("[\";,./*]", "")
                .replaceAll(" \\?", "")
                .replaceAll("<br >\r\n[ ]*<br >\r\n", VERSE)
                .replaceAll("<br >\n<br >\n", VERSE)
                .replaceAll("<br >\r\n", LINE)
                .replaceAll("<br >\n", LINE);
    }

    private String transformContentIsing(String content) {
        content = content.substring(content.indexOf("class=\"lyrics-original\""));
        return content.substring(71, content.indexOf("</p>"))
                .replaceAll("\\\\", "")
                .replaceAll("&quot;", "")
                .replaceAll(" \\?", "")
                .replaceAll("[\";,./*]", "")
                .replaceAll("<br >\r\n[ ]*<br >\r\n", VERSE)
                .replaceAll("<br >\r\n", LINE)
                .replaceAll("<br >\n", LINE)
                .replaceAll("<br >", LINE)
                .replaceAll(" " + LINE, LINE)
                .replaceAll(LINE + " ", LINE)
                .replaceAll(" " + VERSE, VERSE)
                .replaceAll(VERSE + " ", VERSE)
                ;
    }

    //    private String transformContentGroove(String content) {
//        content = content.substring(content.indexOf("mid-content-text"));
//        return content.substring(18, content.indexOf("</div>") - 4)
//                .replaceAll("\\\\", "")
//                .replaceAll("[\";,./*]", "")
//                .replaceAll("<br >\r\n[ ]*<br >\r\n", VERSE)
//                .replaceAll("<br >\r\n", LINE);
//    }

}
