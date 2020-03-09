package com.lab7.test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.lab7.lib.*;

public class UnitTests
{
    @Test
    public void testHello()
    {
        assertEquals("Hello World!", Hello.getTest());
    }

    @Test
    public void testReporter()
    {
        try
        {
            DriverManager.getConnection("", "", "");
        }
        catch (SQLException e)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.err;
            ExceptionReporter rp = new ExceptionReporter(e);

            System.setErr(ps);
            rp.report();
            System.err.flush();
            System.setErr(old);
            String expected = 
                "Error: No suitable driver found for \r\n" +
                "Error Code: 0\r\n" +
                "SQLState: 08001\r\n";
            
            assertEquals(expected, baos.toString());
        }
    }

    @Test
    public void testRoomGet()
    {
        try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
                                                            System.getenv("HP_JDBC_USER"),
                                                            System.getenv("HP_JDBC_PW")))
        {
            Room rm = Room.fromDatabase("AOB", conn);

            String expected =
                "Room[roomCode=AOB, " +
                "roomName=Abscond or bolster, " +
                "beds=2, " +
                "bedType=Queen, " +
                "maxOcc=4, " +
                "basePrice=175.00, " +
                "decor=traditional]";

            assertEquals(expected, rm.toString());
        }
        catch (SQLException e)
        {
            ExceptionReporter rp = new ExceptionReporter(e);

            rp.report();
            System.exit(-1);
        }
    }

    @Test
    public void testReservationGet()
    {
        try (Connection conn = DriverManager.getConnection(System.getenv("HP_JDBC_URL"),
                                                            System.getenv("HP_JDBC_USER"),
                                                            System.getenv("HP_JDBC_PW")))
        {
            Reservation rn = Reservation.fromDatabase(10105, conn);

            String expected =
                "Reservation[code=10105" +
                ", room=HBB" +
                ", checkIn=2020-05-23" +
                ", checkOut=2020-05-25" +
                ", rate=100.00" +
                ", lastName=SELBIG" +
                ", firstName=CONRAD" +
                ", adults=1" +
                ", kids=0]";

            assertEquals(expected, rn.toString());
        }
        catch (SQLException e)
        {
            ExceptionReporter rp = new ExceptionReporter(e);

            rp.report();
            System.exit(-1);
        }
    }
}