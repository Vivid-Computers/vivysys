package vivyclient.gui.common;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import vivyclient.data.TransactionContainer;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.common.PanelUtil;
import vivyclient.model.Address;
import vivyclient.model.AddressType;
import vivyclient.model.BaseModel;
import vivyclient.model.Region;

public class AddressPanel extends JPanel implements EditPanel {
   private boolean isNew;
   private JTextField tCity;
   private JComboBox cRegion;
   private JLabel lCity;
   private JTextField tDeliveryName;
   private JLabel lLine2;
   private JLabel lLine1;
   private JLabel lDeliveryName;
   private JComboBox cAddressType;
   private JTextField tLine2;
   private JTextField tLine1;
   private JCheckBox jCheckBox1;
   private JLabel lCountry;
   private JTextField tCountry;
   private JLabel lAddressType;
   private JLabel lRegion;
   private Address address;

   public AddressPanel(Address address) throws Exception {
      this.address = address;
      this.initComponents();
      this.initialiseLists();
      this.refresh();
   }

   public void refresh() throws Exception {
      this.isNew = !this.address.exists((TransactionContainer)null);
      if(this.isNew) {
         PanelUtil.setComboSelection(this.cAddressType, AddressType.STREET_ADDRESS_TYPE);
         this.tDeliveryName.setText(this.address.getDeliveryName());
         this.tLine1.setText("");
         this.tLine2.setText("");
         this.tCity.setText("Auckland");
         this.tCountry.setText("New Zealand");
         PanelUtil.setComboSelection(this.cRegion, Region.AUCKLAND_REGION);
      } else {
         PanelUtil.setComboSelection(this.cAddressType, this.address.getAddressType());
         this.tDeliveryName.setText(this.address.getDeliveryName());
         this.tLine1.setText(this.address.getAddressLine1());
         this.tLine2.setText(this.address.getAddressLine2());
         this.tCity.setText(this.address.getCity());
         this.tCountry.setText(this.address.getCountry());
         PanelUtil.setComboSelection(this.cRegion, this.address.getRegion());
      }

   }

   private void initialiseLists() throws Exception {
      this.cAddressType.setModel(new DefaultComboBoxModel(AddressType.cachedFindAll(new AddressType()).toArray()));
      this.cRegion.setModel(new DefaultComboBoxModel(Region.cachedFindAll(new Region()).toArray()));
   }

   public void readUserValues() throws Exception {
      this.address.setAddressType((AddressType)this.cAddressType.getSelectedItem());
      this.address.setDeliveryName(this.tDeliveryName.getText());
      this.address.setAddressLine1(this.tLine1.getText());
      this.address.setAddressLine2(this.tLine2.getText());
      this.address.setCity(this.tCity.getText());
      this.address.setCountry(this.tCountry.getText());
      this.address.setRegion((Region)this.cRegion.getSelectedItem());
   }

   public boolean exit() throws Exception {
      return false;
   }

   public boolean save() throws Exception {
      return false;
   }

   private void initComponents() {
      this.lAddressType = new JLabel();
      this.cAddressType = new JComboBox();
      this.lDeliveryName = new JLabel();
      this.tDeliveryName = new JTextField();
      this.lLine1 = new JLabel();
      this.tLine1 = new JTextField();
      this.lLine2 = new JLabel();
      this.tLine2 = new JTextField();
      this.lCity = new JLabel();
      this.tCity = new JTextField();
      this.lCountry = new JLabel();
      this.tCountry = new JTextField();
      this.lRegion = new JLabel();
      this.cRegion = new JComboBox();
      this.jCheckBox1 = new JCheckBox();
      this.setLayout(new GridBagLayout());
      this.lAddressType.setText("AddressType:");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      this.add(this.lAddressType, gridBagConstraints);
      this.cAddressType.setMinimumSize(new Dimension(31, 20));
      this.cAddressType.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.cAddressType, gridBagConstraints);
      this.lDeliveryName.setText("DeliveryName:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      this.add(this.lDeliveryName, gridBagConstraints);
      this.tDeliveryName.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tDeliveryName, gridBagConstraints);
      this.lLine1.setText("Line 1:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      this.add(this.lLine1, gridBagConstraints);
      this.tLine1.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tLine1, gridBagConstraints);
      this.lLine2.setText("Line 2:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      this.add(this.lLine2, gridBagConstraints);
      this.tLine2.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tLine2, gridBagConstraints);
      this.lCity.setText("City:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      this.add(this.lCity, gridBagConstraints);
      this.tCity.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tCity, gridBagConstraints);
      this.lCountry.setText("Country:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      gridBagConstraints.anchor = 12;
      this.add(this.lCountry, gridBagConstraints);
      this.tCountry.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.tCountry, gridBagConstraints);
      this.lRegion.setText("Region:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.insets = new Insets(2, 10, 0, 5);
      gridBagConstraints.anchor = 12;
      this.add(this.lRegion, gridBagConstraints);
      this.cRegion.setMinimumSize(new Dimension(31, 20));
      this.cRegion.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      gridBagConstraints.weightx = 1.0D;
      this.add(this.cRegion, gridBagConstraints);
      this.jCheckBox1.setText("Old Address");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.insets = new Insets(0, 10, 0, 0);
      gridBagConstraints.anchor = 18;
      this.add(this.jCheckBox1, gridBagConstraints);
   }

   public Address getAddress() {
      return this.address;
   }

   public void setAddress(Address address) {
      this.address = address;
   }

   public BaseModel getModel() {
      return this.address;
   }
}
