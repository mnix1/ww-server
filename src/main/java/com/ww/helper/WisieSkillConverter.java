package com.ww.helper;

import com.ww.model.constant.Skill;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WisieSkillConverter implements AttributeConverter<HashSet<Skill>, String> {
    @Override
    public String convertToDatabaseColumn(HashSet<Skill> attribute) {
        return attribute == null ? null : StringUtils.join(attribute, ",");
    }

    @Override
    public HashSet<Skill> convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData))
            return new HashSet<>();

        try (Stream<String> stream = Arrays.stream(dbData.split(","))) {
            return stream.map(Skill::fromString).collect(Collectors.toCollection(HashSet::new));
        }
    }
}
