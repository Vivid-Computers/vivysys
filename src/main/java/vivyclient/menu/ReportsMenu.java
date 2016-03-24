package vivyclient.menu;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import vivyclient.Client;
import vivyclient.gui.reports.GSTFrame;
import vivyclient.gui.reports.PrintCustomerBalancesFrame;

public class ReportsMenu extends JMenu {
   public ReportsMenu() {
      super("Reports");
      this.initialise();
   }

   private void initialise() {
      JMenuItem customerBalances = new JMenuItem("Customer Balances");
      customerBalances.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ReportsMenu.this.viewCustomerBalancesReport();
         }
      });
      this.add(customerBalances);
      JMenuItem gstTotals = new JMenuItem("GST Totals");
      gstTotals.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            ReportsMenu.this.viewGSTTotalsReport();
         }
      });
      this.add(gstTotals);
   }

   private void viewCustomerBalancesReport() {
      try {
         Client.getMainFrame().setCursor(Cursor.getPredefinedCursor(3));
         PrintCustomerBalancesFrame frame = new PrintCustomerBalancesFrame();
         Client.getMainFrame();
         Client.addInternalFrame(frame);
      } finally {
         Client.getMainFrame().setCursor(Cursor.getPredefinedCursor(0));
      }

   }

   private void viewGSTTotalsReport() {
      try {
         Client.getMainFrame().setCursor(Cursor.getPredefinedCursor(3));
         GSTFrame frame = new GSTFrame();
         Client.getMainFrame();
         Client.addInternalFrame(frame);
      } finally {
         Client.getMainFrame().setCursor(Cursor.getPredefinedCursor(0));
      }

   }
}
