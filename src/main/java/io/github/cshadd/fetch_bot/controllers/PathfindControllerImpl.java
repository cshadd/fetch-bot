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
    
    // Private Methods
    /*private CartesianCoordinate directionCoordinate() {
        final double a = currentDirection*((Math.PI)/180);
        return getCoordinateFromDefault(Math.sin(a), Math.cos(a));
    }*/
    
    // Public Methods (Overrided)
    @Override
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