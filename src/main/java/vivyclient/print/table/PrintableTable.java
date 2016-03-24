package vivyclient.print.table;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import java.util.List;
import vivyclient.print.PrintProgress;
import vivyclient.print.table.TableBlock;
import vivyclient.print.table.TableCell;
import vivyclient.print.table.TableColumn;
import vivyclient.print.table.TableRegion;

public class PrintableTable extends TableRegion {
   private List columns;
   private double columnDivider;
   private double startX;
   private double width;
   private List blocks;
   private boolean initialised;

   public PrintableTable(double startX, double width, double columnDivider) {
      this.startX = startX;
      this.width = width;
      this.columnDivider = columnDivider;
      this.blocks = new ArrayList();
      this.columns = new ArrayList();
      this.initialised = false;
   }

   public void addColumn(TableColumn column) {
      column.setIndex(this.columns.size());
      this.columns.add(column);
   }

   public void addBlock(TableBlock block) {
      this.blocks.add(block);
   }

   public List getColumns() {
      return this.columns;
   }

   public void setColumns(List columns) {
      this.columns = columns;
   }

   public double getColumnDivider() {
      return this.columnDivider;
   }

   public void setColumnDivider(double columnDivider) {
      this.columnDivider = columnDivider;
   }

   public void initialise(Graphics2D g) {
      double consumedWidth = 0.0D;

      for(int diff = 0; diff < this.columns.size(); ++diff) {
         TableColumn column = (TableColumn)this.columns.get(diff);
         column.initialise(g);
         if(column.getFillMultiplier() > 0.0D) {
            consumedWidth += column.getTitleWidth();
         } else {
            consumedWidth += column.getPreferredWidth();
         }
      }

      consumedWidth += this.columnDivider * (double)(this.columns.size() - 1);
      double var8 = this.width - consumedWidth;

      for(int i = 0; i < this.columns.size(); ++i) {
         TableColumn column1 = (TableColumn)this.columns.get(i);
         if(column1.getFillMultiplier() > 0.0D) {
            column1.setWidth(column1.getTitleWidth() + column1.getFillMultiplier() * var8);
         } else {
            column1.setWidth(column1.getPreferredWidth());
         }
      }

      this.setInitialised(true);
   }

   public double getPreferredWidth(Graphics2D g) {
      double preferredWidth = 0.0D;

      for(int i = 0; i < this.columns.size(); ++i) {
         TableColumn column = (TableColumn)this.columns.get(i);
         column.initialise(g);
         preferredWidth += column.getPreferredWidth();
      }

      preferredWidth += this.columnDivider * (double)(this.columns.size() - 1);
      return preferredWidth;
   }

   public Rectangle2D print(Graphics2D g, PrintProgress printProgress, double startY, boolean hidden, double maxHeight) {
      double leftMost = this.width;
      double rightMost = 0.0D;
      double height = 0.0D;
      if(printProgress.uninitialised()) {
         printProgress.setTotalParts(this.blocks.size());
      }

      int partsCompleted = printProgress.getPartsCompleted();
      System.out.println("[PrintableTable] Print " + (hidden?"(hidden) ":"") + "table called, start block: " + partsCompleted + "  start y: " + startY + "  maxHeight: " + maxHeight);

      for(int i = partsCompleted; i < this.blocks.size(); ++i) {
         Rectangle2D blockSize = ((TableBlock)this.blocks.get(i)).print(g, startY + height, true);
         if(height + blockSize.getHeight() > maxHeight) {
            System.out.println("[PrintableTable] block " + i + " (0 indexed) will not fit.");
            break;
         }

         ++partsCompleted;
         if(!hidden) {
            ((TableBlock)this.blocks.get(i)).print(g, startY + height, false);
         }

         if(blockSize.getMinX() < leftMost) {
            leftMost = blockSize.getMinX();
         }

         if(blockSize.getMaxX() > rightMost) {
            rightMost = blockSize.getMaxX();
         }

         height += blockSize.getHeight();
      }

      System.out.println("[PrintableTable] " + partsCompleted + " blocks have been completed.");
      if(hidden) {
         printProgress.setPartsCompleteable(partsCompleted);
      } else {
         printProgress.setPartsCompleted(partsCompleted);
      }

      return new Double(leftMost, startY, rightMost - leftMost, height);
   }

   public Rectangle2D printColumnTitles(Graphics2D g, double startY, boolean hidden) {
      double height = 0.0D;

      for(int i = 0; i < this.columns.size(); ++i) {
         TableColumn column = (TableColumn)this.columns.get(i);
         height = this.max(height, column.printTitle(g, startY, hidden).getHeight());
      }

      return new Double(this.startX, startY, this.width, height);
   }

   public TableColumn getColumn(int index) {
      return (TableColumn)this.columns.get(index);
   }

   public double getSpanWidth(TableColumn column, int columnSpan) {
      if(columnSpan == 1) {
         return column.getWidth();
      } else {
         double width = 0.0D;

         for(int i = column.getIndex(); i < column.getIndex() + columnSpan; ++i) {
            width += this.getColumn(i).getWidth();
         }

         width += (double)(columnSpan - 1) * this.columnDivider;
         return width;
      }
   }

   public List getColumnsForCell(TableCell cell) {
      ArrayList columns = new ArrayList();

      for(int i = cell.getStartColumn().getIndex(); i < cell.getStartColumn().getIndex() + cell.getXSpan(); ++i) {
         columns.add(columns.get(i));
      }

      return columns;
   }

   public double getX(TableColumn column) {
      return column.getIndex() == 0?this.startX:this.startX + this.getSpanWidth(this.getColumn(0), column.getIndex()) + this.columnDivider;
   }

   public List getCellsForColumn(TableColumn column) {
      ArrayList columnCells = new ArrayList();

      for(int i = 0; i < this.blocks.size(); ++i) {
         columnCells.addAll(((TableBlock)this.blocks.get(i)).getCellsForColumn(column));
      }

      return columnCells;
   }

   public List getAllMultiColumnCells() {
      ArrayList cells = new ArrayList();

      for(int i = 0; i < this.blocks.size(); ++i) {
         cells.addAll(((TableBlock)this.blocks.get(i)).getAllMultiColumnCells());
      }

      return cells;
   }

   public double getStartX() {
      return this.startX;
   }

   public void setStartX(double startX) {
      this.startX = startX;
   }

   public double getWidth() {
      return this.width;
   }

   public void setWidth(double width) {
      this.width = width;
   }

   public List getBlocks() {
      return this.blocks;
   }

   public void setBlocks(List blocks) {
      this.blocks = blocks;
   }

   public boolean isInitialised() {
      return this.initialised;
   }

   public void setInitialised(boolean initialised) {
      this.initialised = initialised;
   }
}
