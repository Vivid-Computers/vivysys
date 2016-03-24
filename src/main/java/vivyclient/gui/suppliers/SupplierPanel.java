package vivyclient.gui.suppliers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import vivyclient.data.TransactionContainer;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.common.PanelUtil;
import vivyclient.model.BaseModel;
import vivyclient.model.Supplier;
import vivyclient.model.SupplierType;

public class SupplierPanel extends JPanel implements EditPanel {
   private Supplier supplier;
   boolean isNew;
   private JTextField tFax;
   private JPanel contactsPanel;
   private JLabel lPhone;
   private JTextField tTrackingURL;
   private JPanel filler;
   private JTextField tPhone;
   private JTextField tObjectId;
   private JTextField tURL;
   private JLabel lName;
   private JLabel lObjectId;
   private JPanel detailsPanel;
   private JTextField tName;
   private JTextField tEmail;
   private JLabel lFax;
   private JLabel lType;
   private JLabel lTrackingURL;
   private JLabel lEmail;
   private JSeparator jSeparator1;
   private JPanel jPanel1;
   private JLabel lTitle;
   private JLabel lURL;
   private JComboBox cSupplierType;

   public SupplierPanel(Supplier supplier) throws Exception {
      this.supplier = supplier;
      this.initComponents();
      this.initialiseLists();
      this.refresh();
   }

   private void initialiseLists() throws Exception {
      PanelUtil.populateComboList(this.cSupplierType, new SupplierType(), false);
   }

   public void refresh() throws Exception {
      this.isNew = !this.supplier.exists((TransactionContainer)null);
      if(this.isNew) {
         this.tObjectId.setText("");
      } else {
         this.tObjectId.setText(String.valueOf(this.supplier.getObjectId()));
         this.tObjectId.setEditable(false);
      }

      PanelUtil.setComboSelection(this.cSupplierType, this.supplier.getSupplierType());
      this.tName.setText(this.supplier.getName());
      this.tPhone.setText(this.supplier.getPhone());
      this.tFax.setText(this.supplier.getFax());
      this.tEmail.setText(this.supplier.getEmail());
      this.tURL.setText(this.supplier.getUrl());
      this.tTrackingURL.setText(this.supplier.getTrackingUrl());
   }

   public boolean save() throws Exception {
      boolean wasNew = this.isNew;
      if(this.isNew) {
         PanelUtil.validatedObjectIdRead(this.supplier, this.tObjectId);
      }

      this.supplier.setSupplierType((SupplierType)PanelUtil.getValidatedComboItem(this.cSupplierType, "Supplier Type", true));
      this.supplier.setName(PanelUtil.getValidatedStringValue(this.tName, "Name", true));
      this.supplier.setPhone(PanelUtil.getValidatedStringValue(this.tPhone, "Phone", false));
      this.supplier.setFax(PanelUtil.getValidatedStringValue(this.tFax, "Fax", false));
      this.supplier.setEmail(PanelUtil.getValidatedStringValue(this.tEmail, "Email", false));
      this.supplier.setUrl(PanelUtil.getValidatedStringValue(this.tURL, "URL", false));
      this.supplier.setTrackingUrl(PanelUtil.getValidatedStringValue(this.tTrackingURL, "Tracking URL", false));
      this.supplier.save((TransactionContainer)null);
      this.refresh();
      return wasNew;
   }

   public boolean exit() throws Exception {
      return true;
   }

   public BaseModel getModel() {
      return this.supplier;
   }

   private void initComponents() {
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.detailsPanel = new JPanel();
      this.lObjectId = new JLabel();
      this.tObjectId = new JTextField();
      this.lName = new JLabel();
      this.tName = new JTextField();
      this.cSupplierType = new JComboBox();
      this.lType = new JLabel();
      this.contactsPanel = new JPanel();
      this.lPhone = new JLabel();
      this.tPhone = new JTextField();
      this.lEmail = new JLabel();
      this.tEmail = new JTextField();
      this.lFax = new JLabel();
      this.tFax = new JTextField();
      this.jPanel1 = new JPanel();
      this.lURL = new JLabel();
      this.tURL = new JTextField();
      this.tTrackingURL = new JTextField();
      this.lTrackingURL = new JLabel();
      this.filler = new JPanel();
      this.setLayout(new GridBagLayout());
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Supplier");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.anchor = 18;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 5, 3, 5);
      this.add(this.lTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 3;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 8, 0);
      this.add(this.jSeparator1, gridBagConstraints);
      this.detailsPanel.setLayout(new GridBagLayout());
      this.detailsPanel.setBorder(new TitledBorder("Supplier"));
      this.lObjectId.setText("Id:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.detailsPanel.add(this.lObjectId, gridBagConstraints);
      this.tObjectId.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.detailsPanel.add(this.tObjectId, gridBagConstraints);
      this.lName.setText("Name:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.detailsPanel.add(this.lName, gridBagConstraints);
      this.tName.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.detailsPanel.add(this.tName, gridBagConstraints);
      this.cSupplierType.setMinimumSize(new Dimension(31, 20));
      this.cSupplierType.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.detailsPanel.add(this.cSupplierType, gridBagConstraints);
      this.lType.setText("Type:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.detailsPanel.add(this.lType, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.detailsPanel, gridBagConstraints);
      this.contactsPanel.setLayout(new GridBagLayout());
      this.contactsPanel.setBorder(new TitledBorder("Contact"));
      this.lPhone.setText("Phone:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.contactsPanel.add(this.lPhone, gridBagConstraints);
      this.tPhone.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.contactsPanel.add(this.tPhone, gridBagConstraints);
      this.lEmail.setText("Email:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.contactsPanel.add(this.lEmail, gridBagConstraints);
      this.tEmail.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.contactsPanel.add(this.tEmail, gridBagConstraints);
      this.lFax.setText("Fax:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.contactsPanel.add(this.lFax, gridBagConstraints);
      this.tFax.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.contactsPanel.add(this.tFax, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.add(this.contactsPanel, gridBagConstraints);
      this.jPanel1.setLayout(new GridBagLayout());
      this.jPanel1.setBorder(new TitledBorder("Online"));
      this.lURL.setText("URL:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.jPanel1.add(this.lURL, gridBagConstraints);
      this.tURL.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.tURL, gridBagConstraints);
      this.tTrackingURL.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.jPanel1.add(this.tTrackingURL, gridBagConstraints);
      this.lTrackingURL.setText("Tracking URL:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.jPanel1.add(this.lTrackingURL, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.filler, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.add(this.jPanel1, gridBagConstraints);
   }
}
