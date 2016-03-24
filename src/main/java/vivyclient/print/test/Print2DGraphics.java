package vivyclient.print.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.Locale;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.DocFlavor.SERVICE_FORMATTED;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;

public class Print2DGraphics implements Printable {
   public Print2DGraphics() {
      SERVICE_FORMATTED flavor = SERVICE_FORMATTED.PRINTABLE;
      HashPrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
      aset.add(OrientationRequested.LANDSCAPE);
      aset.add(new Copies(2));
      aset.add(new JobName("My job", (Locale)null));
      PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, aset);
      if(services.length > 0) {
         System.out.println("selected printer " + services[0].getName());
         DocPrintJob pj = services[0].createPrintJob();

         try {
            SimpleDoc e = new SimpleDoc(this, flavor, (DocAttributeSet)null);
            pj.print(e, aset);
         } catch (PrintException var6) {
            System.err.println(var6);
         }
      }

   }

   public int print(Graphics g, PageFormat pf, int pageIndex) {
      if(pageIndex == 0) {
         Graphics2D g2d = (Graphics2D)g;
         g2d.translate(pf.getImageableX(), pf.getImageableY());
         g2d.setColor(Color.black);
         g2d.drawString("example string", 250, 250);
         g2d.fillRect(0, 0, 200, 200);
         return 0;
      } else {
         return 1;
      }
   }

   public static void main(String[] arg) {
      new Print2DGraphics();
   }
}
