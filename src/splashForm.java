
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;



public class splashForm extends JFrame {
	private JPanel topLeftPanel, panelNewRecord, panelBisUnit,  panelLoad, panelExit;

	private JButton btnExit, btnNew, btnLoad;
	private JTable dataTable;
	private JPanel panelDataTable;
	private JScrollPane scrlPane;
	private JTextField txtMovieName;
	private JLabel lblBU, lblWkEndDt, lblTitle;
	private JTextArea txtMovieNotes;
	final FlowLayout myLayOut = new FlowLayout(FlowLayout.CENTER);
	private JComboBox<String> ddBisUnits, ddSaturdays;

	sqlHandler sqls = new sqlHandler();
	formEvents frmEvents = new formEvents();

	/*
	* All the BUs
	*/
	String[] buList = { "", "8501 Accounting Finance", "8505 Admin Corporate",
			"8506 Transportation", "8507 Board", "8508 Sales", "8509 HR",
			"8510 G3 IT", "8710 Operations Closure",
			"875623 Ukiah Capsule Forming", "8765 QA Lab Closure",
			"8767 Research & Innovation", "8770 Admin Closure",
			"8771 Supply Chain Closure", "8772 Engineering Closure",
			"8805 Admin Label", "8815 Production Label",
			"8832 Web Press Label", "8905 Admin G3 Realty 2",
			"8955 Admin Winery Ops", "8960 Services Winery Ops",
			"8963 Bottle Etching", "8964 Repack", "8965 Mobile Bottling",
			"8966 Grape Seed Oil" };

	public void startForm() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(myLayOut);
		setSize(650, 515);

		lblTitle = new JLabel("Expense Application", JLabel.CENTER);
		lblTitle.setPreferredSize(new Dimension(650, 35));
		lblTitle.setFont(new Font("Arial", Font.PLAIN, 16));
		add(lblTitle);

		/*
		 * PANEL FOR BU QUESTION
		 */

		ddBisUnits = new JComboBox<String>(buList){{
				setPreferredSize(new Dimension(100, 25));
				addActionListener(frmEvents);
			}		
		};

		panelBisUnit = new JPanel(new BorderLayout()){{
				setBorder(BorderFactory.createRaisedBevelBorder());
				setPreferredSize(new Dimension(295, 50));
				add(new JLabel("Please Choose a BU to Charge", JLabel.LEFT), BorderLayout.NORTH);
				add(ddBisUnits, BorderLayout.SOUTH);
			}
		};		
		add(panelBisUnit);

		/*
		 * ADD NEW RECORD BUTTON
		 */
		btnNew = new JButton("ADD A NEW RECORD"){{
				setPreferredSize(new Dimension(100, 25));
				addActionListener(frmEvents);
				setEnabled(false);		
			}
		};

		/*
		 * panel for the new button
		 */
		panelNewRecord = new JPanel(new BorderLayout()){{
				setPreferredSize(new Dimension(295, 50));
				add(btnNew, BorderLayout.SOUTH);
			}
		};		
		add(panelNewRecord);


		/*
		 * DATA TABLE AND SCROLL PANE
		 */
		DefaultTableModel model = new DefaultTableModel(null, new Object[] {"ID", "USER", "BU", "Entered Date" }) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		/*
		* Create the grid
		*/
		dataTable = new JTable(model){{
				setColumnSelectionAllowed(false);
				getColumnModel().getColumn(0).setMaxWidth(0);
				getColumnModel().getColumn(0).setMinWidth(0);
				getColumnModel().getColumn(0).setPreferredWidth(0);
				getColumnModel().getColumn(1).setPreferredWidth(75);
				getColumnModel().getColumn(2).setPreferredWidth(180);
				getColumnModel().getColumn(3).setPreferredWidth(200);	
				addMouseListener(frmEvents);
			}
		};

		/*
		 * data table sits in this panel
		 */
		panelDataTable = new JPanel(){{
				setLayout(new BorderLayout());
				setPreferredSize(new Dimension(600, 200));
				add(new JScrollPane(dataTable), BorderLayout.CENTER);
				addMouseListener(frmEvents);
			}
		};		
		add(panelDataTable);

