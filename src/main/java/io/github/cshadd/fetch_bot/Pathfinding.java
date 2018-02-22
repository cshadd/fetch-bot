package io.github.cshadd.fetch_bot;
import io.github.cshadd.cshadd_java_data_structures.util.*;

// Main
public class Pathfinding
implements FetchBot, Runnable {
    // Private Constant Instance/Property Fields
    private static final int DIV = 10;

    // Private Final Instance/Property Fields
    private final Stack<Coordinate> backTrackStack;
    private final int maxX;
    private final int maxY;
    private final DirectedGraph<Coordinate> paths;

    // Public Constructors
    public Pathfinding() {
        this(0, 0);
    }
    public Pathfinding(int maxX, int maxY) {
        backTrackStack = new Stack<Coordinate>();
        this.maxX = maxX/DIV;
        this.maxY = maxY/DIV;
        paths = new DirectedGraph<Coordinate>();
        Thread t = new Thread(this);
        t.start();
    }

    // Protected Static Final Nested Classes
    protected static final class Coordinate
    implements Comparable<Coordinate> {
        // Private Instance/Property Fields
        private int x = 0;
        private int y = 0;

        // Public Constructors
        public Coordinate() {
            this(0, 0);
        }
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // Public Property Accessor Methods
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }

        // Public Property Mutator Methods
        public void setX(int x) {
            this.x = x;
        }
        public void setY(int y) {
            this.y = y;
        }

        // Public Methods
        public Coordinate coordinateDown() {
            return new Coordinate(x, y - 1);
        }
        public Coordinate coordinateDownLeft() {
            return new Coordinate(x - 1, y - 1);
        }
        public Coordinate coordinateDownRight() {
            return new Coordinate(x + 1, y - 1);
        }
        public Coordinate coordinateLeft() {
            return new Coordinate(x - 1, y);
        }
        public Coordinate coordinateRight() {
            return new Coordinate(x + 1, y);
        }
        public Coordinate coordinateUp() {
            return new Coordinate(x, y + 1);
        }
        public Coordinate coordinateUpLeft() {
            return new Coordinate(x - 1, y + 1);
        }
        public Coordinate coordinateUpRight() {
            return new Coordinate(x + 1, y + 1);
        }
        public float length() {
            return (float)Math.sqrt(x*x + y*y);
        }

        // Public Methods (Overrided)
        @Override
        public int compareTo(Coordinate other) {
            float l = length();
            float l2 = other.length();
            if (l < l2) {
                return -1;
            }
            else if (l > l2) {
                return 1;
            }
            else {
                return 0;
            }
        }
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    // Public Methods
    @Override
    public void run() {
        try {
            for (int i = -maxX; i < maxX; i++) {
                for (int i2 = -maxY; i2 < maxY; i2++) {
                    final Coordinate coord = new Coordinate(i, i2);
                    paths.addEdge(coord, coord.coordinateDown());
                    paths.addEdge(coord, coord.coordinateLeft());
                    paths.addEdge(coord, coord.coordinateRight());
                    paths.addEdge(coord, coord.coordinateUp());
                }
            }
        }
        catch (OutOfMemoryError e) {
            System.err.println("Error: Out of memory.");
            e.printStackTrace();
        }
        catch (Exception e) {
            System.err.println("Error: Unspecified.");
            e.printStackTrace();
        }
        finally {
            System.out.println(paths.toStringFull());
        }

    }
}
