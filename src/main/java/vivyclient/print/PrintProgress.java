package vivyclient.print;

public class PrintProgress {
   private int totalParts = -1;
   private int partsCompleted = 0;
   private int partsCompleteable = 0;

   public boolean uninitialised() {
      return this.totalParts < 0;
   }

   public int getTotalParts() {
      return this.totalParts;
   }

   public void setTotalParts(int totalParts) {
      this.totalParts = totalParts;
   }

   public int getPartsCompleted() {
      return this.partsCompleted;
   }

   public void setPartsCompleted(int partsCompleted) {
      this.partsCompleted = partsCompleted;
      this.partsCompleteable = partsCompleted;
   }

   public int getPartsCompleteable() {
      return this.partsCompleteable;
   }

   public void setPartsCompleteable(int partsCompleteable) {
      this.partsCompleteable = partsCompleteable;
   }
}
