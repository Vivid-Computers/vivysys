package vivyclient.gui.reports;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import vivyclient.Client;
import vivyclient.data.TransactionContainer;
import vivyclient.exception.UserInputException;
import vivyclient.gui.common.PanelUtil;
import vivyclient.model.dao.GSTTotalsReportDao;
import vivyclient.print.reports.GSTTotals;
import vivyclient.util.DialogueUtil;
import vivyclient.util.Settings;
import vivyclient.util.ViewUtil;

public class GSTFrame extends JInternalFrame {
   private static final String NAME = "PrintGSTReportFrame";
   private JButton bPrintPreview;
   private JTextField tEndDate;
   private JButton bPrintPackingSlip;
   private JLabel lStartDate;
   private JLabel lEndDate;
   private JPanel viewPanel;
   private JSeparator jSeparator1;
   private JTextField tStartDate;
   private JLabel lTitle;

   public GSTFrame() {
      this.initComponents();
      this.refresh();
      this.setSize(new Dimension(Settings.getWidth("PrintGSTReportFrame"), Settings.getHeight("PrintGSTReportFrame")));
      this.setLocation(Settings.getXPos("PrintGSTReportFrame"), Settings.getYPos("PrintGSTReportFrame"));
   }

   private void refresh() {
      this.tStartDate.setText("");
      this.tEndDate.setText("");
   }

   private GSTTotals readInput() throws Exception {
      GSTTotals totals = new GSTTotals();
      totals.setPeriodStart(PanelUtil.getValidatedCalendarRead(this.tStartDate, "Start Date", true));
      totals.setPeriodEnd(PanelUtil.getValidatedCalendarRead(this.tEndDate, "End Date", true));
      return totals;
   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.viewPanel = new JPanel();
      this.lStartDate = new JLabel();
      this.tStartDate = new JTextField();
      this.lEndDate = new JLabel();
      this.tEndDate = new JTextField();
      this.bPrintPackingSlip = new JButton();
      this.bPrintPreview = new JButton();
      this.getContentPane().setLayout(new GridBagLayout());
      this.setClosable(true);
      this.setIconifiable(true);
      this.setMaximizable(true);
      this.setResizable(true);
      this.addInternalFrameListener(new InternalFrameListener() {
         public void internalFrameOpened(InternalFrameEvent evt) {
         }

         public void internalFrameClosing(InternalFrameEvent evt) {
            GSTFrame.this.formInternalFrameClosing(evt);
         }

         public void internalFrameClosed(InternalFrameEvent evt) {
         }

         public void internalFrameIconified(InternalFrameEvent evt) {
         }

         public void internalFrameDeiconified(InternalFrameEvent evt) {
         }

         public void internalFrameActivated(InternalFrameEvent evt) {
         }

         public void internalFrameDeactivated(InternalFrameEvent evt) {
         }
      });
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("GST Totals Report");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.insets = new Insets(0, 0, 8, 0);
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      this.getContentPane().add(this.jSeparator1, gridBagConstraints);
      this.viewPanel.setLayout(new GridBagLayout());
      this.viewPanel.setBorder(new TitledBorder("Period"));
      this.lStartDate.setText("Start Date:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      this.viewPanel.add(this.lStartDate, gridBagConstraints);
      this.tStartDate.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.viewPanel.add(this.tStartDate, gridBagConstraints);
      this.lEndDate.setText("End Date:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      this.viewPanel.add(this.lEndDate, gridBagConstraints);
      this.tEndDate.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.viewPanel.add(this.tEndDate, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      this.getContentPane().add(this.viewPanel, gridBagConstraints);
      this.bPrintPackingSlip.setText("Print");
      this.bPrintPackingSlip.setEnabled(false);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(15, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      this.getContentPane().add(this.bPrintPackingSlip, gridBagConstraints);
      this.bPrintPreview.setText("Print Preview");
      this.bPrintPreview.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            GSTFrame.this.bPrintPreviewActionPerformed(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(15, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      gridBagConstraints.weighty = 1.0D;
      this.getContentPane().add(this.bPrintPreview, gridBagConstraints);
      this.pack();
   }

   private void formInternalFrameClosing(InternalFrameEvent evt) {
      Settings.setWidth("PrintGSTReportFrame", (int)this.getSize().getWidth());
      Settings.setHeight("PrintGSTReportFrame", (int)this.getSize().getHeight());
      Settings.setXPos("PrintGSTReportFrame", this.getLocation().x);
      Settings.setYPos("PrintGSTReportFrame", this.getLocation().y);
      this.setVisible(false);
      this.dispose();
   }

   private void bPrintPreviewActionPerformed(ActionEvent evt) {
      try {
         this.setCursor(new Cursor(3));
         GSTTotals e = this.readInput();
         GSTTotalsReportDao.populateTotals(e, (TransactionContainer)null);
         this.showResultsDialogue(e);
      } catch (UserInputException var7) {
         DialogueUtil.handleUserInputException(var7, "", "Invalid Input", Client.getMainFrame());
      } catch (Exception var8) {
         DialogueUtil.handleException(var8, "Error calculating GST totals", "Report Error", true, Client.getMainFrame());
      } finally {
         this.setCursor(new Cursor(0));
      }

   }

   private void showResultsDialogue(GSTTotals totals) {
      String message = "<html><b>GST Totals Report</b><br />Showing results from " + ViewUtil.calendarDateTimeDisplay(totals.getPeriodStart()) + " to " + ViewUtil.calendarDateTimeDisplay(totals.getPeriodEnd()) + "." + "<br />Net Total GST Sales: " + ViewUtil.currencyDisplay(totals.getGstSaleTotals()) + "<br />Net Total GST-Exempt Sales: " + ViewUtil.currencyDisplay(totals.getGstExemptSaleTotals()) + "<br />Total GST Collected: " + ViewUtil.currencyDisplay(totals.getGstSaleTotals().multiply(new BigDecimal("0.150"))) + "</html>";
      JOptionPane.showMessageDialog(Client.getMainFrame(), message, this.title, 1);
   }
}