		/*
		 * load button and panel
		 */
		btnLoad = new JButton("LOAD RECORD"){{
				setPreferredSize(new Dimension(100, 35));
				addActionListener(frmEvents);
			}	
		};		
		

		panelLoad = new JPanel(new BorderLayout()){{
				setPreferredSize(new Dimension(295, 50));
				add(btnLoad, BorderLayout.NORTH);
			}
		};		
		add(panelLoad, BorderLayout.NORTH);
	
		
		/*
		 * create an empty rows between load and exit buttons
		 */
		add(new JPanel() {{setPreferredSize(new Dimension(295,50));}});
		add(new JPanel() {{setPreferredSize(new Dimension(595,50)); setBorder(BorderFactory.createEtchedBorder());}});
		add(new JPanel() {{setPreferredSize(new Dimension(295,50));}});

		/*
		 * Exit button panel and button
		 */
		btnExit = new JButton("EXIT"){{
				setPreferredSize(new Dimension(100, 35));
				addActionListener(frmEvents);	
				setBackground(Color.RED);
			}
		};

		panelExit = new JPanel(new BorderLayout()){{
				setPreferredSize(new Dimension(295, 50));
				add(btnExit, BorderLayout.SOUTH);
			}
		};		
		add(panelExit);

		
		
		/*
		 * SET UP THE SQL CLASS
		 */
		loadTable();

		setVisible(true);
	}

	/*
	 * loop through dates and find the saturdays
	 */
	private void getSaturdays() {
		Calendar cal = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");

		cal.add(Calendar.YEAR, -1);
		start.setTime(cal.getTime());

		for (Date date = end.getTime(); !end.before(start); end.add(Calendar.DATE, -1), date = end.getTime()) {
			if (date.getDay() == 6) {
				ddSaturdays.addItem(f.format(date));
			}
		}
	}

	public void loadTable() throws SQLException {
		DefaultTableModel tblModel = (DefaultTableModel) dataTable.getModel();
		ResultSet rs = sqls.sqlSelect("SELECT * FROM " + sqls.HEADER_TABLE);
		try {
			while (rs.next()) {
				tblModel.addRow(new Object[] { rs.getString(sqls.KEY_ROWID),
						rs.getString(sqls.USER), rs.getString(sqls.BU),
						rs.getString(sqls.CREATE_DT) });
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private class formEvents implements ActionListener, MouseListener {
		String bu, user = new String(System.getProperty("user.name"));;
		int id, row;
		
		private void newButton()  {				
				bu = new String(ddBisUnits.getSelectedItem().toString());
				try {
					ResultSet rs = sqls.sqlInsertNewHeader(user, bu);
					id=rs.getInt(1);
					new EntryForm().loadRecord(id, bu);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error");
				}					
		}
		
		private void loadButton() {
			id = Integer.parseInt(dataTable.getValueAt(row, 0).toString());
			bu = (dataTable.getValueAt(row, 2).toString());
			new EntryForm().loadRecord(id, bu);
		}
		
		private void exitButton() {
			dispose();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() instanceof JButton){
				JButton btn = (JButton) e.getSource();
				this.row= dataTable.getSelectedRow();
				if (btn==btnNew) {
					this.newButton();
				}
				
				if (btn==btnLoad) {
					this.loadButton();
				}
				
				if (btn==btnExit) {		
					this.exitButton();
				}
			}else if(e.getSource()==ddBisUnits) {
				btnNew.setEnabled(false);
				if (!ddBisUnits.getSelectedItem().toString().equals("")) {
					btnNew.setEnabled(true);
				}
				
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getSource() == dataTable) {				
				this.row = dataTable.getSelectedRow();
				DefaultTableModel tblModel = (DefaultTableModel) dataTable.getModel();

				while (tblModel.getRowCount() > 0) {
					tblModel.removeRow(0);
				}

				try {
					loadTable();
					dataTable.setRowSelectionInterval(row, row);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

}
