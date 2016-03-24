package vivyclient.util;

import java.util.Calendar;

public class Util {
   public static Calendar getStartOfToday() {
      Calendar today = Calendar.getInstance();
      today.set(11, 0);
      today.set(12, 0);
      today.set(13, 0);
      today.set(14, 0);
      return today;
   }
}
