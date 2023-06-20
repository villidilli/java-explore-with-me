package ru.practicum.constant;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;


@UtilityClass
public class StatsConstant {
    public final String datetimePattern = "yyyy-MM-dd HH:mm:ss";
    public final Sort sortHitsDesc = Sort.by("hits").descending();
}