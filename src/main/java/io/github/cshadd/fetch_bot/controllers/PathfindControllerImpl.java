package io.github.cshadd.fetch_bot.controllers;
import io.github.cshadd.fetch_bot.Component;

// Main
@Component("AI")
public class PathfindControllerImpl
extends AbstractPathfindController {    
    // Public Constructors
    public PathfindControllerImpl() {
        super();
    }

    // Public Methods (Overrided)
    @Override
    public void blockNext() {
        cartesianGraph.block(getNext());
    }
    @Override
    public void goNext() {
        cartesianGraph.setCurrentCoord(getNext());
    }
    @Override
    public boolean isNextAvalible() {
        return cartesianGraph.isAvalible(getNext());
    }
    @Override
    public void reset() {
        cartesianGraph.reset();
    }
    @Override
    public void rotateLeft() {
        rotateNoNegative(-ROT_ADD);
    }
    @Override
    public void rotateRight() {
        rotateNoNegative(ROT_ADD);
    }
    @Override
    public void visitNext() {
        cartesianGraph.visit(getNext());
    }

    /* ALG 1:
     * If track-class
     *      If next-found
     *          Happy
     *          Stop
     *          Reset
     *          End
     *      Else If all-visited or all-blocked
     *          Sad
     *          Stop
     *          End
     *      Else If next-out-of-bounds
     *          Neutral
     *          Stop
     *          Reset
     *      Else
     *          Else If next-visited
     *              Sad
     *              rot-+90
     *              Stop
     *          Else If next-blocked
     *              Angry
     *              rot-+90
     *              Stop
     *          Else
     *              Neutral
     *              Forward
     *              Stop
     *              Visit
     * Else
     *      Idle
     *      Stop
     *      Reset
     *      End
     *          
     * 
     */
    
    
    
    /* ALG 2:
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