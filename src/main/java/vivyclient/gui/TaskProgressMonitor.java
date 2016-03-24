package vivyclient.gui;

import java.awt.Component;
import javax.swing.ProgressMonitor;
import vivyclient.gui.TaskMonitor;

public class TaskProgressMonitor implements TaskMonitor {
   private ProgressMonitor progressMonitor;
   private int progress;

   public TaskProgressMonitor(Component parentComponent, String description, int taskCount) {
      this.progressMonitor = new ProgressMonitor(parentComponent, description, "", 0, taskCount);
      this.progress = 0;
   }

   public void increment(String taskDescription) {
      this.progressMonitor.setNote(taskDescription);
      this.progressMonitor.setProgress(this.progress++);
   }

   public boolean isCancelled() {
      return this.progressMonitor.isCanceled();
   }

   public void close() {
      this.progressMonitor.close();
   }
}
