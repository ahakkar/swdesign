package fi.nordicwatt.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeConverter {

    public static LocalDateTime finnishTimeToGMTTime(LocalDateTime finnishTime) {
        ZoneId sourceZone = ZoneId.of("Europe/Helsinki");
        ZonedDateTime sourceZonedDateTime = ZonedDateTime.of(finnishTime, sourceZone);
        ZoneId targetZone = ZoneId.of("GMT");
        ZonedDateTime targetZonedDateTime = sourceZonedDateTime.withZoneSameInstant(targetZone);
        return targetZonedDateTime.toLocalDateTime();
    }

    public static LocalDateTime gmtTimeToFinnishTime(LocalDateTime gmtTime) {
        ZoneId sourceZone = ZoneId.of("GMT");
        ZonedDateTime sourceZonedDateTime = ZonedDateTime.of(gmtTime, sourceZone);
        ZoneId targetZone = ZoneId.of("Europe/Helsinki");
        ZonedDateTime targetZonedDateTime = sourceZonedDateTime.withZoneSameInstant(targetZone);
        return targetZonedDateTime.toLocalDateTime();
    }
}
