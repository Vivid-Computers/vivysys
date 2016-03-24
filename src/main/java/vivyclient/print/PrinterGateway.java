package vivyclient.print;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Locale;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.MediaSize.ISO;
import vivyclient.Client;
import vivyclient.print.BasePrinter;
import vivyclient.print.PrintPreview;

public class PrinterGateway {
   public static void handlePrintRequest(BasePrinter printable, boolean preview) throws Exception {
      if(preview) {
         printPreview(printable);
      } else {
         print(printable);
      }

   }

   public static PrintRequestAttributeSet getDefaultPrintAttributes() {
      HashPrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
      aset.add(OrientationRequested.PORTRAIT);
      aset.add(new Copies(1));
      aset.add(new JobName("VivyClient Print", (Locale)null));
      aset.add(MediaSizeName.ISO_A4);
      MediaSize size = MediaSize.getMediaSizeForName(MediaSizeName.ISO_A4);
      float width = size.getX(1000);
      float height = size.getY(1000);
      float defaultMargin = 15.0F;
      MediaPrintableArea printableArea = new MediaPrintableArea(defaultMargin, defaultMargin, width - 2.0F * defaultMargin, height - 2.0F * defaultMargin, 1000);
      aset.add(printableArea);
      return aset;
   }

   public static PageFormat getDefaultPageFormat() {
      PageFormat format = new PageFormat();
      double multiplier = 2.834645669291339D;
      double width = (double)ISO.A4.getX(1000) * multiplier;
      double height = (double)ISO.A4.getY(1000) * multiplier;
      double topMargin = 15.0D * multiplier;
      double bottomMargin = 15.0D * multiplier;
      double leftMargin = 15.0D * multiplier;
      double rightMargin = 15.0D * multiplier;
      Paper paper = new Paper();
      paper.setSize(width, height);
      paper.setImageableArea(leftMargin, topMargin, width - leftMargin - rightMargin, height - topMargin - bottomMargin);
      format.setPaper(paper);
      format.setOrientation(1);
      return format;
   }

   private static void printPreview(BasePrinter printable) throws Exception {
      PrintPreview previewFrame = new PrintPreview(printable, getDefaultPageFormat());
      printable.setImageObserver(previewFrame);
      Client.addInternalFrame(previewFrame);
      previewFrame.setResizable(true);
      previewFrame.setClosable(true);
      previewFrame.setMaximizable(true);
      previewFrame.setIconifiable(true);
      previewFrame.setVisible(true);
   }

   private static void print(BasePrinter printable) throws Exception {
      PrintRequestAttributeSet aset = getDefaultPrintAttributes();
      PrinterJob pj = PrinterJob.getPrinterJob();
      pj.setPrintable(printable);
      PrintService[] services = PrinterJob.lookupPrintServices();
      if(services.length > 0) {
         if(pj.printDialog(aset)) {
            pj.print(aset);
         }

      } else {
         throw new PrinterException("Unable to print - no printer found");
      }
   }
}
