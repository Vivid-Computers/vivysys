package vivyclient.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

public class Settings {
   private static final String DIV = "/";
   private static final String USER_HAS_SETUP = "/userHasSetup";
   private static final String GST_RATE = "/business/gstRate";
   private static final String LOG_ERRORS = "/logging/errors/logErrors";
   private static final String ERROR_LOG_FILE_PATH = "/logging/errors/errorLogFile";
   private static final String MAX_ERROR_LOG_FILE_SIZE = "/logging/errors/maxLogSize";
   private static final String LOG_SQL_READS = "/logging/sql/logSqlReads";
   private static final String LOG_SQL_WRITES = "/logging/sql/logSqlWrites";
   private static final String SQL_LOG_FILE_PATH = "/logging/sql/sqlLogFile";
   private static final String MAX_SQL_LOG_FILE_SIZE = "/logging/sql/maxLogSize";
   private static final String GUI = "/gui";
   private static final String X_POS = "xPos";
   private static final String Y_POS = "yPos";
   private static final String WIDTH = "width";
   private static final String HEIGHT = "height";
   private static final String MAXIMIZED = "maximized";
   private static final String STATE = "state";
   private static final String DIVIDER_LOCATION = "dividerLocation";
   private static final String LOOK_AND_FEEL_CLASS_NAME = "lookAndFeelClassName";
   private static final String LOOK_AND_FEEL_THEME_CLASS_NAME = "lookAndFeelThemeClassName";
   private static final String DB_HOST_NAME = "/db/connection/hostName";
   private static final String DB_SERVER_PORT = "/db/connection/serverPort";
   private static final String DB_NAME = "/db/connection/name";
   private static final String DB_USER_NAME = "/db/connection/userName";
   private static final String DB_USER_PASSWORD = "/db/connection/password";
   private static final String ALLOW_BLANK_STRINGS_IN_DB = "/db/saving/allowBlankStringsInDB";
   private static final String PRINT_FOOTER_TAGS = "/print/all/footerTags";
   private static Preferences system = Preferences.systemRoot();
   private static Preferences user = Preferences.userRoot();
   private static String defaultFooterTags = "Shop online, securely: www.vividcomputers.co.nz\nChoose vivid.net when connecting to the Internet\nUpgrading to JetStream?  Best prices at vivid.net\nNeed a website?  Let vivid.net create and host it!\nThankyou for choosing Vivid Computers";

   public static boolean getUserHasSetup() {
      return user.getBoolean("/userHasSetup", false) && getDBHostName().length() > 0;
   }

   public static void setUserHasSetup(boolean userHasSetup) {
      user.putBoolean("/userHasSetup", userHasSetup);
   }

   public static BigDecimal getGSTRate() {
      return new BigDecimal("0.150");
   }

   public static void setGSTRate(BigDecimal rate) {
   }

   public static boolean getLogErrors() {
      return system.getBoolean("/logging/errors/logErrors", true);
   }

   public static void setLogErrors(boolean logErrors) {
      system.putBoolean("/logging/errors/logErrors", logErrors);
   }

   public static boolean getLogSQLReads() {
      return system.getBoolean("/logging/sql/logSqlReads", true);
   }

   public static void setLogSQLReads(boolean logSQLReads) {
      system.putBoolean("/logging/sql/logSqlReads", logSQLReads);
   }

   public static boolean getLogSQLWrites() {
      return system.getBoolean("/logging/sql/logSqlWrites", true);
   }

   public static void setLogSQLWrites(boolean logSQLWrites) {
      system.putBoolean("/logging/sql/logSqlWrites", logSQLWrites);
   }

   public static String getErrorLogFilePath() {
      return system.get("/logging/errors/errorLogFile", "error_log.txt");
   }

   public static void setErrorLogFilePath(String errorLogFilePath) {
      system.put("/logging/errors/errorLogFile", errorLogFilePath);
   }

   public static String getSqlLogFilePath() {
      return system.get("/logging/sql/sqlLogFile", "sql_log.txt");
   }

   public static void setSqlLogFilePath(String sqlLogFilePath) {
      system.put("/logging/sql/sqlLogFile", sqlLogFilePath);
   }

   public static long getMaxErrorLogFileSize() {
      return system.getLong("/logging/errors/maxLogSize", 1048576L);
   }

   public static void setMaxErrorLogFileSize(long maxErrorLogFileSize) {
      system.putLong("/logging/errors/maxLogSize", maxErrorLogFileSize);
   }

   public static int getXPos(String componentName) {
      return user.getInt("/gui/" + componentName + "/" + "xPos", 50);
   }

   public static void setXPos(String componentName, int xPos) {
      user.putInt("/gui/" + componentName + "/" + "xPos", xPos);
   }

