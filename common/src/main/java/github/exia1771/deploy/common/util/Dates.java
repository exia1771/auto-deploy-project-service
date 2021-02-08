package github.exia1771.deploy.common.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Date;

public abstract class Dates {

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static Date now() {
        return toDate(LocalDateTime.now());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE_ID).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZONE_ID);
    }

    public static LocalDateTime toLocalDateTime(Long timestamp) {
        return toLocalDateTime(new Date(timestamp));
    }

    public static Date plus(Date date, TemporalAmount amount) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        return toDate(localDateTime.plus(amount));
    }

    public static Duration minus(Date start, Date end) {
        return minus(Duration.ofMillis(start.getTime()), Duration.ofMillis(end.getTime()));
    }

    public static Duration minus(Date start, TemporalAmount end) {
        return minus(Duration.ofMillis(start.getTime()), end);
    }

    public static Duration minus(TemporalAmount start, TemporalAmount end) {
        return Duration.from(end).minus(Duration.from(start));
    }

}
