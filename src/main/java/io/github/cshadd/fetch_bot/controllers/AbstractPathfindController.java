package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.cshadd_java_data_structures.util.UndirectedGraph;

// Main
public abstract class AbstractPathfindController
extends AbstractController
implements PathfindController {
    // Private Constant Instance/Property Fields
    private static final int MAX_RANGE = 10;
    
    // Private Final Instance/Property Fields
    private final CartesianGraph cartesianGraph;
    
    // Private Instance/Property Fields
    private int rot;

    // Protected Constructors
    protected AbstractPathfindController() {
        super();
        cartesianGraph = new CartesianGraph();
        rot = 0;
    }
    
    // Private Static Final Nested Classes
    private static final class CartesianGraph
    extends UndirectedGraph<CartesianGraph.CartesianCoordinate> {        
        // Private Instance/Property Fields
        private CartesianCoordinate currentCoord;
        
        // Private Constructors
        private CartesianGraph() {
            this(MAX_RANGE);
        }
        private CartesianGraph(int maxRange) {
            super();
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
            private CartesianCoordinate invertedCoordinate() {
                return new CartesianCoordinate(x*-1, y*-1);
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
        
        // Private Static Methods
        private static CartesianCoordinate directionCoordinate(int rot) {
            final double a = rot*((Math.PI)/180);
            return new CartesianCoordinate(Math.sin(a), Math.cos(a));
        }
        private CartesianCoordinate nextCoordinateFromDirection(CartesianCoordinate coord, int rot) {
            final CartesianCoordinate otherCoord = CartesianGraph.directionCoordinate(rot);
            return coord.add(otherCoord.x, otherCoord.y);
        }
    }
}