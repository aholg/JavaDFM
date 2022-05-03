package mining;

import java.io.File;

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

    /////////////////////////////
    /// YOUR WORK STARTS HERE ///
    /////////////////////////////
  }
}
