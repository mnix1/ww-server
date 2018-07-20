package com.ww.service.rival.task.music;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.MusicTrackSource;
import com.ww.model.entity.rival.task.MusicTrack;
import com.ww.repository.rival.task.category.MusicTrackRepository;
import com.ww.service.rival.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ww.helper.NetworkHelper.downloadContent;

@Service
public class MusicTrackService {

    @Autowired
    MusicTrackRepository musicTrackRepository;

    private static String VERSE = "_V_";
    private static String LINE = "_L_";

    public Boolean addTrack(String author, String name, String url) {
        return addTrack(author, name, url, Language.ALL);
    }

    public Boolean addTrack(String author, String name, String url, Language lang) {
        MusicTrack track = new MusicTrack();
        track.setAuthor(author);
        track.setName(name);
        track.setUrl(url);
        MusicTrackSource source = MusicTrackSource.fromUrl(url);
        track.setSource(source);
        track.setLang(lang);
        String content = downloadContent(url);
        if (content == null) {
            return false;
        }
        if (source == MusicTrackSource.TEKSTOWO) {
            content = transformContentTekstowo(content);
        }
        if (source == MusicTrackSource.ISING) {
            content = transformContentIsing(content);
        }
//        if (source == MusicTrackSource.GROOVE) {
//            content = transformContentGroove(content);
//        }
        track.setContent(content);
        musicTrackRepository.save(track);
        return true;
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
                .replaceAll("<br >", LINE);
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
