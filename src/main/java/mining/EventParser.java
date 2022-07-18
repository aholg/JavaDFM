package mining;

import models.Event;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

public class EventParser {

    public static Stream<Event> parse(Stream<String> lines) {
        return lines
                .map(line -> line.split(","))
                .map(EventParser::extractEvent);
    }

    private static Event extractEvent(String[] values) {
        String traceId = values[0];
        String activity = values[1];
        ZonedDateTime startDate = parseDate(values[2]);

        return new Event(traceId, activity, startDate);
    }

    private static ZonedDateTime parseDate(String values) {
        ZonedDateTime date;
        try {
            date = LocalDateTime.parse(values, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")).atZone(ZoneOffset.UTC);
        } catch (DateTimeParseException ex) {
            date = ZonedDateTime.now(ZoneOffset.UTC);
        }
        return date;
    }
}
