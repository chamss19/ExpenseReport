import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Properties;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

//System.getProperty("user.name")
public class EntryForm extends JFrame {
	/*
	 * SQLS DOE
	 */
	sqlHandler sqls = new sqlHandler();

	/*
	 * LAYOUT SOME STUFF
	 */
	FlowLayout myLayOut = new FlowLayout(FlowLayout.LEFT);

	/*
	 * FORM EVENTS CLASS
	 */
	FormEvents frmEvents = new FormEvents();

	/*
	 * Border
	 */
	Border border = BorderFactory.createLoweredBevelBorder();

	/*
	 *  ALL THE DECLERATIONS!
	 */
	public JDatePanelImpl datePanel ;
	public JDatePickerImpl  datePicker; 
	public UtilDateModel dateModel = new UtilDateModel();
	
	private JPanel panelType, panelDate, panelSubType, panelComments, panelSave, panelAmount, panelTable, panelReviewLabels, panelReview;

	private JComboBox<String> ddType, ddSubType;
	private JTextField txtAmount;
	private JTextArea txtComments;
	private JButton btnAddRecord, btnDelete;
	private JTable dataTable;
	private JLabel lblType, lblSubType, lblComments, lblAmount, lblExpDate, lblEnterDate;
	
	public static Integer RECORD_ID;
	public static String BUSINESS_UNIT;
	
	String[] meals= {"Select One","Breakfast","Lunch","Dinner"};
	
	String[] typeList = {"Select One","Meals", "Hotels Incl Tax", "Mileage",
			"Entertainment", "Airfare", "Auto Rental", "Taxi",
			"Parking/Tolls/Tips", "Internet", "Cell Phone", "Telephone" };
	
	String[] lblText = {"Entry Type: ","Sub Type: ","Comments: ","Amounts: ","Expense Date: ", "Entered Date: "};

	
	String user = new String(System.getProperty("user.name"));
	String userId = new String("256054");
	

	public void loadRecord(int id, String bu) {
		this.RECORD_ID = id;
		this.BUSINESS_UNIT = bu;
		startForm();
	}

