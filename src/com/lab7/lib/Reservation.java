package com.lab7.lib;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.math.BigDecimal;

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
        }
        catch (SQLException e)
        {
            ExceptionReporter rp = new ExceptionReporter(e);

            rp.report();
            System.exit(-1);
        }

        return newRes;
    }

    public static List<Reservation> perRoom(Room rm, Connection conn)
    {
        List<Reservation> res = new ArrayList<Reservation>();
        String query = "SELECT CODE FROM lab7_reservations WHERE Room = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setString(1, rm.getCode());
            try (ResultSet rs = stmt.executeQuery())
            {
                if (!rs.next()) {
                    return null;
                }
                rs.beforeFirst();

                while(rs.next())
                {
                    res.add(Reservation.fromDatabase(rs.getInt(1), conn));
                }
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

        return res;
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

    public static int getUniqueCode(Connection conn) {
        int code = 0;
        String codeQuery;
        codeQuery = "SELECT Code FROM lab7_reservations WHERE code >= ALL (SELECT Code FROM lab7_reservations)";

        try(PreparedStatement codeStmt = conn.prepareStatement(codeQuery);) {
            ResultSet codeRs = codeStmt.executeQuery();
            codeRs.next();
            code = codeRs.getInt("Code");
            code++;
        }
        catch (SQLException e) {
            ExceptionReporter rp = new ExceptionReporter(e);
            rp.report();
            System.exit(-1);
        }
        return code;
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
