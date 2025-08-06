package com.sicua.interfaces.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/test")
public class TimezoneTestController {

    @GetMapping("/timezone")
    public Map<String, Object> getTimezoneInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // System timezone
        info.put("systemDefaultTimezone", TimeZone.getDefault().getID());
        info.put("systemDefaultTimezoneOffset", TimeZone.getDefault().getRawOffset() / (1000 * 60 * 60)); // hours
        
        // Current times
        info.put("localDateTime_now", LocalDateTime.now());
        info.put("zonedDateTime_systemDefault", ZonedDateTime.now());
        info.put("zonedDateTime_lima", ZonedDateTime.now(ZoneId.of("America/Lima")));
        info.put("localDateTime_from_lima", ZonedDateTime.now(ZoneId.of("America/Lima")).toLocalDateTime());
        
        // JVM timezone
        info.put("jvmTimezone", System.getProperty("user.timezone"));
        
        return info;
    }
}
