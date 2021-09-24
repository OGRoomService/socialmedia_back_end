package com.mantaray.Database;

import java.sql.Connection;
import javax.management.Query;

import com.mantaray.Interfaces.DatabaseIntf;

public class mySQLDatabaseConnection implements DatabaseIntf
{
    @Override
    public boolean connectToDatabase() 
    {
        return false;
    }

    protected Connection dbConnection;
    protected Query dbQuery;
}
