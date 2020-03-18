package com.lab7.lib;

import java.util.Calendar;
import java.util.TimeZone;
import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;

/**
 * Reservation data wrapper class
 */
public class Reservation
{
    public static int uniqueCode = 100000;
    private int code;
    private String room;
    private Date checkIn;
    private Date checkOut;
    private BigDecimal rate;
    private String lastName;
    private String firstName;
    private int adults;
    private int kids;

    /**
     * Creates a Reservation instance retrieved from the database
     * 
     * @param code The identifying reservation code to retrieve
     * @param conn The connection to the database to retrieve from
     * 
     * @return A Reservation instance that contains the data in the database
     */
    public static Reservation fromDatabase(int code, Connection conn)
    {
        Reservation newRes = new Reservation();
        newRes.code = code;

        String query = "SELECT * FROM lab7_reservations WHERE CODE = " + code;

        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query))
        {
            if (!rs.next()) {
                return null;
            }
            rs.beforeFirst();
            rs.next();

            // TODO: Investigate a better solution
            Calendar pst = Calendar.getInstance(TimeZone.getTimeZone("PST"));

            newRes.room =       rs.getString(2);
            newRes.checkIn =    rs.getDate(3, pst);
            newRes.checkOut =   rs.getDate(4, pst);
            newRes.rate =       rs.getBigDecimal(5);
            newRes.lastName =   rs.getString(6);
            newRes.firstName =  rs.getString(7);
            newRes.adults =     rs.getInt(8);
            newRes.kids =       rs.getInt(9);
        }
        catch (SQLException e)
        {
            ExceptionReporter rp = new ExceptionReporter(e);

            rp.report();
            System.exit(-1);
        }

        return newRes;
    }

    public int getCode()            { return code; }
    public String getRoomCode()     { return room; }
    public Date getCheckIn()        { return checkIn; }
    public Date getCheckOut()       { return checkOut; }
    public BigDecimal getRate()     { return rate; }
    public String getLastName()     { return lastName; }
    public String getFirstName()    { return firstName; }
    public int getAdults()          { return adults; }
    public int getKids()            { return kids; }

    public void setRoomCode(String rmCode)  {this.room = rmCode;}
    public void setCheckIn(Date date)       {this.checkIn = date;}
    public void setCheckOut(Date date)      {this.checkOut = date;}

    public static int getUniqueCode() {
        Reservation.uniqueCode++;
        return Reservation.uniqueCode;
    }

    @Override
    public String toString()
    {
        String str = "Reservation[code=" + code +
            ", room=" + room +
            ", checkIn=" + checkIn +
            ", checkOut=" + checkOut +
            ", rate=" + rate +
            ", lastName=" + lastName +
            ", firstName=" + firstName +
            ", adults=" + adults +
            ", kids=" + kids + "]";

        return str;
    }
}