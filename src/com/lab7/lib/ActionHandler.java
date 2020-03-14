package com.lab7.lib;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Action handler for actions created by console/gui user inputs
 */
public class ActionHandler {
    Action currentAction;
    Results actionResult;
    List<String> prevArgs;
    boolean continueAction = false;


    enum Action {
        VIEW_ROOMS,
        CREATE_RES,
        ALTER_RES,
        CANCEL_RES,
        VIEW_RES,
        VIEW_REVENUE
    }

    /**
     * SUCCESS when action is done and return string contains final output
     *     information for action
     * PROMPT_AGAIN when action requires more user input to complete and
     *     return string is a new prompt to return to console/GUI
     * FAIL when action cannot complete properly and return string contains
     *     error information for user to correct
     */
    public enum Results {
        SUCCESS,
        PROMPT_AGAIN,
        FAIL
    }

    public ActionHandler() {
    }

    /**
     * Takes user input and handles desired action
     *
     * @param actionNumber The desired action number entered by the user in the
     *                     console/associated number from button press in GUI
     *
     * @return Formatted string containing desired information from database or
     * action status for user
     */
    public String handleAction(int actionNumber, List<String> argsList) {
        if (actionNumber != -1) {
            this.currentAction = Action.values()[actionNumber];
            this.continueAction = false;
        }
        else {
            this.continueAction = true;
        }

        //In case action with multiple prompts needs previous information
        this.prevArgs = argsList;

        switch (this.currentAction) {
            case VIEW_ROOMS:
                return this.getRooms();
            case CREATE_RES:
                return this.createReservation(argsList);
            case ALTER_RES:
                return this.alterReservation(argsList);
            case CANCEL_RES:
                return this.cancelReservation(argsList);
            case VIEW_RES:
                return this.viewReservation(argsList);
            case VIEW_REVENUE:
                return this.viewRevenue();
        }
        return "No matching action was found.";
    }

    private String getRooms() {
        String retString = "";

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String createReservation(List<String> argsList) {
        String retString = "";

        if (this.continueAction) {
            //For confirmation/other reservation selection
        }
        else {
            //For initial reservation request
        }

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String alterReservation(List<String> argsList) {
        String retString = "";

        if (this.continueAction) {
            //For confirmation of alteration
        }
        else {
            //For initial alteration request
        }

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String cancelReservation(List<String> argsList) {
        String retString = "temp";

        if (this.continueAction) {
            //For cancellation confirmation
        }
        else {
            //For initial cancellation request
        }

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String viewReservation(List<String> argsList) {
        String retString = "";

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    private String viewRevenue() {
        String retString = "";

        //Default successful return
        this.actionResult = Results.SUCCESS;
        return retString;
    }

    public Action getCurrentAction()         { return currentAction; }
    public Results getActionResult()         { return actionResult; }
    public List<String> getPrevArgs()        { return prevArgs;}
}
