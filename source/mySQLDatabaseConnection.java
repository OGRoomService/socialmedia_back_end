package socialmedia_back_end.source;

import socialmedia_back_end.source.interfaces.databaseConnection_intf;

public class mySQLDatabaseConnection implements databaseConnection_intf
{
    public mySQLDatabaseConnection(String node, String username, String password)
    {
        this.node = node;
        this.username = username;
        this.password = password;
    }

    public boolean connectToDatabase()
    {
        //TODO Make this actually connect to the AWS Database
        System.out.println("Connecting to: {" + node + "} \nUsername: {" + username + "} \nPassword: {" + password + "}");
        return true;
    }

    String node;
    String username;
    String password;
}