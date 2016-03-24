package vivyclient.gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JSplitPane;
import vivyclient.exception.StaleModelException;
import vivyclient.util.DialogueUtil;

public class SplitPaneBugListener implements ComponentListener {
   private JSplitPane splitPane;

   public SplitPaneBugListener(JSplitPane splitPane) {
      this.splitPane = splitPane;
   }

   public void componentResized(ComponentEvent componentEvent) {
      System.out.println("resized");
   }

   public void componentMoved(ComponentEvent componentEvent) {
      System.out.println("moved");
   }

   public void componentShown(ComponentEvent componentEvent) {
      System.out.println("shown");
      int location = this.splitPane.getDividerLocation();
      this.splitPane.setDividerLocation(0.5D);
      this.splitPane.revalidate();
      this.splitPane.repaint();
      DialogueUtil.getActionForStaleModelException(new StaleModelException("Idiot", "yo!"), this.splitPane);
      this.splitPane.setDividerLocation(location);
   }

   public void componentHidden(ComponentEvent componentEvent) {
      System.out.println("hidden");
   }
}
