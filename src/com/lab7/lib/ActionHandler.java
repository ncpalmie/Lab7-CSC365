package com.lab7.lib;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import com.lab7.console.ConsoleUtils;

/**
 * Action handler for actions created by console/gui user inputs
 */
public class ActionHandler {
    Action currentAction;
    Results actionResult;
    List<String> lastArgs = new ArrayList<String>();
    List<String> prevVals = new ArrayList<String>();
    List<String> newVals = new ArrayList<String>();
    boolean continueAction = false;
    int lastChoice = -1;


    public enum Action {
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
            this.prevVals.clear();
            this.lastArgs.clear();
            this.newVals.clear();
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

    // TODO: Refactor into more digestible functions
    private String createReservation(List<String> argsList) {
        int totalOcc;
        String retString = "";
        String availableRoomQuery;
        String makeResStmt;
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Room> validRooms = new ArrayList<Room>();
        Room desiredRoom = null;

        availableRoomQuery = "with TakenRooms as (SELECT DISTINCT Room FROM lab7_reservations WHERE " +
                "CheckIn >= ? AND CheckIn < ? OR " +
                "CheckOut > ? AND CheckOut <= ?) " +
                "SELECT RoomCode FROM lab7_rooms as r WHERE r.roomCode NOT IN (SELECT Room FROM TakenRooms) AND " +
                "r.maxOcc >= ?";
        makeResStmt = "INSERT INTO lab7_reservations (code, room, checkin, checkout, rate, lastname, firstname," +
                " adults, kids) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (this.continueAction) {
            //For confirmation/other reservation selection
            if (Character.isDigit(argsList.get(0).charAt(0))) {
                try (Connection conn = DriverManager.getConnection(System.getenv("APP_JDBC_URL"),
                        System.getenv("APP_JDBC_USER"),
                        System.getenv("APP_JDBC_PW")))
                {
                    desiredRoom = Room.fromDatabase(prevVals.get(
                            Integer.valueOf(argsList.get(0)) - 1), conn);
                    this.lastChoice = Integer.valueOf(argsList.get(0));
                    return this.prepRoomConfirmation(desiredRoom, lastArgs);
                }
                catch (SQLException e)
                {
                    ExceptionReporter rp = new ExceptionReporter(e);

                    rp.report();
                    System.exit(-1);
                }
            }
            else if (argsList.get(0).toLowerCase().equals("y")) {
                try (Connection conn = DriverManager.getConnection(System.getenv("APP_JDBC_URL"),
                        System.getenv("APP_JDBC_USER"),
                        System.getenv("APP_JDBC_PW")))
                {
                    float dailyRate = Float.valueOf(lastArgs.get(8)) / ConsoleUtils.getNumDays(
                            lastArgs.get(4), lastArgs.get(5));
                    PreparedStatement newRes = conn.prepareStatement(makeResStmt);

                    if (newVals.size() > 0) {
                        lastArgs.set(2, newVals.get((this.lastChoice - 1) * 3));
                        lastArgs.set(4, newVals.get((this.lastChoice - 1) * 3 + 1));
                        lastArgs.set(5, newVals.get((this.lastChoice - 1) * 3 + 2));
                    }

                    newRes.setInt(1, Reservation.getUniqueCode());
                    newRes.setString(2, lastArgs.get(2)); //RoomCode
                    newRes.setDate(3, java.sql.Date.valueOf(lastArgs.get(4))); //CheckIn
                    newRes.setDate(4, java.sql.Date.valueOf(lastArgs.get(5))); //CheckOut
                    newRes.setBigDecimal(5, new BigDecimal(dailyRate));
                    newRes.setString(6, lastArgs.get(1)); // Last Name
                    newRes.setString(7, lastArgs.get(0)); // First Name
                    newRes.setInt(8, Integer.valueOf(lastArgs.get(7))); //Num adults
                    newRes.setInt(9, Integer.valueOf(lastArgs.get(6))); //Num Kids

                    newRes.executeUpdate();
                }
                catch (SQLException e)
                {
                    ExceptionReporter rp = new ExceptionReporter(e);

                    rp.report();
                    System.exit(-1);
                }
                retString += "Your reservation has been confirmed.\n";
            }
            else {
                retString += "You have cancelled your reservation request.\n";
            }
        }
        else {
            //For initial reservation request
            try (Connection conn = DriverManager.getConnection(System.getenv("APP_JDBC_URL"),
                    System.getenv("APP_JDBC_USER"),
                    System.getenv("APP_JDBC_PW")))
            {
                totalOcc = Integer.parseInt(argsList.get(6)) +                    //argsList 6 and 7 are occupants
                    Integer.parseInt(argsList.get(7));

                //Check if total occupancy exceeds max room size
                if (totalOcc >= 5) {
                    retString += "We're sorry, but there are no rooms currently available that can fit\n" +
                            "your entire party. You'll need to make multiple reservations and split\n" +
                            "your party among multiple rooms.\n";
                    this.actionResult = Results.FAIL;
                    return retString;
                }

                //Check if specific room was requested
                if (roomCodeExists(argsList.get(2), conn) && argsList.get(2).length() > 0) {
                    desiredRoom = Room.fromDatabase(argsList.get(2), conn);
                }

                //Need to get valid rooms to check if requested room is available
                PreparedStatement availRooms = conn.prepareStatement(availableRoomQuery);


                availRooms.setDate(1, java.sql.Date.valueOf(argsList.get(4))); //argsList 4 is checkIn date
                availRooms.setDate(2, java.sql.Date.valueOf(argsList.get(5))); //argsList 5 is checkOut date
                availRooms.setDate(3, java.sql.Date.valueOf(argsList.get(4)));
                availRooms.setDate(4, java.sql.Date.valueOf(argsList.get(5)));
                availRooms.setInt(5, totalOcc);

                ResultSet rooms = availRooms.executeQuery();

                while(rooms.next()) {
                    validRooms.add(Room.fromDatabase(rooms.getString("RoomCode"), conn));
                }

                if (validRooms.size() == 0) {
                    int roomNdx = 1;
                    retString += "Unfortunately no available rooms match your search description. \n";
                    retString += "Here is a list of rooms available for the length of time you'd like \n";
                    retString += "to book, but on future dates when they are available.\n";
                    List<Reservation> suggestedRooms = getFiveSuggestions(argsList.get(4),
                            argsList.get(5), totalOcc, conn);
                    for (Reservation res : suggestedRooms) {
                        retString += roomNdx + ": " + res.getRoomCode() + " from " +
                                dFormat.format(res.getCheckIn()) + " to " +
                                dFormat.format(res.getCheckOut()) + "\n";
                        prevVals.add(res.getRoomCode());
                        newVals.add(res.getRoomCode());
                        newVals.add(dFormat.format(res.getCheckIn()));
                        newVals.add(dFormat.format(res.getCheckOut()));
                        roomNdx++;
                    }
                    this.actionResult = Results.PROMPT_AGAIN;
                    for (String arg : argsList)
                        lastArgs.add(arg);
                    return retString;
                }
                else {
                    int roomNdx = 1;
                    for (Room room : validRooms) {
                        if (desiredRoom != null && desiredRoom.getCode().equals(room.getCode())) {
                            return this.prepRoomConfirmation(desiredRoom, argsList);
                        }
                        if (roomNdx == 1) {
                            retString += "Please select from one of the available rooms that \n";
                            retString += "match your desired search terms:\n";
                        }
                        retString += roomNdx + ": " + room.getCode() + "\n";
                        prevVals.add(room.getCode());
                        roomNdx++;
                    }
                    this.actionResult = Results.PROMPT_AGAIN;
                    for (String arg : argsList) {
                        lastArgs.add(arg);
                    }
                    return retString;
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

    private boolean roomCodeExists(String roomCode, Connection conn) {
        String queryString = "SELECT RoomCode FROM lab7_rooms";
        List<String> roomCodes = new ArrayList<String>();
        try (PreparedStatement allRooms = conn.prepareStatement(queryString);
             ResultSet rs = allRooms.executeQuery()) {
            while (rs.next()) {
                roomCodes.add(rs.getString("RoomCode"));
            }
        }
        catch (SQLException e)
        {
            ExceptionReporter rp = new ExceptionReporter(e);

            rp.report();
            System.exit(-1);
        }
        for (String rmCode : roomCodes) {
            if (rmCode.equals(roomCode))
                return true;
        }
        return false;
    }

    private List<Reservation> getFiveSuggestions(String startDateStr, String endDateStr, int totalOcc,
                                                 Connection conn) {
        String availableRoomQuery = "with TakenRooms as (SELECT DISTINCT Room FROM lab7_reservations WHERE " +
                "CheckIn >= ? AND CheckIn < ? OR " +
                "CheckOut > ? AND CheckOut <= ?) " +
                "SELECT RoomCode FROM lab7_rooms as r WHERE r.roomCode NOT IN (SELECT Room FROM TakenRooms) AND " +
                "r.maxOcc >= ?";
        int stayLength = ConsoleUtils.getNumDays(startDateStr, endDateStr);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Reservation> fiveRes = new ArrayList<Reservation>();
        Date startDate = null;
        Date endDate = null;

        try {
            startDate = new java.sql.Date(dFormat.parse(startDateStr).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (fiveRes.size() < 5) {
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, stayLength);
            endDate = new java.sql.Date(calendar.getTime().getTime());

            try (PreparedStatement availRooms = conn.prepareStatement(availableRoomQuery)) {
                availRooms.setDate(1, startDate);
                availRooms.setDate(2, endDate);
                availRooms.setDate(3, startDate);
                availRooms.setDate(4, endDate);
                availRooms.setInt(5, totalOcc);

                ResultSet rooms = availRooms.executeQuery();

                while (rooms.next() && fiveRes.size() < 5) {
                    Reservation newRes = new Reservation();
                    newRes.setRoomCode(rooms.getString("RoomCode"));
                    newRes.setCheckIn(startDate);
                    newRes.setCheckOut(endDate);
                    fiveRes.add(newRes);
                }
            } catch (SQLException e) {
                ExceptionReporter rp = new ExceptionReporter(e);
                rp.report();
                System.exit(-1);
            }
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);
            startDate = new java.sql.Date(calendar.getTime().getTime());
        }

        return fiveRes;
    }

    private String prepRoomConfirmation(Room room, List<String> argsList) {
        String retString = "";
        String roomCost = String.valueOf(getRoomCost(room, argsList.get(4), argsList.get(5)));
        retString += "Your desired room is available in your specified time frame.\n";
        retString += "Here are the details of your reservation:\n";
        retString += "Reserved by: " + argsList.get(0) + ", " + argsList.get(1) + "\n";
        retString += "Booking room: " + room.getName() + " (Code: " +
                room.getCode() + ") \n";
        retString += "Bed type: " + room.getBedType() + "\n";
        retString += "Start date: " + argsList.get(4) + "\n";
        retString += "End date: " + argsList.get(5) + "\n";
        retString += "Number of adults: " + argsList.get(7) + "\n";
        retString += "Number of children: " + argsList.get(6) + "\n";
        retString += "Total cost of stay: $" + roomCost + "\n";
        this.actionResult = Results.PROMPT_AGAIN;
        if (this.lastArgs != argsList) {
            for (String arg : argsList)
                this.lastArgs.add(arg);
        }
        this.lastArgs.add(roomCost);
        return retString;
    }

    private float getRoomCost(Room room, String startDateStr, String endDateStr) {
        float totalCost = 0;
        float roomCost = room.getBasePrice()
                .setScale(2, RoundingMode.DOWN).floatValue();
        float weekendCost = roomCost * 1.1f;
        int numDays = ConsoleUtils.getNumDays(startDateStr, endDateStr);
        int numWeekdays = ConsoleUtils.getNumWeekdays(startDateStr, endDateStr);
        int numWeekendDays = numDays - numWeekdays;

        totalCost += numWeekdays * roomCost;
        totalCost += numWeekendDays * weekendCost;
        totalCost += totalCost * 0.18f;

        return totalCost;
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
    public List<String> getLastArgs()        { return lastArgs;}
}
