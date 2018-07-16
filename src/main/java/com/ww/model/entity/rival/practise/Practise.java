package com.ww.model.entity.rival.practise;

import com.ww.model.constant.PractiseResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Practise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long profileId;
    private PractiseResult result = PractiseResult.OPEN;
    private Date openDate = new Date();
    private Date closeDate;
    @OneToMany(mappedBy = "practise", fetch = FetchType.LAZY)
    private Set<PractiseQuestion> questions;

    public boolean isOpen() {
        return result == PractiseResult.OPEN;
    }
}
