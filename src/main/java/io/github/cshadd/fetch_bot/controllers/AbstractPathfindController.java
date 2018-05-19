package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.cshadd_java_data_structures.util.UndirectedGraph;
import io.github.cshadd.fetch_bot.util.Logger;
import java.util.ArrayList;
import java.util.List;

// Main
public abstract class AbstractPathfindController
extends AbstractController
implements PathfindController {
    // Private Constant Instance/Property Fields
    private static final int COORD_MAX_RANGE = 6;
    private static final int ROT_MAX_RANGE = 360;

    // Protected Constant Instance/Property Fields
    protected static final int ROT_ADD = 90;
    
    // Protected Final Instance/Property Fields
    protected final CartesianGraph cartesianGraph;
    
    // Protected Instance/Property Fields
    protected int currentRot;

    // Protected Constructors
    protected AbstractPathfindController() {
        super();
        this.cartesianGraph = new CartesianGraph();
        this.currentRot = 0;
    }
    
    // Public Static Final Nested Classes
    public static final class CartesianGraph
    extends UndirectedGraph<CartesianGraph.CartesianCoordinate> {
        // Private Instance/Property Fields
        private List<CartesianCoordinate> blockedCoords;
        private CartesianCoordinate currentCoord;
        private int maxRange;
        
        // Protected Constructors
        protected CartesianGraph() {
            this(COORD_MAX_RANGE);
        }
        protected CartesianGraph(int maxRange) {
            super();
            this.blockedCoords = new ArrayList<CartesianCoordinate>();
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
            
            // Protected Methods
            protected CartesianCoordinate add(int x, int y) {
                return new CartesianCoordinate(this.x + x, this.y + y);
            }
            protected CartesianCoordinate down() {
                return new CartesianCoordinate(this.x, this.y - 1);
            }
            protected CartesianCoordinate left() {
                return new CartesianCoordinate(this.x - 1, this.y);
            }
            protected CartesianCoordinate right() {
                return new CartesianCoordinate(this.x + 1, this.y);
            }
            protected CartesianCoordinate up() {
                return new CartesianCoordinate(this.x, this.y + 1);
            }
            
            // Public Methods (Overrided)
            @Override
            public int compareTo(CartesianCoordinate coord) {
                final int otherX = coord.x;
                final int otherY = coord.y;
                final int distance = (int)Math.sqrt(((this.x - otherX)*(this.x - otherX)) + ((this.y - otherY)*(this.y - otherY)));
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
                return "(" + this.x + "," + this.y + ")";
            }
        }
        
        // Public Property Accessor Methods
        public CartesianCoordinate getCurrentCoord() {
            return this.currentCoord;
        }

        // Public Property Mutator Methods
        public void setCurrentCoord(CartesianCoordinate coord) {
            this.currentCoord = fetchCoord(coord);
        }

        // Private Static Methods
        private static CartesianCoordinate directionCoordinate(int rot) {
            final double rad = Math.toRadians(rot);
            return new CartesianCoordinate((int)Math.sin(rad), (int)Math.cos(rad));
        }
        
        // Private Methods
        private CartesianCoordinate fetchCoord(CartesianCoordinate coord) {
            for (int i = 0; i < this.adjacencyList.size(); i++) {
                final Vertex vertex = this.adjacencyList.get(i);
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
            return coord;
        }
        
        // Protected Methods
        protected CartesianCoordinate getNextCoordinateFromDirection(int rot) {
            final CartesianCoordinate otherCoord = CartesianGraph.directionCoordinate(rot);
            final CartesianCoordinate coord = this.currentCoord.add(otherCoord.x, otherCoord.y);
            return fetchCoord(coord);
        }

        // Public Methods
        public void blockCoord(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            if (!isCoordBlocked(coord)) {
                this.blockedCoords.add(coord);
            }
        }
        public boolean isCoordBlocked(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            return this.blockedCoords.contains(coord);
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
            this.blockedCoords.clear();
            for (int i = -this.maxRange; i <= this.maxRange; i++) {
                for (int i2 = -this.maxRange; i2 <= this.maxRange; i2++) {
                    final CartesianCoordinate coord = new CartesianCoordinate(i, i2);
                    fetchCoord(coord);
                    if ((i + 1) <= this.maxRange) {
                        addEdge(coord, fetchCoord(coord.right()));
                    }
                    if ((i - 1) >= -this.maxRange) {
                        addEdge(coord, fetchCoord(coord.left()));
                    }
                    if ((i2 + 1) <= this.maxRange) {
                        addEdge(coord, fetchCoord(coord.up()));
                    }
                    if ((i2 - 1) >= -this.maxRange) {
                        addEdge(coord, fetchCoord(coord.down()));
                    }
                    if (i == 0 && i2 == 0) {
                        setRoot(vertex(coord));
                    }
                }
            }
            this.currentCoord = getRoot().data();
            getRoot().visit();
        }
        public void unblockCoord(CartesianCoordinate coord) {
            coord = fetchCoord(coord);
            if (isCoordBlocked(coord)) {
                this.blockedCoords.remove(coord);
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
        CartesianGraph.CartesianCoordinate coord = this.cartesianGraph.getNextCoordinateFromDirection(this.currentRot);
        if (coord.x < -COORD_MAX_RANGE || coord.x > COORD_MAX_RANGE || coord.y < -COORD_MAX_RANGE || coord.y > COORD_MAX_RANGE) {
            Logger.debug("PathfindController - Out of bounds, resetting.");
            this.cartesianGraph.reset();
            coord = this.cartesianGraph.currentCoord;
        }
        return coord;
    }
    protected void rotateFix(int rot) {
        this.currentRot = this.currentRot + rot;
        if (this.currentRot <= -ROT_ADD) {
            this.currentRot = ROT_MAX_RANGE - ROT_ADD;
        }
        else if (this.currentRot >= ROT_MAX_RANGE) {
            this.currentRot = 0;
        }
        Logger.debug("PathfindController - New rot: " + this.currentRot + ".");
    }
    
    // Public Methods (Overrided)
    @Override
    public String toString() {
        return "Current coord: " + this.cartesianGraph.getCurrentCoord() + "; Current rot: " + this.currentRot;
    }
}