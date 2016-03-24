package vivyclient.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class PopupListener extends MouseAdapter {
   public void mousePressed(MouseEvent e) {
      this.maybeShowPopup(e);
   }

   public void mouseReleased(MouseEvent e) {
      this.maybeShowPopup(e);
   }

   private void maybeShowPopup(MouseEvent e) {
      if(e.isPopupTrigger()) {
         this.popupRequested(e);
      }

   }

   public abstract void popupRequested(MouseEvent var1);
}
