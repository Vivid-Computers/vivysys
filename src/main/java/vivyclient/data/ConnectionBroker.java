package vivyclient.data;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import vivyclient.Client;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionBroker {
    private static JtdsDataSource ds;

    public static Connection getConnection() throws Exception {
        if (ds == null) {
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:jtds:sqlserver://" + Settings.getDBHostName() + ":"
//                    + Settings.getDBServerPort() + "/vivysys;TDS=4.2", Settings.getDBUserName(), Settings.getDBUserPassword());



            ds = new JtdsDataSource();

            ds.setServerName(Settings.getDBHostName());
            ds.setPortNumber(Integer.parseInt(Settings.getDBServerPort()));
            ds.setDatabaseName(Settings.getDBName());
            ds.setUser(Settings.getDBUserName());
            ds.setPassword(Settings.getDBUserPassword());
            ds.setLoginTimeout(10);
            ds.setDescription("A Test Data Source");
            ds.setPortNumber(Integer.parseInt(Settings.getDBServerPort()));
        }

        return ds.getPooledConnection().getConnection();
    }

    public static void resetConnection() {
        ds = null;
    }

}
