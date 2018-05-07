package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.cshadd_java_data_structures.util.UndirectedGraph;
import io.github.cshadd.fetch_bot.util.Logger;

import java.util.ArrayList;
import java.util.List;

// Main
public abstract class AbstractPathfindController
extends AbstractController
implements PathfindController {
    // protected Constant Instance/Property Fields
    protected static final int COORD_MAX_RANGE = 5;
    protected static final int ROT_ADD = 90;
    protected static final int ROT_MAX_RANGE = 360;
    
    // Protected Final Instance/Property Fields
    protected final CartesianGraph cartesianGraph;
    
    // Protected Instance/Property Fields
    protected int currentRot;

    // Protected Constructors
    protected AbstractPathfindController() {
        super();
        cartesianGraph = new CartesianGraph();
        currentRot = 0;
    }
    
    // Public Static Final Nested Classes
    public static final class CartesianGraph
    extends UndirectedGraph<CartesianGraph.CartesianCoordinate> {
        // Private Instance/Property Fields
        private List<CartesianCoordinate> blockedCoords;
        private CartesianCoordinate currentCoord;
        private int maxRange;
        
        // Private Constructors
        private CartesianGraph() {
            this(COORD_MAX_RANGE);
        }
        private CartesianGraph(int maxRange) {
            super();
            blockedCoords = new ArrayList<CartesianCoordinate>();
            this.maxRange = maxRange;
        }
        
        // Private Static Final Nested Classes
        private static final class CartesianCoordinate
        implements Comparable<CartesianCoordinate> {
            // Private Instance/Property Fields
            private int x;
            private int y;
            
            // Private Constructors
            private CartesianCoordinate() { }
            
            // Public Constructors
            public CartesianCoordinate(int x, int y) {
                this();
                this.x = x;
                this.y = y;
            }
            
            // Private Methods
            private CartesianCoordinate add(int x, int y) {
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
                final int otherX = coord.x;
                final int otherY = coord.y;
                final int distance = (int)Math.sqrt(((x - otherX)*(x - otherX)) + ((y - otherY)*(y - otherY)));
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
            @Override
            public String toString() {
                return "(" + x + "," + y + ")";
            }
        }
        
        // Public Property Accessor Methods
        public CartesianCoordinate getCurrentCoord() {
            return currentCoord;
        }

        // Public Property Mutator Methods
        public void setCurrentCoord(CartesianCoordinate coord) {
            currentCoord = fetchCoord(coord);
        }

        // Private Static Methods
        private static CartesianCoordinate directionCoordinate(int rot) {
            final double rad = Math.toRadians(rot);
            return new CartesianCoordinate((int)Math.sin(rad), (int)Math.cos(rad));
        }
        
        // Private Methods
        private CartesianCoordinate fetchCoord(CartesianCoordinate coord) {
            for (int i = 0; i < adjacencyList.size(); i++) {
                final Vertex vertex = adjacencyList.get(i);
                if (vertex != null) {
                    final CartesianCoordinate vertexData = vertex.data();
                    if (vertexData != null) {
                        if (vertexData.x == coord.x && vertexData.y == coord.y) {
                            return vertexData;
                        }
                    }
                }
            }
            addVertex(coord);
            Logger.debug("PathfindController - Fetched coord: " + coord);
            return coord;
        }
        private CartesianCoordinate getNextCoordinateFromDirection(int rot) {
            final CartesianCoordinate otherCoord = CartesianGraph.directionCoordinate(rot);
            final CartesianCoordinate coord = currentCoord.add(otherCoord.x, otherCoord.y);
            return fetchCoord(coord);
        }
        
        // Protected Methods
        protected boolean checkForAvailable(int rot) {
            final CartesianGraph.CartesianCoordinate forward = getNextCoordinateFromDirection(rot);
            final CartesianGraph.CartesianCoordinate left = getNextCoordinateFromDirection(rot - ROT_ADD);
            final CartesianGraph.CartesianCoordinate right = getNextCoordinateFromDirection(rot + ROT_ADD);
            Logger.debug("PathfindController - Forward coord: " + forward);
            Logger.debug("PathfindController - Left coord:" + left);
            Logger.debug("PathfindController - Right coord:" + right);
            return (isCoordAvailable(forward) || isCoordAvailable(left) || isCoordAvailable(right));
        }
        
        // Public Methods
        public void blockCoord(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            if (!isCoordBlocked(coord)) {
                blockedCoords.add(coord);
            }
        }
        public boolean isCoordAvailable(CartesianCoordinate coord) {
            final boolean returnData = !(isCoordBlocked(coord) && isCoordVisited(coord));
            Logger.debug("PathfindController - Avalible: " + returnData);
            return returnData;
        }
        public boolean isCoordBlocked(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            return blockedCoords.contains(coord);
        }
        public boolean isCoordVisited(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            boolean returnData = false;
            final Vertex v = vertex(coord);
            if (v != null) {
                returnData = v.isVisited();
            }
            return returnData;
        }
        public void reset() {
            unvisitAll();
            blockedCoords.clear();
            for (int i = -maxRange; i <= maxRange; i++) {
                for (int i2 = -maxRange; i2 <= maxRange; i2++) {
                    final CartesianCoordinate coord = new CartesianCoordinate(i, i2);
                    fetchCoord(coord);
                    if ((i + 1) <= maxRange) {
                        addEdge(coord, fetchCoord(coord.right()));
                    }
                    if ((i - 1) >= -maxRange) {
                        addEdge(coord, fetchCoord(coord.left()));
                    }
                    if ((i2 + 1) <= maxRange) {
                        addEdge(coord, fetchCoord(coord.up()));
                    }
                    if ((i2 - 1) >= -maxRange) {
                        addEdge(coord, fetchCoord(coord.down()));
                    }
                    if (i == 0 && i2 == 0) {
                        setRoot(vertex(coord));
                    }
                }
            }
            currentCoord = getRoot().data();
            getRoot().visit();
        }
        public void unblockCoord(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            if (isCoordBlocked(coord)) {
                blockedCoords.remove(coord);
            }
        }
        public void unvisitCoord(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            if (isCoordVisited(coord)) {
                final Vertex v = vertex(coord);
                if (v != null) {
                    v.unvisit();
                }
            }
        }
        public void visitCoord(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            if (!isCoordVisited(coord)) {
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
    protected void rotateFix(int rot) {
        rot = currentRot + rot;
        if (rot == -ROT_ADD) {
            rot = ROT_MAX_RANGE - ROT_ADD;
        }
        else if (currentRot == ROT_MAX_RANGE) {
            rot = 0;
        }
        currentRot = rot;
        Logger.debug("PathfindController - New rot: " + currentRot);
    }
    
    // Public Methods (Overrided)
    @Override
    public String toString() {
        return "Current coord: " + cartesianGraph.getCurrentCoord() + "; Current rot: " + currentRot;
    }
}