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
        List<Event> result = EventParser.parse(lines).collect(Collectors.toList());

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void shouldExtractEvents() {
        Stream<String> lines = Stream
                .of("trace_0,Resolution and recovery,2016/01/05 03:56:44.000,2016/01/05 04:30:44.000");
        ZonedDateTime expectedDate = ZonedDateTime.of(2016, 1, 5, 3, 56, 44, 0, ZoneOffset.UTC);
        Event expected = new Event("trace_0", "Resolution and recovery", expectedDate);
        List<Event> result = EventParser.parse(lines).collect(Collectors.toList());

        assertEquals(expected.getTraceId(), result.get(0).getTraceId());
        assertEquals(expected.getActivity(), result.get(0).getActivity());
        assertEquals(expected.getStart(), result.get(0).getStart());
    }

    @Test
    public void shouldUseDefaultStartDateWhenDateParseFailed() {
        Stream<String> lines = Stream
                .of("trace_0,Resolution and recovery,,2016/01/05 04:30:44.000");
        ZonedDateTime expectedDate = ZonedDateTime.now(ZoneOffset.UTC);
        Event expected = new Event("trace_0", "Resolution and recovery", expectedDate);
        List<Event> result = EventParser.parse(lines).collect(Collectors.toList());

        assertEquals(expected.getStart().toLocalDate(), result.get(0).getStart().toLocalDate());
    }
}
