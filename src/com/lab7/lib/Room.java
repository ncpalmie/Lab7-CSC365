package com.lab7.lib;

import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

public class Room
{
    private String roomCode;
    private String roomName;
    private int beds;
    private String bedType;
    private int maxOcc;
    private BigDecimal basePrice;
    private String decor;

    /**
     * Creates a Room instance retrieved from the database
     * 
     * @param code The identifying room code to retrieve, pre-sanitized
     * @param conn The connection to the database to retrieve from
     * 
     * @return A Room instance that contains the data in the database
     */
    public static Room fromDatabase(String roomCode, Connection conn)
    {
        Room newRoom = new Room();
        newRoom.roomCode = roomCode;

        String query = "SELECT * FROM lab7_rooms WHERE RoomCode = \'" + roomCode + "\'";

        try (Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(query);

            rs.next();

            newRoom.roomName =     rs.getString(2);
            newRoom.beds =         rs.getInt(3);
            newRoom.bedType =      rs.getString(4);
            newRoom.maxOcc =       rs.getInt(5);
            newRoom.basePrice =    rs.getBigDecimal(6);
            newRoom.decor =        rs.getString(7);
        }
        catch (SQLException e)
        {
            ExceptionReporter rp = new ExceptionReporter(e);

            rp.report();
            System.exit(-1);
        }

        return newRoom;
    }

    public String getCode()             { return roomCode; }
    public String getName()             { return roomName; }
    public int getBeds()                { return beds; }
    public String getBedType()          { return bedType; }
    public int getMaxOcc()              { return maxOcc; }
    public BigDecimal getBasePrice()    { return basePrice; }
    public String getDecor()            { return decor; }

    @Override
    public String toString()
    {
        String str = "Room[roomCode=" + roomCode +
            ", roomName=" + roomName +
            ", beds=" + beds +
            ", bedType=" + bedType +
            ", maxOcc=" + maxOcc +
            ", basePrice=" + basePrice +
            ", decor=" + decor + "]";

        return str;
    }
}