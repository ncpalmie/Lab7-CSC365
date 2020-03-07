package com.lab7.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.lab7.lib.Hello;

public class UnitTests
{
    @Test
    public void testHello()
    {
        assertEquals("Hello World!", Hello.getTest());
    }
}