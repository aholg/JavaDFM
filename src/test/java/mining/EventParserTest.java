package mining;

import models.Event;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class EventParserTest {

    @Test
    public void shouldReturnEmptyForEmptyInput() {
        Stream<String> lines = Stream.empty();
        List<Event> result = EventParser.parse(lines);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void shouldExtractEvents() {
        Stream<String> lines = Stream
                .of("trace_0,Resolution and recovery,2016/01/05 03:56:44.000,2016/01/05 04:30:44.000");
        ZonedDateTime expectedStartDate = ZonedDateTime.of(2016, 1, 5, 3, 56, 44, 0, ZoneOffset.UTC);
        ZonedDateTime expectedEndDate = ZonedDateTime.of(2016, 1, 5, 4, 30, 44, 0, ZoneOffset.UTC);
        Event expected = new Event("trace_0", "Resolution and recovery", expectedStartDate, expectedEndDate);
        List<Event> result = EventParser.parse(lines);

        assertEquals(expected.getTraceId(), result.get(0).getTraceId());
        assertEquals(expected.getActivity(), result.get(0).getActivity());
        assertEquals(expected.getStart(), result.get(0).getStart());
        assertEquals(expected.getEnd(), result.get(0).getEnd());
    }

    @Test
    public void shouldUseDefaultStartDateWhenDateParseFailed() {
        Stream<String> lines = Stream
                .of("trace_0,Resolution and recovery,,\\s");
        ZonedDateTime expectedStartDate = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime expectedEndDate = ZonedDateTime.now(ZoneOffset.UTC);
        Event expected = new Event("trace_0", "Resolution and recovery", expectedStartDate, expectedEndDate);
        List<Event> result = EventParser.parse(lines);

        assertEquals(expected.getStart().toLocalDate(), result.get(0).getStart().toLocalDate());
        assertEquals(expected.getEnd().toLocalDate(), result.get(0).getEnd().toLocalDate());
    }
}
