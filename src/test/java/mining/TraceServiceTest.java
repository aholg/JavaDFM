package mining;

import models.Event;
import models.Trace;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TraceServiceTest {

    @Test
    public void shouldReturnEmptyForEmptyInput() {
        List<Trace> result = TraceService.createTraces(Stream.empty());

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void shouldCreateATrace() {
        Event expectedEvent = new Event("someTraceId", "someActivity", ZonedDateTime.now());
        Stream<Event> events = Stream.of(expectedEvent);
        List<Trace> result = TraceService.createTraces(events);

        assertFalse(result.isEmpty());
        assertFalse(result.get(0).getEvents().isEmpty());
        assertTrue(result.get(0).getEvents().contains(expectedEvent));
    }

    @Test
    public void shouldGroupEventsWithSameTraceIds() {
        ZonedDateTime now = ZonedDateTime.now();
        Event event1 = new Event("traceId1", "someActivity1", now);
        Event event2 = new Event("traceId1", "someActivity2", now);
        Stream<Event> events = Stream.of(event1, event2);
        List<Trace> result = TraceService.createTraces(events);

        assertEquals(1, result.size());
        assertSame("traceId1", result.get(0).getTraceId());

        List<Event> eventResults = result.get(0).getEvents();
        assertTrue(eventResults.contains(event1));
        assertTrue(eventResults.contains(event2));
    }

    @Test
    public void shouldSortGroupedEventsWithOldestFirst() {
        ZonedDateTime now = ZonedDateTime.now();
        Event event1 = new Event("traceId1", "someActivity1", now.minusHours(1));
        Event event2 = new Event("traceId1", "someActivity2", now);
        Event event3 = new Event("traceId1", "someActivity2", now.minusHours(10));
        Stream<Event> events = Stream.of(event1, event2, event3);
        List<Trace> result = TraceService.createTraces(events);

        assertEquals(1, result.size());
        assertSame("traceId1", result.get(0).getTraceId());

        List<Event> eventResults = result.get(0).getEvents();
        assertEquals(3, eventResults.size());
        assertSame(eventResults.get(0), event3);
        assertSame(eventResults.get(1), event1);
        assertSame(eventResults.get(2), event2);
    }

    @Test
    public void shouldCreateANewTraceForDifferentTraceIds() {
        ZonedDateTime now = ZonedDateTime.now();
        Event event1 = new Event("traceId1", "someActivity1", now.minusHours(1));
        Event event2 = new Event("traceId2", "someActivity2", now);
        Stream<Event> events = Stream.of(event1, event2);
        List<Trace> result = TraceService.createTraces(events);

        assertEquals(2, result.size());
    }
}
