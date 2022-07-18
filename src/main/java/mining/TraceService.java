package mining;

import models.Event;
import models.Trace;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class TraceService {

    public static List<Trace> createTraces(Stream<Event> events) {
        return events
                .collect(groupingBy(Event::getTraceId,
                        Collectors.toCollection(() ->
                                new TreeSet<>(new EventComparable()))))
                .entrySet()
                .stream()
                .map(es -> new Trace(es.getKey(), new ArrayList<>(es.getValue())))
                .collect(Collectors.toList());
    }

    private static class EventComparable implements Comparator<Event> {

        @Override
        public int compare(Event o1, Event o2) {
            boolean isDuplicate = (o1.getStart().isEqual(o2.getStart()))
                    && (o1.getActivity().equals(o2.getActivity())
                    && (o1.getTraceId().equals(o2.getTraceId())));

            if (isDuplicate) {
                return 0;
            } else if (o1.getStart().isAfter(o2.getStart())) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
