package com.mantaray.database;

import java.sql.Connection;
import javax.management.Query;
import com.mantaray.interfaces.DatabaseIntf;

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
