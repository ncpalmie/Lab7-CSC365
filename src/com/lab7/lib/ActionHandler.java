package com.lab7.lib;

/**
 * Action handler for actions created by console/gui user inputs
 */
public class ActionHandler {
    Action currentAction;
    enum Action {
        VIEW_ROOMS,
        CREATE_RES,
        ALTER_RES,
        CANCEL_RES,
        VIEW_ALL_RES,
        VIEW_REVENUE
    }

    public ActionHandler() {
    }

    /**
     * Takes user input and handles desired action
     *
     * @param actionNumber The desired action number entered by the user in the
     *                     console/associated number from button press in GUI
     *
     * @return Formatted string containing desired information from database
     */
    public String handleAction(int actionNumber) {
        String retString = "";

        this.currentAction = Action.values()[actionNumber];

        switch (this.currentAction) {
            case VIEW_ROOMS:
                //View room code/functions
                break;
            case CREATE_RES:
                //Reserve room code/functions
                break;
            case ALTER_RES:
                //Alter reservation code/functions
                break;
            case CANCEL_RES:
                //Cancel reservation code/functions
                break;
            case VIEW_ALL_RES:
                //View all reservations code/functions
                break;
            case VIEW_REVENUE:
                //View revenue code/functions
                break;
        }

        return retString;
    }
}
