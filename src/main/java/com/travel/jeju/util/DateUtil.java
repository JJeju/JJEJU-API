package com.travel.jeju.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// @Component
@RequiredArgsConstructor
public class DateUtil {

    // private final HttpServletRequest request;
    
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    
    public static final String TIME_ZONE = "x-timezone";

    /**
     * 요청자의 타임존을 반환
     * @return
     */
    public static String getTimeZone(HttpServletRequest request){

        String timeZone = request.getHeader(TIME_ZONE);

        // if(timeZone == null) throw new AppException(ErrorCode.NON_VALID_PARAMETER);

        if(timeZone == null) timeZone = "Asia/Seoul";

        return timeZone;
    }

    /**
     * 현재날짜을 구해서 LocalDate로 반환
     * @return
     */
    public static LocalDate getNowDate(String zoneId){

        return Instant.now().atZone(ZoneId.of(zoneId)).toLocalDate();
    }

    /**
     * 현재시간을 구해서 LocalDate로 반환
     * @return
     */
    public static LocalTime getNowTime(String zoneId){

        return Instant.now().atZone(ZoneId.of(zoneId)).toLocalTime();
    }

    /**
     * 현재날짜시간을 구해서 LocalDate로 반환 
     * @return
     */
    public static LocalDateTime getNowDateTime(String zoneId){

        return Instant.now().atZone(ZoneId.of(zoneId)).toLocalDateTime();
    }

    /**
     * LocalDateTime 을 포밋팅에 맞춰변환시킴
     * @param targetDateTime
     * @return
     */
    public static String parseLocalDateTimeToString(LocalDateTime targetDateTime, String pattern){

        return targetDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDate를 포밋팅에 맞춰변환시킴
     * @param targetDateTime
     * @param pattern
     * @return
     */
    public static String parseLocalDateToString(LocalDate targetDateTime, String pattern){

        return targetDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalTime을 포밋팅에 맞춰변환시킴
     * @param targetDateTime
     * @param pattern
     * @return
     */
    public static String parseLocalTimeToString(LocalTime targetDateTime, String pattern){

        return targetDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * String온 값을 LocalDate로 파싱함
     * @param targetDate
     * @return
     */
    public static LocalDate parseStringToLocalDate(String targetDate) {        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT);

        return LocalDate.parse(targetDate, formatter);
    }

    /**
     * String온 값을 LocalTime로 파싱함
     * @param targetDate
     * @return
     */
    public static LocalTime parseStringToLocalTime(String targetTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtil.TIME_FORMAT);

        return LocalTime.parse(targetTime, formatter);
    }

    /**
     * String온 값을 LocalDateTime로 파싱함
     * @param targetDate
     * @return
     */
    public static LocalDateTime parseStringToLocalDateTime(String targetDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateUtil.DATE_TIME_FORMAT);

        return LocalDateTime.parse(targetDateTime, formatter);
    }
    
    /**
     * LocalTime을 Instant로 변환 [기본 오늘날짜]
     * @param targetTime
     * @return
     */
    public static Instant localTimeToInstant(LocalTime targetTime, String zoneId){

        return targetTime.atDate(DateUtil.getNowDate(zoneId)).atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * LocalTime을 Instant로 변환 [원하는 날짜]
     * @param targetTime
     * @return
     */
    public static Instant localTimeToInstant(LocalDate targetDate, LocalTime targetTime){

        return targetTime.atDate(targetDate).atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * LocalDate를 Instant로 변환
     * @param targetDate
     * @return
     */
    public static Instant localDateToInstant(LocalDate targetDate){

        return targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }

    /**
     * LocalDateTime을 Instant로 반환
     * @param targetDateTime
     * @return
     */
    public static Instant localDateTimeToInstant(LocalDateTime targetDateTime){

        return targetDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * Instant를 LocalDate로 반환
     * @param instant
     * @return
     */
    public static LocalDate instantToLocalDate(Instant instant, String zoneId) {

        return instant.atZone(ZoneId.of(zoneId)).toLocalDate();
    }

    /**
     * Instant를 LocalTime로 반환
     * @param instant
     * @return
     */
    public static LocalTime instantToLocalTime(Instant instant, String zoneId) {

        return instant.atZone(ZoneId.of(zoneId)).toLocalTime();
    }

    /**
     * Instant를 LocalDateTime로 반환
     * @param instant
     * @return
     */
    public static LocalDateTime instantToLocalDateTime(Instant instant, String zoneId) {
        return instant.atZone(ZoneId.of(zoneId)).toLocalDateTime();
    }

    /**
     * Instant를 LocalDateTime로 반환
     * @param instant
     * @return
     */
    public static LocalDateTime instantToLocalDateTime(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}   
