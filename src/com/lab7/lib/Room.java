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

    public Room(String roomCode, Connection conn)
    {
        this.roomCode = roomCode;

        String query = "SELECT * FROM lab7_rooms WHERE RoomCode = \'" + roomCode + "\'";

        try (Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(query);

            rs.next();

            this.roomName =     rs.getString(2);
            this.beds =         rs.getInt(3);
            this.bedType =      rs.getString(4);
            this.maxOcc =       rs.getInt(5);
            this.basePrice =    rs.getBigDecimal(6);
            this.decor =        rs.getString(7);
        }
        catch (SQLException e)
        {
            ExceptionReporter rp = new ExceptionReporter(e);

            rp.report();
            System.exit(-1);
        }
    }

    public Room(String roomCode, String roomName, int beds, String bedType, int maxOcc, BigDecimal basePrice, String decor)
    {
        this.roomCode = roomCode;
        this.roomName = roomName;
        this.beds = beds;
        this.bedType = bedType;
        this.maxOcc = maxOcc;
        this.basePrice = basePrice;
        this.decor = decor;
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