package vivyclient.gui.customers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import vivyclient.data.TransactionContainer;
import vivyclient.gui.common.EditPanel;
import vivyclient.gui.common.PanelUtil;
import vivyclient.model.Address;
import vivyclient.model.BaseModel;
import vivyclient.model.Customer;
import vivyclient.model.CustomerType;
import vivyclient.shared.Constants;
import vivyclient.util.ViewUtil;

public class CustomerPanel extends JPanel implements EditPanel {
   private Customer customer;
   private boolean isNew;
   private JLabel lOpeningBalance;
   private JTextField tFax;
   private JLabel lRestrictedGroup;
   private JPanel addressPanel;
   private JTextField tCellphone;
   private JLabel lPhoneTwo;
   private JComboBox cDefaultDelivery;
   private JLabel lDefaultDelivery;
   private JTextField tOrganisationName;
   private JLabel lCustTitle;
   private JPanel namePanel;
   private JLabel lSurname;
   private JPanel detailsPanel;
   private JLabel lCreditLimit;
   private JLabel lFax;
   private JLabel lOpeningBalanceDueDate;
   private JTextField tCreditLimit;
   private JPanel jPanel1;
   private JLabel lOrganisationName;
   private ButtonGroup customerType;
   private JLabel lFirstName;
   private JLabel lCustomerSince;
   private JLabel lCellphone;
   private JTextField tPassword;
   private JTextField tOpeningBalance;
   private JTextField tLoginEmail;
   private JTextField tEmailTwo;
   private JTextField tCustomerSince;
   private JPanel contactsPanel;
   private JLabel lDefaultBilling;
   private JTextField tOpeningBalanceDueDate;
   private JLabel lPhone;
   private JPanel balancesPanel;
   private JPanel filler;
   private JTextField tSurname;
   private JCheckBox cRestrictedGroup;
   private JComboBox cDefaultBilling;
   private JTextField tPhone;
   private JComboBox cCustomerType;
   private JTextField tPhoneTwo;
   private JTextField tObjectId;
   private JLabel lObjectId;
   private JLabel lEmailTwo;
   private JLabel lType;
   private JSeparator jSeparator1;
   private JLabel lLogin;
   private JLabel lPassword;
   private JTextField tFirstName;
   private JLabel lTitle;
   private JTextField tTitle;

   public CustomerPanel(Customer customer) throws Exception {
      this.customer = customer;
      this.initComponents();
      this.initialiseLists();
      this.setValues();
   }

   private void setValues() throws Exception {
      this.isNew = !this.customer.exists((TransactionContainer)null);
      if(this.isNew) {
         PanelUtil.setComboSelection(this.cCustomerType, CustomerType.PERSON_CUSTOMER_TYPE);
         this.tCustomerSince.setText(ViewUtil.calendarDisplay(Calendar.getInstance()));
         this.tObjectId.setEditable(true);
         this.tObjectId.setText("");
         this.tCreditLimit.setText(ViewUtil.currencyDisplay(Constants.ZERO_BIG_DECIMAL));
         this.tOpeningBalance.setText(ViewUtil.currencyDisplay(Constants.ZERO_BIG_DECIMAL));
      } else {
         PanelUtil.setComboSelection(this.cCustomerType, this.customer.getCustomerType());
         this.tCustomerSince.setText(ViewUtil.calendarDisplay(this.customer.getCustomerSince()));
         this.tObjectId.setText(String.valueOf(this.customer.getObjectId()));
         this.tObjectId.setEditable(false);
         if(this.customer.getCreditLimit().equals(Constants.NO_CREDIT_LIMIT_AMOUNT)) {
            this.tCreditLimit.setText(ViewUtil.currencyDisplay(Constants.ZERO_BIG_DECIMAL));
         } else {
            this.tCreditLimit.setText(ViewUtil.currencyDisplay(this.customer.getCreditLimit()));
         }

         this.tOpeningBalance.setText(ViewUtil.currencyDisplay(this.customer.getOpeningBalance()));
      }

      this.cRestrictedGroup.setSelected(this.customer.getRestrictedGroup());
      this.tTitle.setText(this.customer.getTitle());
      this.tFirstName.setText(this.customer.getFirstName());
      this.tSurname.setText(this.customer.getSurname());
      this.tOrganisationName.setText(this.customer.getOrganisationName());
      this.tLoginEmail.setText(this.customer.getLoginEmail());
      this.tEmailTwo.setText(this.customer.getEmail2());
      this.tPassword.setText(this.customer.getPassword());
      this.tPhone.setText(this.customer.getPhone());
      this.tPhoneTwo.setText(this.customer.getPhone2());
      this.tCellphone.setText(this.customer.getCellphone());
      this.tFax.setText(this.customer.getFax());
      PanelUtil.setComboSelection(this.cDefaultBilling, this.customer.getDefaultBillingAddress());
      PanelUtil.setComboSelection(this.cDefaultDelivery, this.customer.getDefaultDeliveryAddress());
      this.tOpeningBalanceDueDate.setText(ViewUtil.calendarDisplay(this.customer.getOpeningBalanceDueDate()));
   }

