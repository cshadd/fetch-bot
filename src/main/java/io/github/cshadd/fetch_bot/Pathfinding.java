package io.github.cshadd.fetch_bot;
import io.github.cshadd.cshadd_java_data_structures.util.*;

// Main
public class Pathfinding
implements FetchBot {
    // Private Final Instance/Property Fields
    private final Graph<Coordinate> paths;
    private final Stack<Coordinate> backTrackStack;

    // Public Constructors
    public Pathfinding() {
        paths = new UndirectedGraph<Coordinate>();
        backTrackStack = new Stack<Coordinate>();
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
}
