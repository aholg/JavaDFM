package models;

import java.util.List;

public class Trace {
  String traceId;
  List<Event> events;

  public Trace(String traceId, List<Event> events) {
    this.traceId = traceId;
    this.events = events;
  }

  public String getTraceId() {
    return traceId;
  }

  public List<Event> getEvents() {
    return events;
  }
}
