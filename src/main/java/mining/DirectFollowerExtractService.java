package mining;

import models.Trace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectFollowerExtractService {

    public static Map<String, Map<String, Integer>> createDirectFollowerMatrix(List<Trace> traces) {
        Map<String, Map<String, Integer>> directFollowerMatrix = new HashMap<>();
        traces.forEach(trace -> trace.getEvents().stream().reduce((event1, event2) -> {
            if (directFollowerMatrix.containsKey(event1.getActivity())) {
                directFollowerMatrix.get(event1.getActivity()).merge(event2.getActivity(), 1, Integer::sum);
            } else {
                Map<String, Integer> activitiesMap = new HashMap<>();
                activitiesMap.put(event2.getActivity(), 1);
                directFollowerMatrix.put(event1.getActivity(), activitiesMap);
            }
            return event2;
        }));

        return directFollowerMatrix;
    }
}
