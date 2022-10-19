package com.yangmy.cobweb.common.core.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author YangMingYang
 * @since 2022/7/2
 */
public class TimeUtils {

    public static LocalDateTime toLocalDateTime(long timestamp){
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    public static long toTimestamp(LocalDateTime localDateTime){
        return localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    public static String format(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static void main(String[] args) {
        System.out.println(TimeUtils.toLocalDateTime(1660317476473L));
    }

}
