package mining;

import models.Event;
import models.Trace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Extracts the direct follower matrix from a log.
 * Parses each line of the log file as an `Event` and builds the `Trace`s from the events.
 * Then counts all direct follower relations and prints them to the command line.
 */
public class DirectFollowerExtraction {

    public static void main(String[] args) throws Exception {

        // Get the log file from the resources
        File logFile = new File(DirectFollowerExtraction.class.getResource("/IncidentExample.csv").getPath());

        System.out.println("Please use the events in the log-file located at " + logFile.getAbsolutePath() +
                " to build a direct follower matrix.");

        try {
            BufferedReader br = new BufferedReader(new FileReader(logFile));
            br.readLine();

            Stream<Event> events = EventParser.parse(br.lines());
            List<Trace> traces = TraceService.createTraces(events);
            Map<String, Map<String, Integer>> directFollowerMatrix =
                    DirectFollowerExtractService.createDirectFollowerMatrix(traces);

            printDirectFollowerMatrix(directFollowerMatrix);
        } catch (FileNotFoundException ex) {
            System.out.println("File does not exist. Exiting.");
        }
    }

    private static void printDirectFollowerMatrix(Map<String, Map<String, Integer>> directFollowerMatrix) {
        Set<String> headerActivities = directFollowerMatrix.keySet();
        System.out.printf("%30s", "");
        headerActivities.forEach(k -> System.out.printf("%30s", k));

        directFollowerMatrix.forEach((columnHeaderActivity, activities) -> {
            System.out.println();
            System.out.printf("%30s", columnHeaderActivity);

            headerActivities.forEach(activity -> {
                Integer directFollowCount = activities.getOrDefault(activity, 0);
                System.out.printf("%30s", directFollowCount);
            });
        });
    }
}
