package io.github.cshadd.fetch_bot;
import java.io.*;
import org.apache.commons.io.*;

// Main
public class Core
implements FetchBot {
    // Private Final Instance/Property Fields
    private final Pathfinding pathfind = new Pathfinding();

    // Entry Point
    public static void main(String[] args) {
        FileUtils.deleteQuietly(new File("FetchBot.log"));
        Logger.info("Fetch Bot starting!");
        Logger.info("Fetch Bot terminating!");
    }
}
