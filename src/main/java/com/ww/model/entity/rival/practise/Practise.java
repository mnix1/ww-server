package com.ww.model.entity.rival.practise;

import com.ww.model.constant.rival.practise.PractiseResult;
import com.ww.model.entity.rival.task.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Practise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long profileId;
    private PractiseResult result = PractiseResult.IN_PROGRESS;
    private Date inProgressDate = new Date();
    private Date closeDate;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;

    public boolean isOpen() {
        return result == PractiseResult.IN_PROGRESS;
    }

    public Long inProgressInterval(){
        return closeDate.getTime() - inProgressDate.getTime();
    }
}
