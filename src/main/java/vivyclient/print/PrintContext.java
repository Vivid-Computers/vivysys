package vivyclient.print;

import java.awt.Graphics2D;

public class PrintContext {
   private Graphics2D graphics;
   private double startY;
   private double maxHeight;
   private double startX;
   private double width;
   private double currentY;
   private int pageCount;

   public PrintContext(Graphics2D graphics, double startX, double startY, double width, double maxHeight) {
      this.graphics = graphics;
      this.startX = startX;
      this.startY = startY;
      this.width = width;
      this.maxHeight = maxHeight;
      this.currentY = startY;
   }

   public PrintContext createClone() {
      PrintContext newContext = new PrintContext(this.graphics, this.startX, this.startY, this.width, this.maxHeight);
      newContext.setCurrentY(this.getCurrentY());
      newContext.setPageCount(this.getPageCount());
      return newContext;
   }

   public Graphics2D getGraphics() {
      return this.graphics;
   }

   public void setGraphics(Graphics2D graphics) {
      this.graphics = graphics;
   }

   public double getStartY() {
      return this.startY;
   }

   public void setStartY(double startY) {
      this.startY = startY;
   }

   public double getMaxHeight() {
      return this.maxHeight;
   }

   public void setMaxHeight(double maxHeight) {
      this.maxHeight = maxHeight;
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

   public double getCurrentY() {
      return this.currentY;
   }

   public void setCurrentY(double currentY) {
      this.currentY = currentY;
   }

   public int getPageCount() {
      return this.pageCount;
   }

   public void setPageCount(int pageCount) {
      this.pageCount = pageCount;
   }
}
