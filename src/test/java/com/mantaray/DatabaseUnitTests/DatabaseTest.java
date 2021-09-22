package com.mantaray.DatabaseUnitTests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mantaray.database.mySQLDatabaseConnection;

public class DatabaseTest 
{
    mySQLDatabaseConnection db = new mySQLDatabaseConnection();

    @Test
    void givenNoPreconditions_WhenConnectToDatabaseCalled_ThenReturnFalse() 
    {
        assertEquals(db.connectToDatabase(), false);
    }
}
