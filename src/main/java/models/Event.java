package models;

import java.time.ZonedDateTime;

public class Event {
    String traceId;
    String activity;
    ZonedDateTime start;
    ZonedDateTime end;

    public Event(String traceId, String activity, ZonedDateTime start, ZonedDateTime end) {
        this.traceId = traceId;
        this.activity = activity;
        this.start = start;
        this.end = end;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getActivity() {
        return activity;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() { return end; }
}
