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
        cartesianGraph.blockCoord(getNext());
    }
    @Override
    public void goNext() {
        visit();
        cartesianGraph.setCurrentCoord(getNext());
    }
    @Override
    public boolean isAnyAvailable() {
        return cartesianGraph.checkForAvailable(currentRot);
    }
    @Override
    public boolean isNextBlocked() {
        return cartesianGraph.isCoordBlocked(getNext());
    }
    @Override
    public boolean isNextVisited() {
        return cartesianGraph.isCoordVisited(getNext());
    }
    @Override
    public void reset() {
        currentRot = 0;
        cartesianGraph.reset();
    }
    @Override
    public void rotateLeft() {
        rotateFix(-ROT_ADD);
    }
    @Override
    public void rotateRight() {
        rotateFix(ROT_ADD);
    }
    @Override
    public void unblockNext() {
        cartesianGraph.unblockCoord(getNext());
    }
    @Override
    public void visit() {
        cartesianGraph.visitCoord(cartesianGraph.getCurrentCoord());
    }
}