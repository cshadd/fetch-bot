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

import io.github.cshadd.fetch_bot.Component;

// Main

/**
 * The Class PathfindControllerImpl. An Pathfind Controller with basic
 * implementation.
 * 
 * @author Christian Shadd
 * @author Maria Verna Aquino
 * @author Thanh Vu
 * @author Joseph Damian
 * @author Giovanni Orozco
 * @since 1.0.0
 */
@Component("AI")
public class PathfindControllerImpl extends AbstractPathfindController {
    // Public Constructors
    
    /**
     * Instantiates a new Pathfind Controller Impl.
     */
    public PathfindControllerImpl() {
        super();
    }
    
    // Public Methods (Overrided)
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#blockNext()
     */
    @Override
    public void blockNext() {
        this.cartesianGraph.blockCoord(getNext());
    }
    
    // Public Methods
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#calculate()
     */
    @Override
    public boolean[] calculate() {
        final boolean backBlocked;
        final boolean backVisited;
        final boolean frontBlocked;
        final boolean frontVisited;
        final boolean leftBlocked;
        final boolean leftVisited;
        final boolean rightBlocked;
        final boolean rightVisited;
        frontBlocked = this.isNextBlocked();
        frontVisited = this.isNextVisited();
        int[] c = this.getNext().toArray();
        if (frontBlocked && frontVisited) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_BLOCKED_AND_VISITED_SYMBOL);
        } else if (frontBlocked) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_BLOCKED_SYMBOL);
        } else if (frontVisited) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_VISITED_SYMBOL);
        } else {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_AVALIBLE_SYMBOL);
        }
        this.rotateRight();
        rightBlocked = this.isNextBlocked();
        rightVisited = this.isNextVisited();
        c = this.getNext().toArray();
        if (rightBlocked && rightVisited) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_BLOCKED_AND_VISITED_SYMBOL);
        } else if (rightBlocked) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_BLOCKED_SYMBOL);
        } else if (rightVisited) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_VISITED_SYMBOL);
        } else {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_AVALIBLE_SYMBOL);
        }
        this.rotateRight();
        backBlocked = this.isNextBlocked();
        backVisited = this.isNextVisited();
        c = this.getNext().toArray();
        if (backBlocked && backVisited) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_BLOCKED_AND_VISITED_SYMBOL);
        } else if (backBlocked) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_BLOCKED_SYMBOL);
        } else if (backVisited) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_VISITED_SYMBOL);
        } else {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_AVALIBLE_SYMBOL);
        }
        this.rotateRight();
        leftBlocked = this.isNextBlocked();
        leftVisited = this.isNextVisited();
        c = this.getNext().toArray();
        if (leftBlocked && leftVisited) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_BLOCKED_AND_VISITED_SYMBOL);
        } else if (leftBlocked) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_BLOCKED_SYMBOL);
        } else if (leftVisited) {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_VISITED_SYMBOL);
        } else {
            this.cartesianGraph.setRawGraphValue(c[0], c[1],
                            CartesianGraph.RAW_GRAPH_AVALIBLE_SYMBOL);
        }
        this.rotateRight();
        c = this.cartesianGraph.getCurrentCoord().toArray();
        this.cartesianGraph.setRawGraphValue(c[0], c[1],
                        CartesianGraph.RAW_GRAPH_LOCATION_SYMBOL);
        return new boolean[] { backBlocked, backVisited, frontBlocked,
                        frontVisited, leftBlocked, leftVisited, rightBlocked,
                        rightVisited };
    }
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#goNext()
     */
    @Override
    public void goNext() {
        visit();
        this.cartesianGraph.setCurrentCoord(getNext());
    }
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#isNextBlocked()
     */
    @Override
    public boolean isNextBlocked() {
        return this.cartesianGraph.isCoordBlocked(getNext());
    }
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#isNextVisited()
     */
    @Override
    public boolean isNextVisited() {
        return this.cartesianGraph.isCoordVisited(getNext());
    }
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#rawGraphToString()
     */
    @Override
    public String rawGraphToString() {
        return this.cartesianGraph.rawGraphToString();
    }
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#reset()
     */
    @Override
    public void reset() {
        this.currentRot = 0;
        this.cartesianGraph.reset();
    }
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#unblockNext()
     */
    @Override
    public void unblockNext() {
        this.cartesianGraph.unblockCoord(getNext());
    }
    
    /**
     * @see io.github.cshadd.fetch_bot.controllers.PathfindController#visit()
     */
    @Override
    public void visit() {
        this.cartesianGraph.visitCoord(this.cartesianGraph.getCurrentCoord());
    }
}