package vivyclient.print.table;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;
import vivyclient.exception.AppRuntimeException;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRegion;
import vivyclient.print.table.TableRow;

public class BlankTableRow extends TableRegion implements TableRow {
   private TableBlock block;
   private double height;
   private Color fillColour;

   public BlankTableRow(TableBlock block, Color fillColour, double height) {
      super(block);
      this.block = block;
      this.height = height;
      this.fillColour = fillColour;
      block.addRow(this);
   }

   public void initialise(Graphics2D g) {
   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      double startX = this.block.getTable().getStartX();
      double width = this.block.getTable().getWidth();
      if(!hidden && this.fillColour != null) {
         g.setColor(this.fillColour);
         g.fillRect((int)startX, (int)startY, (int)width, (int)this.height);
      }

      return new Double(startX, startY, width, this.height);
   }

   public List getCellsForColumn(TableColumn column) {
      return new ArrayList();
   }

   public List getAllMultiColumnCells() {
      return new ArrayList();
   }

   public void addCell(TableCell cell) {
      throw new AppRuntimeException();
   }
}