   public static int getYPos(String componentName) {
      return user.getInt("/gui/" + componentName + "/" + "yPos", 50);
   }

   public static void setYPos(String componentName, int yPos) {
      user.putInt("/gui/" + componentName + "/" + "yPos", yPos);
   }

   public static int getWidth(String componentName) {
      return user.getInt("/gui/" + componentName + "/" + "width", 640);
   }

   public static void setWidth(String componentName, int width) {
      user.putInt("/gui/" + componentName + "/" + "width", width);
   }

   public static int getHeight(String componentName) {
      return user.getInt("/gui/" + componentName + "/" + "height", 480);
   }

   public static void setHeight(String componentName, int height) {
      user.putInt("/gui/" + componentName + "/" + "height", height);
   }

   public static boolean getMaximized(String componentName) {
      return user.getBoolean("/gui/" + componentName + "/" + "maximized", false);
   }

   public static void setMaximized(String componentName, boolean maximized) {
      user.putBoolean("/gui/" + componentName + "/" + "maximized", maximized);
   }

   public static int getDividerLocation(String componentName) {
      return user.getInt("/gui/" + componentName + "/" + "dividerLocation", getWidth(componentName) / 3);
   }

   public static void setDividerLocation(String componentName, int dividerLocation) {
      user.putInt("/gui/" + componentName + "/" + "dividerLocation", dividerLocation);
   }

   public static int getState(String componentName) {
      return user.getInt("/gui/" + componentName + "/" + "state", 6);
   }

   public static void setState(String componentName, int state) {
      user.putInt("/gui/" + componentName + "/" + "state", state);
   }

   public static String getLookAndFeelClassName() {
      return user.get("/gui/lookAndFeelClassName", "javax.swing.plaf.metal.MetalLookAndFeel");
   }

   public static void setLookAndFeelClassName(String lookAndFeelClassName) {
      user.put("/gui/lookAndFeelClassName", lookAndFeelClassName);
   }

   public static String getLookAndFeelThemeClassName() {
      return user.get("/gui/lookAndFeelThemeClassName", (String)null);
   }

   public static void setLookAndFeelThemeClassName(String lookAndFeelThemeClassName) {
      user.put("/gui/lookAndFeelThemeClassName", lookAndFeelThemeClassName);
   }

   public static String getDBHostName() {
      return user.get("/db/connection/hostName", "");
   }

   public static void setDBHostName(String hostName) {
      user.put("/db/connection/hostName", hostName);
   }

   public static String getDBServerPort() {
      return user.get("/db/connection/serverPort", "1433");
   }

   public static void setDBServerPort(String serverPort) {
      user.put("/db/connection/serverPort", serverPort);
   }

   public static String getDBName() {
      return user.get("/db/connection/name", "vivysys");
   }

   public static void setDBName(String name) {
      user.put("/db/connection/name", name);
   }

   public static String getDBUserName() {
      return user.get("/db/connection/userName", "");
   }

   public static void setDBUserName(String userName) {
      user.put("/db/connection/userName", userName);
   }

   public static String getDBUserPassword() {
      return user.get("/db/connection/password", "");
   }

   public static void setDBUserPassword(String userPassword) {
      user.put("/db/connection/password", userPassword);
   }

   public static boolean getAllowBlankStringsInDB() {
      return system.getBoolean("/db/saving/allowBlankStringsInDB", false);
   }

   public static void setAllowBlankStringsInDB(boolean allowBlankStrings) {
      system.putBoolean("/db/saving/allowBlankStringsInDB", allowBlankStrings);
   }

   public static long getInfoMessageDisplayPeriod() {
      return 5000L;
   }

   public static String getDBDateFormatString() {
      return "yyyy-MM-dd HH:mm:ss";
   }

   public static String getPrintFooterTags() {
      return system.get("/print/all/footerTags", defaultFooterTags);
   }

   public static void setPrintFooterTags(String printFooterTags) {
      system.put("/print/all/footerTags", printFooterTags);
   }

   public static List getPrintFooterTagsList() {
      StringTokenizer footerTokens = new StringTokenizer(getPrintFooterTags(), "\n");
      ArrayList tags = new ArrayList();

      while(footerTokens.hasMoreTokens()) {
         String slogan = footerTokens.nextToken().trim();
         if(slogan.length() > 0) {
            tags.add(slogan);
         }
      }

      return tags;
   }

   public static String getRandomFooterTag() {
      List tags = getPrintFooterTagsList();
      int index = (int)(Math.random() * (double)tags.size());
      return (String)tags.get(index);
   }

   public static int getNullInt() {
      return -1;
   }

   public static long getNullLong() {
      return -1L;
   }
}
