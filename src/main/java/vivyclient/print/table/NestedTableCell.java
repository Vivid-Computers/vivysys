package vivyclient.print.table;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import vivyclient.print.PrintProgress;
import vivyclient.print.table.AbstractTableCell;
import vivyclient.print.table.PrintableTable;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRow;

public class NestedTableCell extends AbstractTableCell implements TableCell {
   private PrintableTable table;
   private double height;

   public NestedTableCell(PrintableTable table, TableRow row, TableColumn startColumn, int xSpan) {
      super(row, startColumn, xSpan);
      this.table = table;
   }

   public void initialise(Graphics2D g) {
      System.out.println("[NestedTableCell] set preferred width...");
      this.setPreferredWidth(this.table.getPreferredWidth(g));
      System.out.println("[NestedTableCell] Preferred width is " + this.getPreferredWidth());
   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      AffineTransform initialTransform = g.getTransform();
      System.out.println("[NestedTableCell] Start printing at " + startY + (hidden?" (hidden)":""));

      Double var11;
      try {
         double startX = this.getStartColumn().getX();
         AffineTransform transform = new AffineTransform();
         transform.translate(startX, startY);
         g.transform(transform);
         System.out.println("[NestedTableCell] Initialise nested table for print width " + this.getWidth());
         this.table.setStartX(0.0D);
         this.table.setWidth(this.getWidth());
         this.table.initialise(g);
         System.out.println("[NestedTableCell] Print nested table...");
         PrintProgress tableProgress = new PrintProgress();
         tableProgress.setTotalParts(this.table.getBlocks().size());
         Rectangle2D bounds = this.table.print(g, tableProgress, 0.0D, hidden, java.lang.Double.MAX_VALUE);
         var11 = new Double(bounds.getX() + startX, bounds.getY() + startY, bounds.getWidth(), bounds.getHeight());
      } finally {
         System.out.println("[NestedTableCell] Nested table printing complete!");
         g.setTransform(initialTransform);
      }

      return var11;
   }
}
