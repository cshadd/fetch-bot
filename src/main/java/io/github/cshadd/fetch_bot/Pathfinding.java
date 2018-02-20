package io.github.cshadd.fetch_bot;
import io.github.cshadd.cshadd_java_data_structures.util.*;

// Main
public class Pathfinding
implements FetchBot {
    // Private Final Instance/Property Fields
    private final Graph paths = new UndirectedGraph();
    private final Stack backTrackStack = new Stack();
}