   private void initialiseLists() throws Exception {
      this.cCustomerType.setModel(new DefaultComboBoxModel(CustomerType.cachedFindAll(new CustomerType()).toArray()));
      if(this.customer.exists((TransactionContainer)null)) {
         Address[] addresses = new Address[this.customer.getAddressLinkCount() + 1];

         for(int i = 1; i < addresses.length; ++i) {
            addresses[i] = this.customer.getAddressLink(i - 1).getAddress();
         }

         this.cDefaultBilling.setModel(new DefaultComboBoxModel(addresses));
         this.cDefaultDelivery.setModel(new DefaultComboBoxModel(addresses));
      }

   }

   public boolean save() throws Exception {
      this.readInput();
      if(this.isNew) {
         if(this.customer.getCustomerGroup() != null) {
            this.customer.getCustomerGroup().addChild(this.customer);
            this.customer.getCustomerGroup().save((TransactionContainer)null);
         } else {
            this.customer.save((TransactionContainer)null);
         }

         this.setValues();
         return true;
      } else {
         if(this.customer.getCustomerGroup() != null) {
            this.customer.getCustomerGroup().save((TransactionContainer)null);
         } else {
            this.customer.save((TransactionContainer)null);
         }

         this.setValues();
         return false;
      }
   }

   public void readInput() throws Exception {
      if(this.isNew) {
         PanelUtil.validatedObjectIdRead(this.customer, this.tObjectId);
      }

      this.customer.setCustomerType((CustomerType)PanelUtil.getValidatedComboItem(this.cCustomerType, "Customer Type", true));
      this.customer.setCustomerSince(PanelUtil.getValidatedCalendarRead(this.tCustomerSince, "Customer Since", true));
      this.customer.setRestrictedGroup(this.cRestrictedGroup.isSelected());
      if(this.customer.getCustomerType().equals(CustomerType.PERSON_CUSTOMER_TYPE)) {
         this.customer.setSurname(PanelUtil.getValidatedStringValue(this.tSurname, "Surname", true));
         this.customer.setTitle(PanelUtil.getValidatedStringValue(this.tTitle, "Title", false));
         this.customer.setFirstName(PanelUtil.getValidatedStringValue(this.tFirstName, "First Name", false));
         this.customer.setOrganisationName((String)null);
      } else {
         this.customer.setOrganisationName(PanelUtil.getValidatedStringValue(this.tOrganisationName, "Organisation Name", true));
         this.customer.setSurname((String)null);
         this.customer.setTitle((String)null);
         this.customer.setFirstName((String)null);
      }

      this.customer.setLoginEmail(this.tLoginEmail.getText());
      this.customer.setEmail2(this.tEmailTwo.getText());
      this.customer.setPassword(this.tPassword.getText());
      this.customer.setPhone(this.tPhone.getText());
      this.customer.setPhone2(this.tPhoneTwo.getText());
      this.customer.setCellphone(this.tCellphone.getText());
      this.customer.setFax(this.tFax.getText());
      this.customer.setDefaultBillingAddress((Address)PanelUtil.getValidatedComboItem(this.cDefaultBilling, "Default Billing Address", false));
      this.customer.setDefaultDeliveryAddress((Address)PanelUtil.getValidatedComboItem(this.cDefaultDelivery, "Default Delivery Address", false));
      this.customer.setCreditLimit(PanelUtil.getValidatedCurrencyValue(this.tCreditLimit, "Credit Limit", true, 1));
      this.customer.setOpeningBalance(PanelUtil.getValidatedCurrencyValue(this.tOpeningBalance, "Opening Balance", true, 0));
      this.customer.setOpeningBalanceDueDate(PanelUtil.getValidatedCalendarRead(this.tOpeningBalanceDueDate, "Opening Balance Due Date", false));
   }

