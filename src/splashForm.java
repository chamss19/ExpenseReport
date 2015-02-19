
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableModel;

public class splashForm extends JFrame {
	private JPanel topLeftPanel, topRightPanel, topCenterPanel, mainPanel;

	private JButton exitButton, newButton, loadButton;
	private JTable dataTable;
	private JPanel tblPanel;
	private JScrollPane scrlPane;
	private JTextField txtMovieName;
	private JLabel lblBU, lblWkEndDt, lblTitle;
	private JTextArea txtMovieNotes;
	final FlowLayout myLayOut = new FlowLayout(FlowLayout.CENTER);
	private JComboBox<String> ddBisUnits, ddSaturdays;

	sqlHandler sqls = new sqlHandler();
	formEvents frmEvents = new formEvents();

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
		setSize(650, 500);

		lblTitle = new JLabel("Expense Application", JLabel.CENTER);
		lblTitle.setPreferredSize(new Dimension(650, 35));
		lblTitle.setFont(new Font("Arial", Font.PLAIN, 16));
		add(lblTitle);

		/*
		 * PANEL FOR WEEK ENDING DATE
		 */
		lblWkEndDt = new JLabel("Please Choose Saturday Week Ending Date",
				JLabel.LEFT);
		ddSaturdays = new JComboBox<String>();
		ddSaturdays.setPreferredSize(new Dimension(100, 25));
		getSaturdays();
		ddSaturdays.setSelectedIndex(0);
		ddSaturdays.addActionListener(frmEvents);
		topLeftPanel = new JPanel(new BorderLayout());
		topLeftPanel.setAlignmentY(JPanel.LEFT_ALIGNMENT);
		topLeftPanel.setPreferredSize(new Dimension(295, 40));
		topLeftPanel.setBackground(Color.LIGHT_GRAY);
		topLeftPanel.add(lblWkEndDt, BorderLayout.NORTH);
		topLeftPanel.add(ddSaturdays, BorderLayout.SOUTH);
		add(topLeftPanel);

		/*
		 * PANEL FOR BU QUESTION
		 */
		lblBU = new JLabel("Please Choose a BU to Charge", JLabel.LEFT);
		ddBisUnits = new JComboBox<String>(buList);
		ddBisUnits.setPreferredSize(new Dimension(100, 25));
		ddBisUnits.addActionListener(frmEvents);
		topCenterPanel = new JPanel(new BorderLayout());
		topCenterPanel.setPreferredSize(new Dimension(295, 40));
		topCenterPanel.setBackground(Color.LIGHT_GRAY);
		topCenterPanel.add(lblBU, BorderLayout.NORTH);
		topCenterPanel.add(ddBisUnits, BorderLayout.SOUTH);
		add(topCenterPanel);

		/*
		 * ADD NEW RECORD BUTTON
		 */
		newButton = new JButton("ADD A NEW RECORD");
		newButton.setPreferredSize(new Dimension(600, 50));
		newButton.addActionListener(frmEvents);
		add(newButton);

		/*
		 * DATA TABLE AND SCROLL PANE
		 */
		DefaultTableModel model = new DefaultTableModel(null, new Object[] {"ID", "USER", "BU", "SATURDAY", "CREATE_DT" }) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		dataTable = new JTable(model);
		dataTable.setColumnSelectionAllowed(false);
		dataTable.getColumnModel().getColumn(0).setPreferredWidth(45);
		dataTable.getColumnModel().getColumn(1).setPreferredWidth(75);
		dataTable.getColumnModel().getColumn(2).setPreferredWidth(180);
		dataTable.getColumnModel().getColumn(3).setPreferredWidth(125);
		dataTable.getColumnModel().getColumn(4).setPreferredWidth(200);
		dataTable.addMouseListener(frmEvents);
		scrlPane = new JScrollPane(dataTable);

		/*
		 * PANEL TO PUT THE STUFF ON
		 */
		tblPanel = new JPanel();
		tblPanel.setLayout(new BorderLayout());
		tblPanel.setPreferredSize(new Dimension(600, 200));
		tblPanel.add(scrlPane, BorderLayout.CENTER);
		tblPanel.addMouseListener(frmEvents);
		add(tblPanel);

		/*
		 * load BUTTON
		 */
		loadButton = new JButton("LOAD RECORD");
		loadButton.setPreferredSize(new Dimension(300, 50));
		loadButton.addActionListener(frmEvents);
		add(loadButton, BorderLayout.SOUTH);

		/*
		 * EXIT BUTTON
		 */
		exitButton = new JButton("EXIT");
		exitButton.setPreferredSize(new Dimension(300, 50));
		exitButton.addActionListener(frmEvents);
		add(exitButton, BorderLayout.SOUTH);

		/*
		 * SET UP THE SQL CLASS
		 */
		loadTable();

		setVisible(true);
	}

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
						rs.getString(sqls.SAT_DATE),
						rs.getString(sqls.CREATE_DT) });
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	private class formEvents implements ActionListener, MouseListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			
			if (btn==exitButton) {
				dispose();
			}
			if (btn == newButton) {
				try {
					String usr = new String(System.getProperty("user.name"));
					String bu = new String(ddBisUnits.getSelectedItem().toString());
					String date = new String(ddSaturdays.getSelectedItem().toString());
					System.out.println(bu);
					ResultSet rs = sqls.sqlInsertNewHeader(usr, bu, date);
					int id = rs.getInt(1);
					reportForm rpt = new reportForm();
					rpt.loadRecord(id, bu, date);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			if (btn==loadButton) {
				int row = dataTable.getSelectedRow();
				int id = Integer.parseInt(dataTable.getValueAt(row, 0).toString());
				String bu = (dataTable.getValueAt(row, 2).toString());
				String date = (dataTable.getValueAt(row, 3).toString());
				/*
				 *  "ID", "USER", "BU", "SATURDAY","CREATE_DT"
				 */
				reportForm rpt = new reportForm();
				rpt.loadRecord(id, bu, date);
			}

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// System.out.println(e.getSource().toString());
			if (e.getSource() == dataTable) {

				int row = dataTable.getSelectedRow();

				System.out.print(row);
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
