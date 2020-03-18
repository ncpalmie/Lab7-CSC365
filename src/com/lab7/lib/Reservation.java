package com.lab7.lib;

import java.util.Calendar;
import java.util.TimeZone;
import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;

/**
 * Reservation data wrapper class
 */
public class Reservation
{
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

        final String query = "SELECT * FROM lab7_reservations WHERE CODE = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setInt(1, code);
            try (ResultSet rs = stmt.executeQuery())
            {        
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