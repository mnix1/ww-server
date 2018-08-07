package com.ww.helper;

import com.ww.model.constant.Category;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HeroHobbyConverter implements AttributeConverter<Set<Category>, String> {
    @Override
    public String convertToDatabaseColumn(Set<Category> attribute) {
        return attribute == null ? null : StringUtils.join(attribute, ",");
    }

    @Override
    public Set<Category> convertToEntityAttribute(String dbData) {
        if (StringUtils.isBlank(dbData))
            return new HashSet<>();

        try (Stream<String> stream = Arrays.stream(dbData.split(","))) {
            return stream.map(Category::fromString).collect(Collectors.toSet());
        }
    }
}
