package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.cshadd_java_data_structures.util.Graph;
import io.github.cshadd.cshadd_java_data_structures.util.UndirectedGraph;

// Main
public abstract class AbstractPathfindController
extends AbstractController
implements PathfindController {
    // Private Final Instance/Property Fields
    private final Graph<CartesianCoordinate> cartesianSystem;

    // Private Instance/Property Fields
    private int currentDirection;
    
    // Protected Constructors
    protected AbstractPathfindController() {
        super();
        cartesianSystem = new UndirectedGraph<CartesianCoordinate>();
        currentDirection = 0;
    }
    
    // Private Static Final Nested Classes
    private static final class CartesianCoordinate
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
}