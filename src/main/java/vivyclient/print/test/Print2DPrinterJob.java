package vivyclient.print.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Locale;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;

public class Print2DPrinterJob implements Printable {
   public Print2DPrinterJob() {
      HashPrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
      aset.add(OrientationRequested.LANDSCAPE);
      aset.add(new Copies(2));
      aset.add(new JobName("My job", (Locale)null));
      PrinterJob pj = PrinterJob.getPrinterJob();
      pj.setPrintable(this);
      PrintService[] services = PrinterJob.lookupPrintServices();
      if(services.length > 0) {
         System.out.println("selected printer " + services[0].getName());

         try {
            pj.setPrintService(services[0]);
            pj.pageDialog(aset);
            if(pj.printDialog(aset)) {
               pj.print(aset);
            }
         } catch (PrinterException var5) {
            System.err.println(var5);
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
      new Print2DPrinterJob();
      System.exit(0);
   }
}
