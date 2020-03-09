package com.lab7.lib;

import java.sql.SQLException;

public class ExceptionReporter
{
    private SQLException exception;

    public ExceptionReporter(SQLException e)
    {
        exception = e;
    }

    /**
     * Reports SQL Exception details to ERR stream
     */
    public void report()
    {
        System.err.println("Error: " + exception.getMessage());
        System.err.println("Error Code: " + exception.getErrorCode());
        System.err.println("SQLState: " + exception.getSQLState());
    }
}