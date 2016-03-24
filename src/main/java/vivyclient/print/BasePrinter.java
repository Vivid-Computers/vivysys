package vivyclient.print;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.print.Printable;
import java.util.List;

public abstract class BasePrinter implements Printable {
   public static final int LEFT_ALIGN = 1;
   public static final int CENTRE_ALIGN = 2;
   public static final int RIGHT_ALIGN = 3;
   private ImageObserver imageObserver;
   private List printPages;

   public ImageObserver getImageObserver() {
      return this.imageObserver;
   }

   public void setImageObserver(ImageObserver imageObserver) {
      this.imageObserver = imageObserver;
   }

   protected String getTrimmedString(String string, Graphics2D g2, double maxWidth, boolean appendMoreSymbol) {
      FontMetrics fontMetrics = g2.getFontMetrics();
      if(fontMetrics.getStringBounds(string, g2).getWidth() <= maxWidth) {
         return string;
      } else {
         StringBuffer sb = new StringBuffer(string);
         int offset = 1;
         if(appendMoreSymbol) {
            sb.append("...");
            offset += 3;
         }

         while(fontMetrics.getStringBounds(sb.toString(), g2).getWidth() > maxWidth) {
            sb.deleteCharAt(sb.length() - offset);
         }

         return sb.toString();
      }
   }

   public List getPrintPages() {
      return this.printPages;
   }

   public void setPrintPages(List printPages) {
      this.printPages = printPages;
   }
}
