package vivyclient.print.table;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRegion;
import vivyclient.print.table.TableRow;

public class DefaultTableRow extends TableRegion implements TableRow {
   private List cells;
   private TableBlock block;
   private String verticalAlign;

   public DefaultTableRow(TableBlock block) {
      this(block, "N");
   }

   public DefaultTableRow(TableBlock block, String verticalAlign) {
      super(block);
      this.block = block;
      this.verticalAlign = verticalAlign;
      block.addRow(this);
      this.cells = new ArrayList();
   }

   public void initialise(Graphics2D g) {
   }

   public Rectangle2D print(Graphics2D g, double startY, boolean hidden) {
      return !"N".equals(this.verticalAlign)?this.printSouth(g, startY, hidden):this.printNorth(g, startY, hidden);
   }

   public Rectangle2D printNorth(Graphics2D g, double startY, boolean hidden) {
      Double bounds = new Double(0.0D, startY, 0.0D, 0.0D);

      for(int i = 0; i < this.cells.size(); ++i) {
         bounds.add(((TableCell)this.cells.get(i)).print(g, startY, hidden));
      }

      return bounds;
   }

   public Rectangle2D printSouth(Graphics2D g, double startY, boolean hidden) {
      Double bounds = new Double(0.0D, startY, 0.0D, 0.0D);
      double[] heights = new double[this.cells.size()];

      int i;
      for(i = 0; i < this.cells.size(); ++i) {
         Rectangle2D cellStartY = ((TableCell)this.cells.get(i)).print(g, startY, true);
         bounds.add(cellStartY);
         heights[i] = cellStartY.getHeight();
      }

      if(!hidden) {
         for(i = 0; i < this.cells.size(); ++i) {
            double var10 = startY + bounds.height - heights[i];
            ((TableCell)this.cells.get(i)).print(g, var10, false);
         }
      }

      return bounds;
   }

   public void addCell(TableCell cell) {
      this.cells.add(cell);
   }

   public List getCellsForColumn(TableColumn column) {
      ArrayList columnCells = new ArrayList();

      for(int i = 0; i < this.cells.size(); ++i) {
         TableCell cell = (TableCell)this.cells.get(i);
         if(cell.getStartColumn().getIndex() == column.getIndex()) {
            columnCells.add(cell);
         }
      }

      return columnCells;
   }

   public List getAllMultiColumnCells() {
      ArrayList multiColumnCells = new ArrayList();

      for(int i = 0; i < this.cells.size(); ++i) {
         TableCell cell = (TableCell)this.cells.get(i);
         if(cell.getXSpan() > 1) {
            multiColumnCells.add(cell);
         }
      }

      return multiColumnCells;
   }

   public TableBlock getBlock() {
      return this.block;
   }

   public void setBlock(TableBlock block) {
      this.block = block;
   }

   public List getCells() {
      return this.cells;
   }

   public void setCells(List cells) {
      this.cells = cells;
   }
}
