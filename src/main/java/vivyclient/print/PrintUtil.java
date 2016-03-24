package vivyclient.print;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class PrintUtil {
   public static String getLongestFittingString(String string, boolean breakOnHyphen, Graphics2D g, double maxWidth) {
      StringBuffer s = new StringBuffer();
      FontMetrics fm = g.getFontMetrics();

      for(int index = 0; index < string.length(); ++index) {
         s.append(string.charAt(index));
         if(fm.getStringBounds(s.toString(), g).getWidth() > maxWidth) {
            s.deleteCharAt(index);
            break;
         }

         if(breakOnHyphen && string.charAt(index) == 45 && index > 0) {
            break;
         }
      }

      return s.toString();
   }
}