   private void initComponents() {
      this.customerType = new ButtonGroup();
      this.lTitle = new JLabel();
      this.jSeparator1 = new JSeparator();
      this.balancesPanel = new JPanel();
      this.lOpeningBalance = new JLabel();
      this.tOpeningBalance = new JTextField();
      this.lCreditLimit = new JLabel();
      this.tCreditLimit = new JTextField();
      this.lOpeningBalanceDueDate = new JLabel();
      this.tOpeningBalanceDueDate = new JTextField();
      this.contactsPanel = new JPanel();
      this.lPhone = new JLabel();
      this.tPhone = new JTextField();
      this.lPhoneTwo = new JLabel();
      this.tPhoneTwo = new JTextField();
      this.lCellphone = new JLabel();
      this.tCellphone = new JTextField();
      this.lFax = new JLabel();
      this.tFax = new JTextField();
      this.jPanel1 = new JPanel();
      this.lLogin = new JLabel();
      this.tLoginEmail = new JTextField();
      this.lPassword = new JLabel();
      this.tPassword = new JTextField();
      this.tEmailTwo = new JTextField();
      this.lEmailTwo = new JLabel();
      this.filler = new JPanel();
      this.namePanel = new JPanel();
      this.lFirstName = new JLabel();
      this.tFirstName = new JTextField();
      this.lSurname = new JLabel();
      this.tSurname = new JTextField();
      this.lOrganisationName = new JLabel();
      this.tOrganisationName = new JTextField();
      this.lCustTitle = new JLabel();
      this.tTitle = new JTextField();
      this.detailsPanel = new JPanel();
      this.lObjectId = new JLabel();
      this.tObjectId = new JTextField();
      this.lCustomerSince = new JLabel();
      this.tCustomerSince = new JTextField();
      this.cCustomerType = new JComboBox();
      this.lType = new JLabel();
      this.lRestrictedGroup = new JLabel();
      this.cRestrictedGroup = new JCheckBox();
      this.addressPanel = new JPanel();
      this.lDefaultBilling = new JLabel();
      this.cDefaultBilling = new JComboBox();
      this.lDefaultDelivery = new JLabel();
      this.cDefaultDelivery = new JComboBox();
      this.setLayout(new GridBagLayout());
      this.setBorder(new EmptyBorder(new Insets(5, 5, 5, 5)));
      this.lTitle.setFont(new Font("Arial", 1, 14));
      this.lTitle.setForeground(SystemColor.activeCaption);
      this.lTitle.setText("Customer");
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
      this.balancesPanel.setLayout(new GridBagLayout());
      this.balancesPanel.setBorder(new TitledBorder("Credit"));
      this.lOpeningBalance.setText("Opening Balance:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.balancesPanel.add(this.lOpeningBalance, gridBagConstraints);
      this.tOpeningBalance.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.balancesPanel.add(this.tOpeningBalance, gridBagConstraints);
      this.lCreditLimit.setText("Credit Limit:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 2);
      this.balancesPanel.add(this.lCreditLimit, gridBagConstraints);
      this.tCreditLimit.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.balancesPanel.add(this.tCreditLimit, gridBagConstraints);
      this.lOpeningBalanceDueDate.setText("Opening Balance Due Date:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.insets = new Insets(2, 0, 2, 2);
      gridBagConstraints.anchor = 13;
      this.balancesPanel.add(this.lOpeningBalanceDueDate, gridBagConstraints);
      this.tOpeningBalanceDueDate.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(2, 0, 2, 10);
      gridBagConstraints.weightx = 1.0D;
      this.balancesPanel.add(this.tOpeningBalanceDueDate, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 7;
      gridBagConstraints.fill = 2;
      gridBagConstraints.anchor = 11;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.weighty = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.add(this.balancesPanel, gridBagConstraints);
      this.contactsPanel.setLayout(new GridBagLayout());
      this.contactsPanel.setBorder(new TitledBorder("Telephony"));
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
      this.lPhoneTwo.setText("Phone Two:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.contactsPanel.add(this.lPhoneTwo, gridBagConstraints);
      this.tPhoneTwo.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.contactsPanel.add(this.tPhoneTwo, gridBagConstraints);
      this.lCellphone.setText("Cellphone:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.contactsPanel.add(this.lCellphone, gridBagConstraints);
      this.tCellphone.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.contactsPanel.add(this.tCellphone, gridBagConstraints);
      this.lFax.setText("Fax:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.contactsPanel.add(this.lFax, gridBagConstraints);
      this.tFax.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.contactsPanel.add(this.tFax, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 5;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.add(this.contactsPanel, gridBagConstraints);
      this.jPanel1.setLayout(new GridBagLayout());
      this.jPanel1.setBorder(new TitledBorder("Online"));
      this.lLogin.setText("Login/Email:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.jPanel1.add(this.lLogin, gridBagConstraints);
      this.tLoginEmail.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.tLoginEmail, gridBagConstraints);
      this.lPassword.setText("Password:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.jPanel1.add(this.lPassword, gridBagConstraints);
      this.tPassword.setText("jTextField2");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.anchor = 17;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.jPanel1.add(this.tPassword, gridBagConstraints);
      this.tEmailTwo.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.jPanel1.add(this.tEmailTwo, gridBagConstraints);
      this.lEmailTwo.setText("Email Two:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.jPanel1.add(this.lEmailTwo, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.jPanel1.add(this.filler, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 4;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.add(this.jPanel1, gridBagConstraints);
      this.namePanel.setLayout(new GridBagLayout());
      this.namePanel.setBorder(new TitledBorder("Name"));
      this.lFirstName.setText("First:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.namePanel.add(this.lFirstName, gridBagConstraints);
      this.tFirstName.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.namePanel.add(this.tFirstName, gridBagConstraints);
      this.lSurname.setText("Surname:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 4;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.namePanel.add(this.lSurname, gridBagConstraints);
      this.tSurname.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 5;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.namePanel.add(this.tSurname, gridBagConstraints);
      this.lOrganisationName.setText("Organisation:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.namePanel.add(this.lOrganisationName, gridBagConstraints);
      this.tOrganisationName.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.gridwidth = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.namePanel.add(this.tOrganisationName, gridBagConstraints);
      this.lCustTitle.setText("Title:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.namePanel.add(this.lCustTitle, gridBagConstraints);
      this.tTitle.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 0.2D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.namePanel.add(this.tTitle, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 3;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.add(this.namePanel, gridBagConstraints);
      this.detailsPanel.setLayout(new GridBagLayout());
      this.detailsPanel.setBorder(new TitledBorder("Customer"));
      this.lObjectId.setText("Id:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.detailsPanel.add(this.lObjectId, gridBagConstraints);
      this.tObjectId.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(2, 0, 0, 10);
      this.detailsPanel.add(this.tObjectId, gridBagConstraints);
      this.lCustomerSince.setText("Customer Since:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.detailsPanel.add(this.lCustomerSince, gridBagConstraints);
      this.tCustomerSince.setText("jTextField1");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.detailsPanel.add(this.tCustomerSince, gridBagConstraints);
      this.cCustomerType.setMinimumSize(new Dimension(31, 20));
      this.cCustomerType.setPreferredSize(new Dimension(31, 20));
      this.cCustomerType.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            CustomerPanel.this.customerTypeChanged(evt);
         }
      });
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      gridBagConstraints.insets = new Insets(0, 0, 0, 10);
      this.detailsPanel.add(this.cCustomerType, gridBagConstraints);
      this.lType.setText("Type:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.detailsPanel.add(this.lType, gridBagConstraints);
      this.lRestrictedGroup.setText("Restricted Group:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(2, 0, 0, 5);
      this.detailsPanel.add(this.lRestrictedGroup, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 3;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.anchor = 18;
      gridBagConstraints.insets = new Insets(2, 0, 0, 0);
      this.detailsPanel.add(this.cRestrictedGroup, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 2;
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.add(this.detailsPanel, gridBagConstraints);
      this.addressPanel.setLayout(new GridBagLayout());
      this.addressPanel.setBorder(new TitledBorder("Addresses"));
      this.lDefaultBilling.setText("Default Billing:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 0, 0, 5);
      this.addressPanel.add(this.lDefaultBilling, gridBagConstraints);
      this.cDefaultBilling.setMinimumSize(new Dimension(31, 20));
      this.cDefaultBilling.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.addressPanel.add(this.cDefaultBilling, gridBagConstraints);
      this.lDefaultDelivery.setText("Default Delivery:");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.anchor = 13;
      gridBagConstraints.insets = new Insets(0, 10, 0, 5);
      this.addressPanel.add(this.lDefaultDelivery, gridBagConstraints);
      this.cDefaultDelivery.setMinimumSize(new Dimension(31, 20));
      this.cDefaultDelivery.setPreferredSize(new Dimension(31, 20));
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.weightx = 1.0D;
      this.addressPanel.add(this.cDefaultDelivery, gridBagConstraints);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 6;
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(5, 0, 0, 0);
      this.add(this.addressPanel, gridBagConstraints);
   }

   private void customerTypeChanged(ActionEvent evt) {
      if(CustomerType.PERSON_CUSTOMER_TYPE.equals(this.cCustomerType.getSelectedItem())) {
         this.tOrganisationName.setText("");
         this.tOrganisationName.setEditable(false);
         this.tFirstName.setEditable(true);
         this.tSurname.setEditable(true);
      } else {
         this.tFirstName.setText("");
         this.tFirstName.setEditable(false);
         this.tSurname.setText("");
         this.tSurname.setEditable(false);
         this.tOrganisationName.setEditable(true);
      }

   }

   public Customer getCustomer() {
      return this.customer;
   }

   public void setCustomer(Customer customer) {
      this.customer = customer;
   }

   public boolean exit() throws Exception {
      return false;
   }

   public void refresh() throws Exception {
   }

   public BaseModel getModel() {
      return this.customer;
   }
}
