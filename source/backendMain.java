package socialmedia_back_end.source;

import socialmedia_back_end.source.interfaces.databaseConnection_intf;

public class backendMain 
{
    public static void main(String[] args) 
    {
        databaseConnection_intf myDatabase = new mySQLDatabaseConnection("IP", "Username", "Password");

        System.out.println(myDatabase.connectToDatabase());
    }
}
