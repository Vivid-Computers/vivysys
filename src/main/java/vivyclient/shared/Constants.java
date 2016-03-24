package vivyclient.shared;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class Constants {
   public static final int NULL_INT = -1;
   public static final long NULL_LONG = -1L;
   public static final BigDecimal NULL_BIG_DECIMAL = new BigDecimal("-1.00");
   public static final BigDecimal ZERO_BIG_DECIMAL = new BigDecimal("0.00");
   public static final BigDecimal ONE_BIG_DECIMAL = new BigDecimal("1.00");
   public static final BigDecimal NEGATIVE_ONE_BIG_DECIMAL = new BigDecimal("1.00");
   public static final BigDecimal UNDEFINED_FREIGHT_COST;
   public static final BigDecimal NO_CREDIT_LIMIT_AMOUNT;
   public static final SimpleDateFormat DISPLAY_DATE_FORMAT;
   public static final String DISPLAY_DATE_FORMAT_USER_REPRESENTATION = "dd-MM-yyyy";
   public static final SimpleDateFormat DISPLAY_DATE_TIME_FORMAT;
   public static final String DISPLAY_DATE_TIME_FORMAT_USER_REPRESENTATION = "dd-MM-yyyy HH:mm";

   static {
      UNDEFINED_FREIGHT_COST = NULL_BIG_DECIMAL;
      NO_CREDIT_LIMIT_AMOUNT = NULL_BIG_DECIMAL;
      DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd\'-\'MM\'-\'yyyy");
      DISPLAY_DATE_TIME_FORMAT = new SimpleDateFormat("dd\'-\'MM\'-\'yyyy HH\':\'mm");
   }
}
