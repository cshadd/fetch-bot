/*
 * MIT License
 * 
 * Copyright (c) 2018 Christian Shadd
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * https://cshadd.github.io/fetch-bot/
 */
package io.github.cshadd.fetch_bot.controllers;

import io.github.cshadd.cshadd_java_data_structures.util.UndirectedGraph;
import io.github.cshadd.fetch_bot.util.Logger;
import java.util.ArrayList;
import java.util.List;

// Main

/**
 * The Class AbstractPathfindController. Defines what a Pathfind Controller is.
 * A Pathfind Controller is basically a manager for pathfinding tasks in Fetch
 * Bot.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
public abstract class AbstractPathfindController extends AbstractController
                implements PathfindController {
    // Private Constant Instance/Property Fields
    
    /**
     * The Constant COORD_MAX_RANGE.
     */
    private static final int COORD_MAX_RANGE = 6;
    
    /**
     * The Constant ROT_MAX_RANGE.
     */
    private static final int ROT_MAX_RANGE = 360;
    
    // Protected Constant Instance/Property Fields
    
    /**
     * The Constant ROT_ADD.
     */
    protected static final int ROT_ADD = 90;
    
    // Protected Final Instance/Property Fields
    
    /**
     * The Cartesian Graph.
     */
    protected final CartesianGraph cartesianGraph;
    
    // Protected Instance/Property Fields
    
    /**
     * The current rotation.
     */
    protected int currentRot;
    
    // Protected Constructors
    
    /**
     * Instantiates a new abstract Pathfind Controller.
     */
    protected AbstractPathfindController() {
        super();
        this.cartesianGraph = new CartesianGraph();
        this.currentRot = 0;
    }
    
    // Public Static Nested Classes
    
    /**
     * The Class CartesianGraph. An Undirected Graph that has a basic X and Y
     * coordinate system.
     * 
     * @author Christian Shadd
     * @author Maria Verna Aquino
     * @author Thanh Vu
     * @author Joseph Damian
     * @author Giovanni Orozco
     * @since 1.0.0
     */
    public static class CartesianGraph extends
                    UndirectedGraph<CartesianGraph.CartesianCoordinate> {
        // Public Constant Instance/Property Fields
        
        /**
         * The Constant RAW_GRAPH_AVALIBLE_SYMBOL.
         */
        public static final char RAW_GRAPH_AVALIBLE_SYMBOL = '.';
        
        /**
         * The Constant RAW_GRAPH_UNKNOWN_SYMBOL.
         */
        public static final char RAW_GRAPH_UNKNOWN_SYMBOL = '?';
        
        /**
         * The Constant RAW_GRAPH_BLOCKED_AND_VISITED_SYMBOL.
         */
        public static final char RAW_GRAPH_BLOCKED_AND_VISITED_SYMBOL = '#';
        
        /**
         * The Constant RAW_GRAPH_BLOCKED_SYMBOL.
         */
        public static final char RAW_GRAPH_BLOCKED_SYMBOL = 'X';
        
        /**
         * The Constant RAW_GRAPH_LOCATION_SYMBOL.
         */
        public static final char RAW_GRAPH_LOCATION_SYMBOL = '@';
        
        /**
         * The Constant RAW_GRAPH_VISITED_SYMBOL.
         */
        public static final char RAW_GRAPH_VISITED_SYMBOL = '/';
        
        // Private Instance/Property Fields
        
        /**
         * The blocked coordinates.
         */
        private List<CartesianCoordinate> blockedCoords;
        
        /**
         * The max range.
         */
        private int maxRange;
        
        /**
         * The raw graph.
         */
        private char rawGraph[][];
        
        /**
         * The current coordinate.
         */
        // Protected Instance/Property Fields
        protected CartesianCoordinate currentCoord;
        
        // Protected Constructors
        
        /**
         * Instantiates a new Cartesian Graph.
         */
        protected CartesianGraph() {
            this(COORD_MAX_RANGE);
        }
        
        /**
         * Instantiates a new Cartesian Graph with max range.
         *
         * @param newMaxRange
         *            the new max range
         */
        protected CartesianGraph(int newMaxRange) {
            super();
            this.blockedCoords = new ArrayList<>();
            this.maxRange = newMaxRange;
            this.rawGraph = new char[newMaxRange * 2][newMaxRange * 2];
            this.reset();
        }
        
        // Protected Static Nested Classes
        
        /**
         * The Class CartesianCoordinate. A Comparable that is used with a
         * {@link CartesianGraph} that has an X and Y pair.
         * 
         * @author Christian Shadd
         * @author Maria Verna Aquino
         * @author Thanh Vu
         * @author Joseph Damian
         * @author Giovanni Orozco
         * @since 1.0.0
         */
        protected static class CartesianCoordinate implements
                        Comparable<CartesianCoordinate> {
            // Protected Instance/Property Fields
            
            /**
             * The X coordinate.
             */
            protected int x;
            
            /**
             * The Y coordinate.
             */
            protected int y;
            
            // Public Constructors
            /**
             * Instantiates a new Cartesian Coordinate.
             */
            public CartesianCoordinate() {
                this(0, 0);
            }
            
            /**
             * Instantiates a new Cartesian Coordinate with a X coordinate and Y
             * coordinate.
             *
             * @param newX
             *            the new X
             * @param newY
             *            the new Y
             */
            public CartesianCoordinate(int newX, int newY) {
                this.x = newX;
                this.y = newY;
            }
            
            // Protected Methods
            
            /**
             * Adds another coordinate to the current coordinate.
             *
             * @param newX
             *            the new X
             * @param newY
             *            the new Y
             * @return the Cartesian Coordinate
             */
            protected CartesianCoordinate add(int newX, int newY) {
                return new CartesianCoordinate(this.x + newX, this.y + newY);
            }
            
            /**
             * Gets the coordinate that is below.
             *
             * @return the Cartesian Coordinate
             */
            protected CartesianCoordinate down() {
                return new CartesianCoordinate(this.x, this.y - 1);
            }
            
            /**
             * Gets the coordinate that is left.
             *
             * @return the Cartesian Coordinate
             */
            protected CartesianCoordinate left() {
                return new CartesianCoordinate(this.x - 1, this.y);
            }
            
            /**
             * Gets the coordinate that is right.
             *
             * @return the Cartesian Coordinate
             */
            protected CartesianCoordinate right() {
                return new CartesianCoordinate(this.x + 1, this.y);
            }
            
            /**
             * Gets the coordinate that is up.
             *
             * @return the Cartesian Coordinate
             */
            protected CartesianCoordinate up() {
                return new CartesianCoordinate(this.x, this.y + 1);
            }
            
            /**
             * Get X coordinate.
             *
             * @return the int
             */
            protected int x() {
                return this.x;
            }
            
            /**
             * Get Y coordinate.
             *
             * @return the int
             */
            protected int y() {
                return this.y;
            }
            
            /**
             * To array.
             *
             * @return the int[]
             */
            protected int[] toArray() {
                return new int[] { this.x, this.y };
            }
            
            // Public Methods (Overrided)
            
            /**
             * @see java.lang.Comparable#compareTo(java.lang.Object)
             */
            @Override
            public int compareTo(CartesianCoordinate coord) {
                final int otherX = coord.x;
                final int otherY = coord.y;
                final int distance = (int) Math.sqrt(((this.x - otherX)
                                * (this.x - otherX)) + ((this.y - otherY)
                                                * (this.y - otherY)));
                int returnData = 0;
                
                if (distance < 0) {
                    returnData = -1;
                } else if (distance > 0) {
                    returnData = 1;
                } else {
                    returnData = 0;
                }
                return returnData;
            }
            
            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public String toString() {
                return "(" + this.x + "," + this.y + ")";
            }
        }
        
        // Public Property Accessor Methods
        
        /**
         * Gets the current coordinate.
         *
         * @return the current coordinate
         */
        public CartesianCoordinate getCurrentCoord() {
            return this.currentCoord;
        }
        
        // Public Property Mutator Methods
        
        /**
         * Sets the current coordinate.
         *
         * @param c
         *            the new current coordinate
         */
        public void setCurrentCoord(CartesianCoordinate c) {
            this.currentCoord = fetchCoord(c);
        }
        
        // Private Static Methods
        
        /**
         * Gets the direction coordinate from a specified rotation. The
         * direction is calculated using a circle and the sine function for the
         * X coordinate, and the cosine function for the Y coordinate.
         *
         * @param rot
         *            the rotation
         * @return the Cartesian Coordinate
         */
        private static CartesianCoordinate directionCoordinate(int rot) {
            final double rad = Math.toRadians(rot);
            return new CartesianCoordinate((int) Math.sin(rad), (int) Math.cos(
                            rad));
        }
        
        // Private Methods
        
        /**
         * Fetches coordinate.
         *
         * @param c
         *            the coordinate
         * @return the Cartesian Coordinate
         */
        private CartesianCoordinate fetchCoord(CartesianCoordinate c) {
            for (int i = 0; i < this.adjacencyList.size(); i++) {
                final Vertex vertex = this.adjacencyList.get(i);
                if (vertex != null) {
                    final CartesianCoordinate vertexData = vertex.data();
                    if (vertexData != null) {
                        if (vertexData.x == c.x
                                        && vertexData.y == c.y) { return vertexData; }
                    }
                }
            }
            addVertex(c);
            return c;
        }
        
        // Protected Methods
        
        /**
         * Gets the next coordinate from direction.
         *
         * @param rot
         *            the rotation
         * @return the next coordinate from direction
         */
        protected CartesianCoordinate getNextCoordinateFromDirection(int rot) {
            final CartesianCoordinate otherCoord = CartesianGraph
                            .directionCoordinate(rot);
            final CartesianCoordinate coord = this.currentCoord.add(
                            otherCoord.x, otherCoord.y);
            return fetchCoord(coord);
        }
        
        /**
         * Gets the raw graph value.
         *
         * @param x
         *            the x
         * @param y
         *            the y
         * @return the raw graph value
         */
        protected char getRawGraphValue(int x, int y) {
            return this.rawGraph[x + this.maxRange][y + this.maxRange];
        }
        
        /**
         * String representation of the raw graph.
         *
         * @return the string
         */
        protected String rawGraphToString() {
            String returnData = "";
            for (int i = this.maxRange; i >= -this.maxRange; i--) {
                for (int i2 = -this.maxRange; i2 <= this.maxRange; i2++) {
                    returnData += this.getRawGraphValue(i2, i) + " ";
                }
                returnData += "\n";
            }
            return returnData;
        }
        
        /**
         * Sets the raw graph value.
         *
         * @param x
         *            the x
         * @param y
         *            the y
         * @param value
         *            the value
         */
        protected void setRawGraphValue(int x, int y, char value) {
            this.rawGraph[x + this.maxRange][y + this.maxRange] = value;
        }
        
        // Public Methods
        
        /**
         * Blocks a coordinate.
         *
         * @param c
         *            the coordinate
         */
        public void blockCoord(CartesianCoordinate c) {
            final CartesianCoordinate coord = fetchCoord(c);
            if (!isCoordBlocked(coord)) {
                this.blockedCoords.add(coord);
            }
        }
        
        /**
         * Checks if coordinate is blocked.
         *
         * @param c
         *            the coordinate
         * @return true, if coordinate is blocked
         */
        public boolean isCoordBlocked(CartesianCoordinate c) {
            final CartesianCoordinate coord = fetchCoord(c);
            return this.blockedCoords.contains(coord);
        }
        
        /**
         * Checks if coordinate visited.
         *
         * @param c
         *            the c
         * @return true, if coordinate is visited
         */
        public boolean isCoordVisited(CartesianCoordinate c) {
            final CartesianCoordinate coord = fetchCoord(c);
            boolean returnData = false;
            final Vertex v = vertex(coord);
            if (v != null) {
                returnData = v.isVisited();
            }
            return returnData;
        }
        
        /**
         * Resets the graph.
         */
        public void reset() {
            for (int i = 0; i <= this.maxRange * 2; i++) {
                for (int i2 = 0; i2 <= this.maxRange * 2; i2++) {
                    this.rawGraph[i][i2] = CartesianGraph.RAW_GRAPH_UNKNOWN_SYMBOL;
                }
            }
            unvisitAll();
            this.blockedCoords.clear();
            for (int i = -this.maxRange; i <= this.maxRange; i++) {
                for (int i2 = -this.maxRange; i2 <= this.maxRange; i2++) {
                    final CartesianCoordinate coord = new CartesianCoordinate(i,
                                    i2);
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
        
        /**
         * Unblocks coordinate.
         *
         * @param c
         *            the coordinate
         */
        public void unblockCoord(CartesianCoordinate c) {
            final CartesianCoordinate coord = fetchCoord(c);
            if (isCoordBlocked(coord)) {
                this.blockedCoords.remove(coord);
            }
        }
        
        /**
         * Unvisits coordinate.
         *
         * @param c
         *            the coordinate
         */
        public void unvisitCoord(CartesianCoordinate c) {
            final CartesianCoordinate coord = fetchCoord(c);
            if (isCoordVisited(coord)) {
                final Vertex v = vertex(coord);
                if (v != null) {
                    v.unvisit();
                }
            }
        }
        
        /**
         * Visit coordinate.
         *
         * @param c
         *            the coordinate
         */
        public void visitCoord(CartesianCoordinate c) {
            final CartesianCoordinate coord = fetchCoord(c);
            if (!isCoordVisited(coord)) {
                final Vertex v = vertex(coord);
                if (v != null) {
                    v.visit();
                }
            }
        }
    }
    
    // Protected Methods
    
    /**
     * Gets the next coordinate. If the coordinate exceeds the max range, the
     * graph resets.
     *
     * @return the next coordinate
     */
    protected CartesianGraph.CartesianCoordinate getNext() {
        CartesianGraph.CartesianCoordinate coord = this.cartesianGraph
                        .getNextCoordinateFromDirection(this.currentRot);
        if (coord.x < -COORD_MAX_RANGE || coord.x > COORD_MAX_RANGE
                        || coord.y < -COORD_MAX_RANGE
                        || coord.y > COORD_MAX_RANGE) {
            Logger.debug("PathfindController - Out of bounds, resetting.");
            this.cartesianGraph.reset();
            coord = this.cartesianGraph.currentCoord;
        }
        return coord;
    }
    
    /**
     * Rotation fix to keep it between the ranges.
     *
     * @param rot
     *            the rotation
     */
    protected void rotateFix(int rot) {
        this.currentRot = this.currentRot + rot;
        if (this.currentRot <= -ROT_ADD) {
            this.currentRot = ROT_MAX_RANGE - ROT_ADD;
        } else if (this.currentRot >= ROT_MAX_RANGE) {
            this.currentRot = 0;
        }
        Logger.debug("PathfindController - New rot: " + this.currentRot + ".");
    }
    
    // Public Methods (Overrided)
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Current coord: " + this.cartesianGraph.getCurrentCoord()
                        + "; Current rot: " + this.currentRot;
    }
}