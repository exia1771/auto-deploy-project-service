package github.exia1771.deploy.common.util;

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

    public static Date expire(Date date, TemporalAmount amount) {
        LocalDateTime localDateTime = toLocalDateTime(date);
        localDateTime.plus(amount);
        return toDate(localDateTime);
    }


}
