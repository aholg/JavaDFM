package mining;

import models.Event;
import models.Trace;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extracts the direct follower matrix from a log.
 * Parses each line of the log file as an `Event` and builds the `Trace`s from the events.
 * Then counts all direct follower relations and prints them to the command line.
 */
public class DirectFollowerExtraction {

    public static void main(String[] args) throws Exception {

        // Get the log file from the resources
        InputStreamReader logFile = new InputStreamReader(DirectFollowerExtraction.class.getResourceAsStream("/IncidentExample.csv"));

        System.out.println("Please use the events in the log-file located at /IncidentExample.csv" +
                " to build a direct follower matrix.");

        try {
            BufferedReader br = new BufferedReader(logFile);
            br.readLine();
            List<Event> events = EventParser.parse(br.lines());
            BufferedReader cmdReader = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                ZonedDateTime startDate = readInput("Enter start date in format yyyy/MM/dd HH:mm:ss. Or type 'exit' to exit.", cmdReader);
                ZonedDateTime endDate = readInput("Enter end date in format yyyy/MM/dd HH:mm:ss. Or type 'exit' to exit.", cmdReader);

                List<Trace> traces = TraceService.createTraces(events, startDate, endDate);
                Map<String, Map<String, Integer>> directFollowerMatrix =
                        DirectFollowerExtractService.createDirectFollowerMatrix(traces);

                printDirectFollowerMatrix(directFollowerMatrix);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File does not exist. Exiting.");
        }
    }

    private static ZonedDateTime readInput(String promptMessage, BufferedReader cmdReader) throws IOException {
        System.out.println(promptMessage);
        String input = cmdReader.readLine();

        if (input.equals("exit")) {
            System.exit(0);
        }

        try {
            return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")).atZone(ZoneOffset.UTC);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid date format. Please use format yyyy/MM/dd HH:mm:ss");
            return readInput(promptMessage, cmdReader);
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
        System.out.println();
    }
}
