package mining;

import models.Event;
import models.Trace;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TraceServiceTest {

    @Test
    public void shouldReturnEmptyForEmptyInput() {
        List<Trace> result = TraceService.createTraces(Collections.emptyList(), ZonedDateTime.now(), ZonedDateTime.now());

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void shouldCreateATrace() {
        ZonedDateTime now = ZonedDateTime.now();
        Event expectedEvent = new Event("someTraceId", "someActivity", now, now);
        List<Event> events = Collections.singletonList(expectedEvent);
        List<Trace> result = TraceService.createTraces(events, ZonedDateTime.now().minusHours(2), ZonedDateTime.now().plusHours(1));

        assertFalse(result.isEmpty());
        assertFalse(result.get(0).getEvents().isEmpty());
        assertTrue(result.get(0).getEvents().contains(expectedEvent));
    }

    @Test
    public void shouldGroupEventsWithSameTraceIds() {
        ZonedDateTime now = ZonedDateTime.now();
        Event event1 = new Event("traceId1", "someActivity1", now, now);
        Event event2 = new Event("traceId1", "someActivity2", now, now);
        List<Event> events = Arrays.asList(event1, event2);
        List<Trace> result = TraceService.createTraces(events, ZonedDateTime.now().minusHours(2), ZonedDateTime.now().plusHours(1));

        assertEquals(1, result.size());
        assertSame("traceId1", result.get(0).getTraceId());

        List<Event> eventResults = result.get(0).getEvents();
        assertTrue(eventResults.contains(event1));
        assertTrue(eventResults.contains(event2));
    }

    @Test
    public void shouldSortGroupedEventsWithOldestFirst() {
        ZonedDateTime now = ZonedDateTime.now();
        Event event1 = new Event("traceId1", "someActivity1", now.minusHours(1), now);
        Event event2 = new Event("traceId1", "someActivity2", now, now);
        Event event3 = new Event("traceId1", "someActivity2", now.minusHours(10), now);
        List<Event> events = Arrays.asList(event1, event2, event3);
        List<Trace> result = TraceService.createTraces(events, ZonedDateTime.now().minusHours(20), ZonedDateTime.now().plusHours(1));

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
        Event event1 = new Event("traceId1", "someActivity1", now.minusHours(1), now);
        Event event2 = new Event("traceId2", "someActivity2", now, now);
        List<Event> events = Arrays.asList(event1, event2);
        List<Trace> result = TraceService.createTraces(events, ZonedDateTime.now().minusHours(2), ZonedDateTime.now().plusHours(1));

        assertEquals(2, result.size());
    }

    @Test
    public void shouldFilterOutEventsThatIsOutsideTimeIntervall() {
        ZonedDateTime now = ZonedDateTime.now();
        Event event1 = new Event("traceId1", "someActivity1", now.minusHours(1), now);
        Event event2 = new Event("traceId2", "someActivity2", now.minusHours(3), now);
        List<Event> events = Arrays.asList(event1, event2);
        List<Trace> result = TraceService.createTraces(events, ZonedDateTime.now().minusHours(2), ZonedDateTime.now().plusHours(1));

        assertEquals(1, result.size());
    }
}
