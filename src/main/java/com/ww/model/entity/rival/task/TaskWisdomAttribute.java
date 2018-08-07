package com.ww.model.entity.rival.task;


import com.ww.model.constant.hero.WisdomAttribute;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class TaskWisdomAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private WisdomAttribute wisdomAttribute;
    private Double value;
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false, updatable = false)
    private TaskType type;

    public TaskWisdomAttribute(WisdomAttribute wisdomAttribute, Double value) {
        this.wisdomAttribute = wisdomAttribute;
        this.value = value;
    }
}
