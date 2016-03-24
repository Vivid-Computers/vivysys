package vivyclient.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import vivyclient.shared.Constants;

public class ViewUtil {
   private static DecimalFormat currencyFormat = new DecimalFormat("0.00");
   private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

   public static String calendarDisplay(Calendar calendar) {
      return calendar != null?Constants.DISPLAY_DATE_FORMAT.format(calendar.getTime()):"";
   }

   public static String calendarDateTimeDisplay(Calendar calendar) {
      return calendar != null?Constants.DISPLAY_DATE_TIME_FORMAT.format(calendar.getTime()):"";
   }

   public static Calendar parseDate(String dateString) throws Exception {
      Calendar date = Calendar.getInstance();
      date.setTime(Constants.DISPLAY_DATE_FORMAT.parse(dateString));
      if(!Constants.DISPLAY_DATE_FORMAT.format(date.getTime()).equals(dateString)) {
         throw new ParseException("Invalid date: " + dateString, 0);
      } else {
         return date;
      }
   }

   public static String truncate(String string, int maxLength) {
      return string != null && string.length() > maxLength?string.substring(0, maxLength - 3) + "...":string;
   }

   public static String viewTruncate(String string, int maxLength) {
      return string != null && string.length() > maxLength?string.substring(0, maxLength - 3) + "...":string;
   }

   public static String currencyDisplay(BigDecimal value) throws NumberFormatException {
      return value == null?"":currencyFormat.format(value.setScale(2, 4));
   }

   public static String balanceCurrencyDisplay(BigDecimal value) {
      return value == null?"":(value.compareTo(Constants.ZERO_BIG_DECIMAL) >= 0?currencyDisplay(value):currencyDisplay(value.abs()) + " CR");
   }

   public static String decimalDisplay(BigDecimal value) {
      return value == null?"":decimalFormat.format(value);
   }

   public static BigDecimal parseDecimalAmount(String value) {
      BigDecimal result = new BigDecimal(value);
      if(result.scale() > 2) {
         result.setScale(2, 4);
      }

      return result;
   }

   public static BigDecimal parseCurrencyAmount(String value) throws NumberFormatException {
      return (new BigDecimal(value)).setScale(2, 4);
   }

   public static String intDisplay(int value, boolean showNullInt) {
      return !showNullInt && value == -1?"":String.valueOf(value);
   }
}
