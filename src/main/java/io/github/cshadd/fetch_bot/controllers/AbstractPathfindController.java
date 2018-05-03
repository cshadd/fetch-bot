package io.github.cshadd.fetch_bot.controllers;
import java.util.ArrayList;
import java.util.List;

import io.github.cshadd.cshadd_java_data_structures.util.UndirectedGraph;

// Main
public abstract class AbstractPathfindController
extends AbstractController
implements PathfindController {
    // protected Constant Instance/Property Fields
    protected static final int COORD_MAX_RANGE = 10;
    protected static final int ROT_ADD = 90;
    protected static final int ROT_MAX_RANGE = 360;
    
    // Protected Final Instance/Property Fields
    protected final CartesianGraph cartesianGraph;
    
    // Private Instance/Property Fields
    private int currentRot;

    // Protected Constructors
    protected AbstractPathfindController() {
        super();
        cartesianGraph = new CartesianGraph();
        currentRot = 0;
    }
    
    // Protected Static Final Nested Classes
    protected static final class CartesianGraph
    extends UndirectedGraph<CartesianGraph.CartesianCoordinate> {
        // Private Instance/Property Fields
        private List<CartesianCoordinate> blockedCoords;
        private CartesianCoordinate currentCoord;
        
        // Private Constructors
        private CartesianGraph() {
            this(COORD_MAX_RANGE);
        }
        private CartesianGraph(int maxRange) {
            super();
            blockedCoords = new ArrayList<CartesianCoordinate>();
            for (int i = -maxRange; i <= maxRange; i++) {
                for (int i2 = -maxRange; i2 <= maxRange; i2++) {
                    final CartesianCoordinate coord = new CartesianCoordinate(i, i2);
                    addVertex(coord);
                    if ((i + 1) <= maxRange) {
                        addEdge(coord, coord.up());
                    }
                    if ((i - 1) >= -maxRange) {
                        addEdge(coord, coord.down());                        
                    }
                    if ((i2 + 1) <= maxRange) {
                        addEdge(coord, coord.right());
                    }
                    if ((i2 - 1) >= -maxRange) {
                        addEdge(coord, coord.left());
                    }
                    if (i == 0 && i2 == 0) {
                        setRoot(vertex(coord));
                    }
                }
            }
            currentCoord = getRoot().data();
        }
        
        // Private Static Final Nested Classes
        private static final class CartesianCoordinate
        implements Comparable<CartesianCoordinate> {
            // Private Instance/Property Fields
            private double x;
            private double y;
            
            // Private Constructors
            private CartesianCoordinate() { }
            
            // Public Constructors
            public CartesianCoordinate(double x, double y) {
                this();
                this.x = x;
                this.y = y;
            }
            
            // Private Methods
            private CartesianCoordinate add(double x, double y) {
                return new CartesianCoordinate(this.x + x, this.y + y);
            }
            private CartesianCoordinate down() {
                return new CartesianCoordinate(x, y - 1);
            }
            private CartesianCoordinate left() {
                return new CartesianCoordinate(x - 1, y);
            }
            private CartesianCoordinate right() {
                return new CartesianCoordinate(x + 1, y);
            }
            private CartesianCoordinate up() {
                return new CartesianCoordinate(x, y + 1);
            }
            
            // Public Methods (Overrided)
            @Override
            public int compareTo(CartesianCoordinate coord) {
                final double otherX = coord.x;
                final double otherY = coord.y;
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
        
        // Public Property Accessor Methods
        public CartesianCoordinate getCurrentCoord() {
            return currentCoord;
        }

        // Public Property Mutator Methods
        public void setCurrentCoord(CartesianCoordinate coord) {
            currentCoord = coord;
        }
        
        // Private Static Methods
        private static CartesianCoordinate directionCoordinate(int rot) {
            final double rad = rot*((Math.PI)/180);
            return new CartesianCoordinate(Math.sin(rad), Math.cos(rad));
        }
        
        // Private Methods
        private CartesianCoordinate getNextCoordinateFromDirection(int rot) {
            final CartesianCoordinate otherCoord = CartesianGraph.directionCoordinate(rot);
            return currentCoord.add(otherCoord.x, otherCoord.y);
        }
        private boolean isBlocked(CartesianCoordinate coord) {
            return blockedCoords.contains(coord);
        }
        private boolean isVisited(CartesianCoordinate coord) {
            boolean returnData = false;
            final Vertex v = vertex(coord);
            if (v != null) {
                returnData = v.isVisited();
            }
            return returnData;
        }
        
        // Public Methods
        public void block(CartesianCoordinate coord) {
            if (!isBlocked(coord)) {
                blockedCoords.add(coord);
            }
        }
        public boolean isAvalible(CartesianCoordinate coord) {
            return !(isBlocked(coord) && isVisited(coord));
        }
        public void reset() {
            unvisitAll();
            blockedCoords.clear();
            currentCoord = getRoot().data();
        }
        public void visit(CartesianCoordinate coord) {
            if (!isVisited(coord)) {
                final Vertex v = vertex(coord);
                if (v != null) {
                    v.visit();
                }
            }
        }
    }
    
    // Protected Methods    
    protected CartesianGraph.CartesianCoordinate getNext() {
        CartesianGraph.CartesianCoordinate coord = cartesianGraph.getNextCoordinateFromDirection(currentRot);
        if (coord.x < -COORD_MAX_RANGE || coord.x > COORD_MAX_RANGE || coord.y < -COORD_MAX_RANGE || coord.y > COORD_MAX_RANGE) {
            cartesianGraph.reset();
            coord = cartesianGraph.currentCoord;
        }
        return coord;
    }
    protected void rotateNoNegative(int rot) {
        currentRot = currentRot + rot;
        if (currentRot == -ROT_ADD) {
            currentRot = ROT_MAX_RANGE - ROT_ADD;
        }
        else if (currentRot == ROT_MAX_RANGE) {
            currentRot = 0;
        }
    }
}