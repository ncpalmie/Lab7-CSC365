package com.lab7.lib;

import java.util.Calendar;
import java.util.TimeZone;
import java.math.BigDecimal;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;

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

    public Reservation(int code, Connection conn)
    {
        this.code = code;

        String query = "SELECT * FROM lab7_reservations WHERE CODE = " + code;

        try (Statement stmt = conn.createStatement())
        {
            ResultSet rs = stmt.executeQuery(query);

            rs.next();

            // TODO: Investigate a better solution
            Calendar pst = Calendar.getInstance(TimeZone.getTimeZone("PST"));

            this.room =         rs.getString(2);
            this.checkIn =      rs.getDate(3, pst);
            this.checkOut =     rs.getDate(4, pst);
            this.rate =         rs.getBigDecimal(5);
            this.lastName =     rs.getString(6);
            this.firstName =    rs.getString(7);
            this.adults =       rs.getInt(8);
            this.kids =         rs.getInt(9);
        }
        catch (SQLException e)
        {
            ExceptionReporter rp = new ExceptionReporter(e);

            rp.report();
            System.exit(-1);
        }
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