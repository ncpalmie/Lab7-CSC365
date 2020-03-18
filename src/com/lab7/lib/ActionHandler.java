package com.lab7.lib;

import java.sql.*;
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
        String availableRoomQuery = "with TakenRooms as (SELECT DISTINCT Room FROM lab7_reservations WHERE " +
                "CheckIn >= ? AND CheckIn < ? OR " +
                "CheckOut > ? AND CheckOut <= ?) " +
                "SELECT RoomCode FROM lab7_rooms as r WHERE r.roomCode NOT IN (SELECT Room FROM TakenRooms)";

        if (this.continueAction) {
            //For confirmation/other reservation selection
        }
        else {
            //For initial reservation request
            try (Connection conn = DriverManager.getConnection(System.getenv("APP_JDBC_URL"),
                    System.getenv("APP_JDBC_USER"),
                    System.getenv("APP_JDBC_PW")))
            {
                //Need to get open rooms to check if requested room is available
                PreparedStatement availRooms = conn.prepareStatement(availableRoomQuery);

                availRooms.setDate(1, java.sql.Date.valueOf(argsList.get(4))); //argsList 4 is checkIn date
                availRooms.setDate(2, java.sql.Date.valueOf(argsList.get(5))); //argsList 5 is checkOut date
                availRooms.setDate(3, java.sql.Date.valueOf(argsList.get(4)));
                availRooms.setDate(4, java.sql.Date.valueOf(argsList.get(5)));

                ResultSet rooms = availRooms.executeQuery();

                while(rooms.next()) {
                    retString += rooms.getString("roomcode") + "\n";
                }
            }
            catch (SQLException e)
            {
                ExceptionReporter rp = new ExceptionReporter(e);

                rp.report();
                System.exit(-1);
            }
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
        String retString = "";
        String deleteReservation = "DELETE FROM lab7_reservations WHERE CODE = ?";

        if (this.continueAction) {
            //For cancellation confirmation
            try (Connection conn = DriverManager.getConnection(System.getenv("APP_JDBC_URL"),
                System.getenv("APP_JDBC_USER"),
                System.getenv("APP_JDBC_PW")))
            {
                PreparedStatement delRes = conn.prepareStatement(deleteReservation);

                delRes.setInt(1, java.lang.Integer.valueOf(argsList.get(0)));

                delRes.executeUpdate();
            }
            catch (SQLException e)
            {
                ExceptionReporter rp = new ExceptionReporter(e);

                rp.report();
                System.exit(-1);
            }
            this.actionResult = Results.SUCCESS;
        }
        else {
            //For initial cancellation request
            try (Connection conn = DriverManager.getConnection(System.getenv("APP_JDBC_URL"),
                    System.getenv("APP_JDBC_USER"),
                    System.getenv("APP_JDBC_PW")))
            {
                conn.setAutoCommit(false);
                PreparedStatement delRes = conn.prepareStatement(deleteReservation);

                delRes.setInt(1, java.lang.Integer.valueOf(argsList.get(0)));

                if (delRes.executeUpdate() > 0)
                {
                    this.actionResult = Results.PROMPT_AGAIN;
    
                    retString = "This will permanently delete your reservation. Are you sure?";
                }
                else
                {
                    this.actionResult = Results.FAIL;

                    retString = "No reservation was found with that code";
                }
                conn.rollback();
            }
            catch (SQLException e)
            {
                ExceptionReporter rp = new ExceptionReporter(e);

                rp.report();
                System.exit(-1);
            }
        }

        //Default successful return
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