	private void startForm() {
		/*
		 * FORM LAYOUT STUFF
		 */
		setLayout(myLayOut);
		setSize(1000, 900);

		/*
		 * QRY AD TO GET USERS AB#
		 */
		setTitle("G3 Enterprise, Inc. Report2 Form " + " " + user + ": "+ userId + " Record Number: " + Integer.toString(RECORD_ID));
		setFont(new Font("Arial", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		/*
		 * Type dropdown i.e. meals, tickets, etc.
		 */
		ddType = new JComboBox<String>(typeList) {{
				setPreferredSize(new Dimension(100, 25));
				addActionListener(frmEvents);				
			}
		};
				
		/*
		 * Sub type i.e. breakfast, lunch, dinner
		 */
		ddSubType = new JComboBox<String>() {{
				setEnabled(false);
				setPreferredSize(new Dimension(100, 25));
				addActionListener(frmEvents);
			}
		};

		/*
		 * Properties for the datepicker
		 */
		Properties p = new Properties() {{			
				put("text.today", "Today");
				put("text.month", "Month");
				put("text.year", "Year");
			}
		};

		/*
		 * panel that the datepicker sits on
		 */
		datePanel = new JDatePanelImpl(dateModel, p);		
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter()) {{
				addActionListener(frmEvents);
				setEnabled(false);
			}
		};	
				
		/*
		 * comments
		 */
		txtComments = new JTextArea() {{
				setName("txtComments");
				setText("Enter Comments Here:");
				addFocusListener(frmEvents);
				setWrapStyleWord(true);
				setLineWrap(true);
			}		
		};
	
		/*
		 * enter amount, either dollar or an amount 
		 * to be converted
		 */
		txtAmount=new JTextField() {{
				setName("txtAmount"); 
				setEnabled(false);
				setText("0");				
			}
		};
	
		/*
		 * 	add line item button
		 */
		btnAddRecord = new JButton("Add Entry to Report") {{
				setPreferredSize(new Dimension(150, 25));
				setEnabled(false);
				addActionListener(frmEvents);
			}
		};
		
		/*
		 * Delete a line item button
		 */
		btnDelete = new JButton("Delete a record") {{
				setPreferredSize(new Dimension(150, 25));
				addActionListener(frmEvents);
			}
		};	
		
		/*
		 * Panel for type dropdown
		 */		
		panelType = new JPanel(new BorderLayout()) {{
				setBackground(Color.LIGHT_GRAY);
				setPreferredSize(new Dimension(315, 40));
				add(new JLabel("Entry Type: " ,JLabel.LEFT), BorderLayout.CENTER);
				add(ddType, BorderLayout.SOUTH);
			}
		};
		add(panelType);

		/*
		 * PANEL FOR SUB TYPE QUESTION
		 */
		panelSubType = new JPanel(new BorderLayout()) {{
				setBackground(Color.LIGHT_GRAY);
				setPreferredSize(new Dimension(315, 40));
				add(new JLabel("Descriptor " ,JLabel.LEFT), BorderLayout.CENTER);
				add(ddSubType, BorderLayout.SOUTH);					
			}
		};
		add(panelSubType);

		/*
		 * panel for date entry
		 */
		panelDate = new JPanel(new BorderLayout()) {{
				setPreferredSize(new Dimension(315, 40));
				setBackground(Color.LIGHT_GRAY);
				add(new JLabel("Entry Date: " ,JLabel.LEFT), BorderLayout.NORTH);
				add(datePicker, BorderLayout.SOUTH);
			}
		};
		add(panelDate);

		/*
		 * Panel for comments
		 */

		panelComments = new JPanel(new BorderLayout()) {{
				setPreferredSize(new Dimension(315, 40));
				add(txtComments);
			}
		};		
		add(panelComments);
		
		
		/*
		 * Panel for amount text box
		 */
		panelAmount = new JPanel(new BorderLayout()) {{
				setPreferredSize(new Dimension(315, 40));
				setBackground(Color.LIGHT_GRAY);
				add(new JLabel("Amount: " ,JLabel.LEFT), BorderLayout.CENTER);			
				add(txtAmount, BorderLayout.SOUTH);			
			}
		};
		add(panelAmount);

		/*
		 * Panel for add record button
		 */
		panelSave = new JPanel(new BorderLayout()){{
				setPreferredSize(new Dimension(315, 40));
				setBackground(Color.LIGHT_GRAY);
				add(new JLabel("Save Entry: " ,JLabel.LEFT), BorderLayout.NORTH);
				add(btnAddRecord, BorderLayout.SOUTH);
			}
		};
		add(panelSave);
		
		
		/*
		 * model for table
		 */
		DefaultTableModel mdl = new DefaultTableModel(null, new String[] {"", "Expense Type", "Description", "Amount", "Comments", "Expense Date", "Entered" }) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		/*
		 *  Set up table for data
		 */
		dataTable = new JTable(mdl){{
				setColumnSelectionAllowed(false);
				getColumnModel().getColumn(0).setMaxWidth(0);
				getColumnModel().getColumn(0).setMinWidth(0);
				getColumnModel().getColumn(0).setPreferredWidth(0);	//id
				getColumnModel().getColumn(1).setPreferredWidth(110);//type
				getColumnModel().getColumn(2).setPreferredWidth(100);//sub type				
				getColumnModel().getColumn(3).setPreferredWidth(80);//amount
				getColumnModel().getColumn(4).setPreferredWidth(245);//comments
				getColumnModel().getColumn(5).setPreferredWidth(120);//expense date
				getColumnModel().getColumn(6).setPreferredWidth(120);//date entered
				
				
				addMouseListener(frmEvents);		
			}
		};

		/*
		 *  Data table sits on this panel
		 */
		panelTable = new JPanel(){{
				setLayout(new BorderLayout());
				setPreferredSize(new Dimension(960, 200));
				add(new JScrollPane(dataTable), BorderLayout.CENTER);
			}
		};
		add(panelTable);


		/*
		 * method to load data into grid
		 */
		loadTable();

		/*
		 * The label panel
		 */		
		panelReviewLabels = new JPanel(){{
				setLayout(new GridLayout(1,6));
				setPreferredSize(new Dimension(960, 25));
				setBackground(Color.LIGHT_GRAY);
			}
		};			
		
		for (int x=0; x<lblText.length;x++) {
			JLabel lbl = new JLabel(lblText[x]);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			panelReviewLabels.add(lbl);
		}		
		add(panelReviewLabels);
	
	
		/*
		 * The review area
		 */
		lblType= new JLabel("") ;
		lblSubType= new JLabel("");
		lblComments= new JLabel("");
		lblAmount= new JLabel("");
		lblEnterDate= new JLabel("");
		lblExpDate= new JLabel("");
		JLabel[] labels = {lblType, lblSubType, lblComments, lblAmount, lblExpDate, lblEnterDate};
		
		panelReview = new JPanel(){{
				setLayout(new GridLayout(1,6));
				setPreferredSize(new Dimension(960, 100));
				setBackground(Color.LIGHT_GRAY);
			}
		};	
		
		for (JLabel lbl: labels) {
			lbl.setVerticalAlignment(SwingConstants.NORTH);
			lbl.setBorder(border);
			panelReview.add(lbl);			
		}
		add(panelReview);
	
		/*
		 * 
		 */
		add(btnDelete, BorderLayout.EAST);		

		/*
		 *  launch the form
		 */
		setVisible(true);

	}

	public void loadTable() {

		DefaultTableModel tblModel = (DefaultTableModel) dataTable.getModel();
		ResultSet rs = sqls.sqlSelect("SELECT * FROM " + sqls.EXPENSE_TABLE+ " WHERE " + sqls.RECORD_ID + " =  " + this.RECORD_ID);
		try {
			while (tblModel.getRowCount() > 0) {
				tblModel.removeRow(0);
			}
			Object[] row;
			while (rs.next()) {
				row = new Object[]{ rs.getString(sqls.KEY_ROWID),
						rs.getString(sqls.TYPE), rs.getString(sqls.SUB_TYPE), rs.getString(sqls.AMOUNT),
						rs.getString(sqls.COMMENTS), rs.getString(sqls.TRAN_DATE), rs.getString(sqls.CREATE_DT) 
						};				
				tblModel.addRow(row);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		

	}

	public void resetEntryForm() {		
		txtAmount.setText("");
		ddType.setSelectedIndex(0);
		ddSubType.setSelectedIndex(0);
		txtComments.setText("Enter Comments Here:");		
	}
	
	public void emptyLabels() {
		JLabel[] labels = {lblType, lblSubType, lblComments, lblAmount, lblExpDate, lblEnterDate};
		for (JLabel lbl: labels) {
			lbl.setText("");			
		}
		
		
	}

	private class FormEvents implements ActionListener, FocusListener, MouseListener {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==datePanel) {
				txtAmount.setEnabled(false);
				if (datePicker.getModel().getValue()!=null) {
					txtAmount.setEnabled(true);
					btnAddRecord.setEnabled(true);
				}
			}
			if (e.getSource()==ddType) {
				/*
				 * filter the sub type here 
				 */
				String expenseType = ddType.getSelectedItem().toString();
				ddSubType.setEnabled(false);
				datePicker.setEnabled(false);
				if(!expenseType.equals(typeList[0])) {									
					ddSubType.removeAllItems();
					if(expenseType.equals("Meals")||expenseType.equals("Entertainment")) {							
						for(String meal: meals) {
							ddSubType.addItem(meal);
						}					
						ddSubType.setEnabled(true);
						datePicker.setEnabled(true);
					}else {
						ddSubType.addItem("None");
					}
					
				}
			} else if (e.getSource()==btnAddRecord) {
				
				String[] bundle = new String[6];
				try {
					Date selectedDate = (Date) datePicker.getModel().getValue();

					bundle[0] = String.valueOf(RECORD_ID);
					bundle[1] = String.valueOf(f.format(selectedDate));
					bundle[2] = String.valueOf(Double.parseDouble(txtAmount.getText()));
					bundle[3] = ddType.getSelectedItem().toString();
					bundle[4] = ddSubType.getSelectedItem().toString();
					bundle[5] = (txtComments.getText().equals("Enter Comments Here:") ? "" : txtComments.getText());
					sqls.sqlInsertNewRecord(bundle);
					resetEntryForm();
					loadTable();
				}catch (Exception ex){
					JOptionPane.showMessageDialog(null, "Please re-check your input.");
				}

				
			}else if (e.getSource()==btnDelete) {
				int row = dataTable.getSelectedRow();
				int pk = Integer.parseInt(dataTable.getValueAt(row, 0).toString());
				String sql = "DELETE FROM " + sqls.EXPENSE_TABLE + " WHERE " + sqls.KEY_ROWID + " = " + pk + " AND " + sqls.RECORD_ID + " = " + RECORD_ID;
				sqls.sqlDelete(sql);
				loadTable();
				emptyLabels();
			} 

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == dataTable) {
				int row = dataTable.getSelectedRow();
				lblType.setText(dataTable.getValueAt(row, 1).toString());
				lblSubType.setText(dataTable.getValueAt(row, 2).toString());
				lblAmount.setText(dataTable.getValueAt(row, 3).toString());
				lblComments.setText("<html><p>" + dataTable.getValueAt(row, 4).toString() + "</html>");
				lblExpDate.setText(dataTable.getValueAt(row, 5).toString());
				lblEnterDate.setText(dataTable.getValueAt(row, 6).toString());				
			}
		}

		@Override
		public void focusLost(FocusEvent arg0) {			
		}

		@Override
		public void focusGained(FocusEvent arg0) {	
			if (arg0.getSource()==txtComments) {
				if (txtComments.getText().equals("Enter Comments Here:")) { 
					txtComments.setText("");
				}
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

}
