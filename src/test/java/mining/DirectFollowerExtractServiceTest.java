package mining;

import models.Event;
import models.Trace;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class DirectFollowerExtractServiceTest {

    @Test
    public void shouldReturnEmptyMatrix() {
        Map<String, Map<String, Integer>> directFollowerMatrix =
                DirectFollowerExtractService.createDirectFollowerMatrix(Collections.emptyList());

        assertEquals(Collections.emptyMap(), directFollowerMatrix);
    }

    @Test
    public void shouldReturnEmptyMatrixForASingleEvent() {
        String traceId = "someTrace";
        List<Trace> traces = Collections.singletonList(
                new Trace(traceId, Collections.singletonList(new Event(traceId, "someActivity", ZonedDateTime.now()))));

        Map<String, Map<String, Integer>> directFollowerMatrix =
                DirectFollowerExtractService.createDirectFollowerMatrix(traces);

        assertEquals(Collections.emptyMap(), directFollowerMatrix);
    }

    @Test
    public void shouldReturnDirectFollowerMatrixForTwoEvents() {
        String traceId = "someTrace";
        String activity1 = "someActivity1";
        String activity2 = "someActivity2";
        List<Trace> traces = Collections.singletonList(
                new Trace(traceId, Arrays.asList(
                        new Event(traceId, activity1, ZonedDateTime.now()),
                        new Event(traceId, activity2, ZonedDateTime.now()))));

        Map<String, Map<String, Integer>> directFollowerMatrix =
                DirectFollowerExtractService.createDirectFollowerMatrix(traces);

        Map<String, Map<String, Integer>> expected = new HashMap<>();
        HashMap<String, Integer> expectedActivities = new HashMap<>();
        expectedActivities.put(activity2, 1);
        expected.put(activity1, expectedActivities);

        assertEquals(expected, directFollowerMatrix);
    }

    @Test
    public void shouldReturnDirectFollowerMatrixForEventsFollowingOverMultipleTraces() {
        String traceId = "someTrace";
        String activity1 = "someActivity1";
        String activity2 = "someActivity2";
        Trace trace = new Trace(traceId, Arrays.asList(
                new Event(traceId, activity1, ZonedDateTime.now()),
                new Event(traceId, activity2, ZonedDateTime.now())));
        List<Trace> traces = Arrays.asList(trace, trace);

        Map<String, Map<String, Integer>> directFollowerMatrix =
                DirectFollowerExtractService.createDirectFollowerMatrix(traces);

        Map<String, Map<String, Integer>> expected = new HashMap<>();
        HashMap<String, Integer> expectedActivities = new HashMap<>();
        expectedActivities.put(activity2, 2);
        expected.put(activity1, expectedActivities);

        assertEquals(expected, directFollowerMatrix);
    }

    @Test
    public void shouldReturnDirectFollowerMatrixForMultipleTracesWithoutRelations() {
        String traceId = "someTrace";
        String activity1 = "someActivity1";
        String activity2 = "someActivity2";
        String activity3 = "someActivity3";
        Trace trace1 = new Trace(traceId, Arrays.asList(
                new Event(traceId, activity1, ZonedDateTime.now()),
                new Event(traceId, activity2, ZonedDateTime.now())));
        Trace trace2 = new Trace(traceId, Arrays.asList(
                new Event(traceId, activity3, ZonedDateTime.now()),
                new Event(traceId, activity2, ZonedDateTime.now())));
        List<Trace> traces = Arrays.asList(trace1, trace2);

        Map<String, Map<String, Integer>> directFollowerMatrix =
                DirectFollowerExtractService.createDirectFollowerMatrix(traces);

        Map<String, Map<String, Integer>> expected = new HashMap<>();
        HashMap<String, Integer> expectedActivities1 = new HashMap<>();
        HashMap<String, Integer> expectedActivities2 = new HashMap<>();

        expectedActivities1.put(activity2, 1);
        expectedActivities2.put(activity2, 1);

        expected.put(activity1, expectedActivities1);
        expected.put(activity3, expectedActivities2);

        assertEquals(expected, directFollowerMatrix);
    }
}