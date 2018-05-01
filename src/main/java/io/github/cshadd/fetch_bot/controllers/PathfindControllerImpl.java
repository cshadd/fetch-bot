package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.cshadd_java_data_structures.util.Graph;
import io.github.cshadd.cshadd_java_data_structures.util.UndirectedGraph;
import java.util.ArrayList;
import java.util.List;

// Main
public class PathfindControllerImpl
implements PathfindController {
    // Private Constant Instance/Property Fields
    
    
    // Private Final Instance/Property Fields
    private final Graph<CartesianCoordinate> cartesianSystem;
    private final List<CartesianCoordinate> defaultCoords;

    // Private Instance/Property Fields
    private int currentDirection;
    
    // Public Constructors
    public PathfindControllerImpl() {
        cartesianSystem = new UndirectedGraph<CartesianCoordinate>();
        defaultCoords = new ArrayList<CartesianCoordinate>();
        // x
        defaultCoords.add(new CartesianCoordinate(-1, 0));
        defaultCoords.add(new CartesianCoordinate(1, 0));
        // Origin
        defaultCoords.add(new CartesianCoordinate(0, 0));
        // Y
        defaultCoords.add(new CartesianCoordinate(0, -1));
        defaultCoords.add(new CartesianCoordinate(0, 1));

        currentDirection = 0;
        cartesianSystem.addEdge(defaultCoords.get(0), defaultCoords.get(2));
        cartesianSystem.addEdge(defaultCoords.get(1), defaultCoords.get(2));
        cartesianSystem.addEdge(defaultCoords.get(3), defaultCoords.get(2));
        cartesianSystem.addEdge(defaultCoords.get(4), defaultCoords.get(2));
    }
    
    // Protected Static Final Nested Classes
    protected static final class CartesianCoordinate
    implements Comparable<CartesianCoordinate> {
        // Private Instance/Property Fields
        private boolean blocked;
        private double x;
        private double y;
        
        // Public Constructors
        public CartesianCoordinate() {
            this(0, 0);
        }
        public CartesianCoordinate(double x, double y) {
            blocked = false;
            this.x = x;
            this.y = y;
        }
        
        // Public Property Accessor Methods
        public boolean getBlocked() {
            return blocked;
        }
        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }

        // Public Property Mutator Methods
        public void setBlocked(boolean state) {
            blocked = state;
        }
        public void setX(double x) {
            this.x = x;
        }
        public void setY(double y) {
            this.y = y;
        }
        
        // Public Methods
        public CartesianCoordinate invertedCoordinate() {
            return new CartesianCoordinate(x*-1, y*-1);
        }
        
        // Public Methods (Overrided)
        @Override
        public int compareTo(CartesianCoordinate coord) {
            final double otherX = coord.getX();
            final double otherY = coord.getY();
            final double distance = Math.sqrt(((x - otherX)*(x - otherX)) + ((y - otherY)*(y - otherY)));
            int returnData = 0;
            
            if (distance < 0) {
                returnData = -1;
            }
            else if (distance > 0) {
                returnData = 1;
            }
            else {
                returnData = 0;
            }
            return returnData;
        }
    }
    
    // Private Methods
    private CartesianCoordinate directionCoordinate() {
        final double a = currentDirection*((Math.PI)/180);
        return getCoordinateFromDefault(Math.sin(a), Math.cos(a));
    }
    private CartesianCoordinate getCoordinateFromDefault(double x, double y) {
        CartesianCoordinate returnData = null;
        for (int i = 0; i < defaultCoords.size(); i++) {
            CartesianCoordinate coord = defaultCoords.get(i);
            if (coord != null) {
                if (coord.getX() == x && coord.getY() == y) {
                    returnData = coord;
                    break;
                }
            }
        }
        return returnData;
    }
    
    // Public Methods
    public void block(boolean state) {
        final CartesianCoordinate coord = directionCoordinate();
        coord.setBlocked(state);
    }
    public void process() { // Algorithm?
        /*
         * If directionCoordinate is not blocked
         *      If directionCoordinate is not visited on graph
         *          If found
         *              Happy
         *              Stop
         *              End
         *          Else
         *              Unvisit all
         *              Marked inverse of directionCoordinate as visited on graph
         *              Neutral
         *              Move forward
         *              Stop
         *              Unblock all
         *      Else
         *          If found
         *              Happy
         *              Stop
         *              End
         *          Else
         *              If all are visited
         *                  Sad
         *                  Stop
         *                  End
         *              Else
         *                  Sad
         *                  Stop
         *                  Choose another directionCoordinate
         *                  Rotate to directionCoordinate
         * Else if blocked
         *      If found
         *          Happy
         *          Stop
         *          End
         *      Else
         *          If all are blocked
         *              Angry
         *              Stop
         *              End
         *          Else
         *              Angry
         *              Stop
         *              Choose another directionCoordinate
         *              Rotate to directionCoordinate
         */
    }
}