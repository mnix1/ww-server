package com.ww.model.entity.rival.task;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.TrackSource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static com.ww.helper.StringHelper.replaceAllNonAlphaumeric;

@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String author;
    private String name;
    private TrackSource source;
    private String url;
    @Column(length = 4000)
    private String content;
    private Language lang;

    public String getContentResourcePath() {
        return "task/music-track-content/" + replaceAllNonAlphaumeric(author) + "_" + replaceAllNonAlphaumeric(name) + ".txt";
    }
}
