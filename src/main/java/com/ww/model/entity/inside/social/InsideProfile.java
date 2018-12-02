package com.ww.model.entity.inside.social;

import com.ww.helper.WisieHobbyConverter;
import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.WisorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;

import static com.ww.helper.RandomHelper.*;

@Getter
@NoArgsConstructor
@Entity
public class InsideProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Boolean admin = false;
    private Boolean auto = false;
    private WisorType wisorType = WisorType.random();
    private Double reflex;
    private Double speed;
    private Double wisdom;
    private Double cunning;
    private Double luck;
    @Column
    @Convert(converter = WisieHobbyConverter.class)
    protected HashSet<Category> hobbies;

    public InsideProfile(String username, String password, Boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public InsideProfile(String username, BCryptPasswordEncoder encoder) {
        this.username = username;
        this.password = encoder.encode("1" + username + "auto");
        this.auto = true;
    }

    public InsideProfile initStats(Double reflex, Double speed, Double wisdom, Double cunning, Double luck, HashSet<Category> hobbies) {
        this.reflex = reflex;
        this.speed = speed;
        this.wisdom = wisdom;
        this.cunning = cunning;
        this.luck = luck;
        this.hobbies = hobbies;
        return this;
    }

    public InsideProfile initStats(Double reflex, Double speed, Double wisdom, Double cunning, Double luck, int hobbiesCount) {
        return initStats(reflex, speed, wisdom, cunning, luck, new HashSet<>(randomElements(Category.list(), hobbiesCount)));
    }

    public InsideProfile initStats() {
        return initStats(randomDouble(7),
                randomDouble(8),
                randomDouble(10),
                randomDouble(6),
                randomDouble(4),
                randomInteger(2, 6));
    }

    public InsideProfile setWisorType(WisorType wisorType) {
        this.wisorType = wisorType;
        return this;
    }
}
