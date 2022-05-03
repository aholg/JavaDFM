package models;

import java.time.ZonedDateTime;

public class Event {
  String traceId;
  String activity;
  ZonedDateTime start;

  public Event(String traceId, String activity, ZonedDateTime start) {
    this.traceId = traceId;
    this.activity = activity;
    this.start = start;
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
}
