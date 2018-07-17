package com.ww.model.entity.rival.task.category;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.task.MusicTrackSource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@ToString
public class MusicTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String author;
    private String name;
    private MusicTrackSource source;
    private String url;
    @Column(length = 4000)
    private String content;
    private Language lang;
}
