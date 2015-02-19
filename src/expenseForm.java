
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


//System.getProperty("user.name")
public class expenseForm extends JFrame
{
	//SQLS DOE
	sqlHandler sqls = new sqlHandler();
	
	
	//LAYOUT SOME STUFF
	FlowLayout myLayOut = new FlowLayout(FlowLayout.LEFT);
	
	//FORM EVENTS CLASS
	FormEvents frmEvents = new FormEvents();
	
	//Border
	Border border =BorderFactory.createLoweredBevelBorder();
	
	//ALL THE DECLERATIONS!
	Dimension dimension = new Dimension(75,35);
	private JPanel topLeftPanel, topRightPanel,  topCenterPanel, mainPanel;
	private JLabel lblWkEndDt, lblBU, lblMon, lblTues, lblWed, lblThurs, lblFri, lblSat, lblSun, lblType, lblInstructions, lblInstructions1, lblInstructions2, lblInstructions3;
	private JLabel lblTotSun, lblTotMon, lblTotTues, lblTotWed, lblTotThurs, lblTotFri, lblTotSat, lblTot, lblTot1, lblTot2, lblTot3, lblTotBlank;
	private JComboBox<String> ddBisUnits, ddSaturdays, ddType ;
	private JTextField txtSun, txtMon, txtTues, txtWed, txtThurs, txtFri, txtSat;
	private JTextField txtSun1, txtMon1, txtTues1, txtWed1, txtThurs1, txtFri1, txtSat1;
	private JTextField txtSun2, txtMon2, txtTues2, txtWed2, txtThurs2, txtFri2, txtSat2;
	private JButton addButton;
	private Integer id = 1;
	
	String[] buList = { "","8501 Accounting Finance", "8505 Admin Corporate", "8506 Transportation", "8507 Board", "8508 Sales", "8509 HR", "8510 G3 IT",  "8710 Operations Closure",
						"875623 Ukiah Capsule Forming", "8765 QA Lab Closure", "8767 Research & Innovation", "8770 Admin Closure", "8771 Supply Chain Closure", "8772 Engineering Closure", 
						"8805 Admin Label", "8815 Production Label", "8832 Web Press Label", "8905 Admin G3 Realty 2", "8955 Admin Winery Ops", "8960 Services Winery Ops", "8963 Bottle Etching",
						"8964 Repack", "8965 Mobile Bottling", "8966 Grape Seed Oil"						
						};

	
	String[] typeList = {"","Meals","Hotels Incl Tax","Mileage", "Entertainment","Airfare","Auto Rental","Taxi","Parking/Tolls/Tips","Internet", "Cell Phone", "Telephone"};	
	String usr = new String(System.getProperty("user.name") );
	String userId = new String ("256054");
	
	
	public void newRecord()
	{
// 		try 
//		{
//
//			startForm();
//		} 
//		catch (SQLException e) 
//		{
//			e.printStackTrace();
//		}
	}
	
	
	public void loadRecord(int id)
	{
		this.id=id;
		startForm();
	}
	
	
	@SuppressWarnings({ "serial" })
	private void startForm()
	{
		
		//FORM LAYOUT STUFF
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		setLayout(myLayOut);			
		setSize(1000, 900);	
		
		
		//QRY AD TO GET USERS AB#				
		setTitle("G3 Enterprise, Inc. Expense Report" + " " + usr + ": " + userId + " Record Number: " + Integer.toString(id));
		setFont(new Font("Arial", Font.PLAIN, 12));
		
		
		//PANEL FOR WEEK ENDING DATE
		lblWkEndDt= new JLabel("Please Choose Saturday Week Ending Date", JLabel.LEFT);		
		ddSaturdays = new JComboBox<String>();
		ddSaturdays.setPreferredSize(new Dimension(100, 25));		
		getSaturdays();
		ddSaturdays.setSelectedIndex(0);
		ddSaturdays.addActionListener(frmEvents);	
		topLeftPanel = new JPanel(new BorderLayout());
		topLeftPanel.setAlignmentY(JPanel.LEFT_ALIGNMENT);		
		topLeftPanel.setPreferredSize(new Dimension(315, 40));
		topLeftPanel.setBackground(Color.LIGHT_GRAY);
		topLeftPanel.add(lblWkEndDt, BorderLayout.NORTH);
		topLeftPanel.add(ddSaturdays, BorderLayout.SOUTH);
		add(topLeftPanel);
		//PANEL FOR WEEK ENDING DATE
		
		//PANEL FOR BU QUESTION		
		lblBU= new JLabel("Please Choose a BU to Charge", JLabel.LEFT);		
		ddBisUnits = new JComboBox<String>(buList);
		ddBisUnits.setPreferredSize(new Dimension(100, 25));
		ddBisUnits.addActionListener(frmEvents);	
		topCenterPanel = new JPanel(new BorderLayout());
		topCenterPanel.setPreferredSize(new Dimension(315, 40));
		topCenterPanel.setBackground(Color.LIGHT_GRAY);
		topCenterPanel.add(lblBU, BorderLayout.NORTH);
		topCenterPanel.add(ddBisUnits, BorderLayout.SOUTH);
		add(topCenterPanel);
		//PANEL FOR BU QUESTION
		
		//PANEL FOR Charge Type QUESTION		
		lblType= new JLabel("Please Choose Type of Charge", JLabel.LEFT);		
		ddType = new JComboBox<String>(typeList);
		ddType.setPreferredSize(new Dimension(100, 25));
		ddType.addActionListener(frmEvents);
		topRightPanel = new JPanel(new BorderLayout());
		topRightPanel.setPreferredSize(new Dimension(315, 40));
		topRightPanel.setBackground(Color.LIGHT_GRAY);
		topRightPanel.add(lblType, BorderLayout.NORTH);
		topRightPanel.add(ddType, BorderLayout.SOUTH);
		add(topRightPanel);
		//PANEL FOR Charge Type QUESTION
		
		lblInstructions = new JLabel(){{setName("lblInstructions"); setText("<html>    <br><br>"); setHorizontalAlignment(LEFT);}};
		lblInstructions1 = new JLabel(){{setName("lblInstructions1"); setText("<html>Dinner<br>"); setHorizontalAlignment(CENTER);}};
		lblInstructions2 = new JLabel(){{setName("lblInstructions2"); setText("<html>Lunch<br>"); setHorizontalAlignment(CENTER);}};		
		lblInstructions3 = new JLabel(){{setName("lblInstructions3"); setText("<html>Breakfast<br>"); setHorizontalAlignment(CENTER);}};

		
		lblSun= new JLabel()		{{setName("lblSun"); setText("<html>    Sun<br>" + getWeekDates(6)); setHorizontalAlignment(CENTER);}};
		lblMon= new JLabel()		{{setName("lblMon"); setText("<html>    Mon<br>" + getWeekDates(5)); setHorizontalAlignment(CENTER);}};
		lblTues= new JLabel() 		{{setName("lblTues"); setText("<html>    Tues<br>" + getWeekDates(4)); setHorizontalAlignment(CENTER);}};		
		lblWed= new JLabel()		{{setName("lblWed"); setText("<html>    Wed<br>" + getWeekDates(3)); setHorizontalAlignment(CENTER);}};		
		lblThurs= new JLabel()		{{setName("lblThurs"); setText("<html>    Thurs<br>" + getWeekDates(2)); setHorizontalAlignment(CENTER);}};				
		lblFri= new JLabel()		{{setName("lblFri"); setText("<html>    Fri<br>" + getWeekDates(1)); setHorizontalAlignment(CENTER);}};			
		lblSat= new JLabel()		{{setName("lblSat"); setText("<html>    Sat<br>" + getWeekDates(0)); setHorizontalAlignment(CENTER);}};
		
		lblTot= new JLabel()		{{setName("lblTot"); setText("<html><br>    Totals</html>");setHorizontalAlignment(CENTER);}};
		lblTotBlank= new JLabel()	{{setName("lblTotBlank"); setText("<html><br>    </html>");setHorizontalAlignment(CENTER);}};
		lblTot1= new JLabel()		{{setName("lblTot1"); setText("<html><br>    </html>");setHorizontalAlignment(CENTER);}};
		lblTot2= new JLabel()		{{setName("lblTot2"); setText("<html><br>    </html>");setHorizontalAlignment(CENTER);}};
		lblTot3= new JLabel()		{{setName("lblTot3"); setText("<html><br>    </html>");setHorizontalAlignment(CENTER);}};
		lblTotSun= new JLabel()		{{setName("lblTotSun"); setHorizontalAlignment(CENTER);}};
		lblTotMon= new JLabel()		{{setName("lblTotMon"); setHorizontalAlignment(CENTER);}};
		lblTotTues= new JLabel() 	{{setName("lblTotTues"); setHorizontalAlignment(CENTER);}};		
		lblTotWed= new JLabel()		{{setName("lblTotWed"); setHorizontalAlignment(CENTER);}};		
		lblTotThurs= new JLabel()	{{setName("lblTotThurs"); setHorizontalAlignment(CENTER);}};				
		lblTotFri= new JLabel()		{{setName("lblTotFri"); setHorizontalAlignment(CENTER);}};			
		lblTotSat= new JLabel()		{{setName("lblTotSat"); setHorizontalAlignment(CENTER);}};
				
		txtSun= new JTextField()	{{setName("txtSun");}};	
		txtMon= new JTextField()	{{setName("txtMon");}};		
		txtTues= new JTextField()	{{setName("txtTues");}};		
		txtWed= new JTextField()	{{setName("txtWed");}};		
		txtThurs= new JTextField()	{{setName("txtThurs");}};		
		txtFri= new JTextField()	{{setName("txtFri");}};		
		txtSat= new JTextField()	{{setName("txtSat");}};		
		txtSun1= new JTextField()	{{setName("txtSun1");}};		
		txtMon1= new JTextField()	{{setName("txtMon1");}};		
		txtTues1= new JTextField()	{{setName("txtTues1");}};		
		txtWed1= new JTextField()	{{setName("txtWed1");}};		
		txtThurs1= new JTextField()	{{setName("txtThurs1");}};		
		txtFri1= new JTextField()	{{setName("txtFri1");}};		
		txtSat1= new JTextField()	{{setName("txtSat1");}};		
		txtSun2= new JTextField()	{{setName("txtSun2");}};		
		txtMon2= new JTextField()	{{setName("txtMon2");}};		
		txtTues2= new JTextField()	{{setName("txtTues2");}};		
		txtWed2= new JTextField()	{{setName("txtWed2");}};		
		txtThurs2= new JTextField()	{{setName("txtThurs2");}};		
		txtFri2= new JTextField()	{{setName("txtFri2");}};		
		txtSat2= new JTextField()	{{setName("txtSat2");}};		
		
		addButton = new JButton("Add Entry to Report") 
		{{
			setPreferredSize(new Dimension(150, 25));
			addActionListener(frmEvents);
		}};
		
		//MAIN PANEL (Need to have this loop through days of week and create that way)	
		mainPanel= new JPanel();
		
		GroupLayout gl = new GroupLayout(mainPanel);
		mainPanel.setLayout(gl);
		mainPanel.setPreferredSize(new Dimension(960, 210));
		mainPanel.setBackground(Color.LIGHT_GRAY);
		mainPanel.setBorder(border);		
		
		gl.setHorizontalGroup(
				gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblInstructions).addComponent(lblInstructions1).addComponent(lblInstructions2).addComponent(lblInstructions3).addComponent(lblTot))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblSun).addComponent(txtSun).addComponent(txtSun1).addComponent(txtSun2).addComponent(lblTotSun))					  
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblMon).addComponent(txtMon).addComponent(txtMon1).addComponent(txtMon2).addComponent(lblTotMon))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblTues).addComponent(txtTues).addComponent(txtTues1).addComponent(txtTues2).addComponent(lblTotTues))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblWed).addComponent(txtWed).addComponent(txtWed1).addComponent(txtWed2).addComponent(lblTotWed))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblThurs).addComponent(txtThurs).addComponent(txtThurs1).addComponent(txtThurs2).addComponent(lblTotThurs))	
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblFri).addComponent(txtFri).addComponent(txtFri1).addComponent(txtFri2).addComponent(lblTotFri))		
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblSat).addComponent(txtSat).addComponent(txtSat1).addComponent(txtSat2).addComponent(lblTotSat))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblTotBlank).addComponent(lblTot1).addComponent(lblTot2).addComponent(lblTot3).addComponent(addButton))
				);
		
		gl.setVerticalGroup(
				gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblInstructions).addComponent(lblSun).addComponent(lblMon).addComponent(lblTues).addComponent(lblWed).addComponent(lblThurs).addComponent(lblFri).addComponent(lblSat).addComponent(lblTotBlank))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblInstructions1).addComponent(txtSun).addComponent(txtMon).addComponent(txtTues).addComponent(txtWed).addComponent(txtThurs).addComponent(txtFri).addComponent(txtSat).addComponent(lblTot1))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblInstructions2).addComponent(txtSun1).addComponent(txtMon1).addComponent(txtTues1).addComponent(txtWed1).addComponent(txtThurs1).addComponent(txtFri1).addComponent(txtSat1).addComponent(lblTot2))
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblInstructions3).addComponent(txtSun2).addComponent(txtMon2).addComponent(txtTues2).addComponent(txtWed2).addComponent(txtThurs2).addComponent(txtFri2).addComponent(txtSat2).addComponent(lblTot3))		
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblTot).addComponent(lblTotSun).addComponent(lblTotMon).addComponent(lblTotTues).addComponent(lblTotWed).addComponent(lblTotThurs).addComponent(lblTotFri).addComponent(lblTotSat))	
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(addButton))
				);
		
		for (Component c : mainPanel.getComponents()) 
		{
		    if (c instanceof JTextField) 
		    {
		    	((JTextField)c).setPreferredSize(dimension);
		    	((JTextField)c).addFocusListener(frmEvents);
		    }	
		}
		mainPanel.setVisible(false);
		
		add( mainPanel );		
		//MAIN PANEL

				
		//LAUNCH THE FORM
		setVisible(true);
		

		
		
	}
	
	
	@SuppressWarnings("deprecation")
	private void getSaturdays()
	{
		Calendar cal =  Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		SimpleDateFormat f=new SimpleDateFormat("MM/dd/yyyy");
		
		cal.add(Calendar.YEAR, -1);
		start.setTime(cal.getTime());
		
		for (Date date = end.getTime(); !end.before(start); end.add(Calendar.DATE, -1), date = end.getTime()) 
		{
			if (date.getDay()==6)
			{
				ddSaturdays.addItem(f.format(date));
			}					
		}
	}
	
	
	private String getWeekDates(int x)
	{
		SimpleDateFormat f=new SimpleDateFormat("MM/dd/yyyy");
		String dt =ddSaturdays.getSelectedItem().toString();
		Calendar cal =  new GregorianCalendar();
		try 
		{
			Date date = f.parse(dt);
			cal.setTime(date);
			cal.add(Calendar.DATE, -x);
			dt = f.format(cal.getTime());
			
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		
		return dt;
	}
	
	
	private class FormEvents implements ActionListener, FocusListener 
	{
		@SuppressWarnings("serial")
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == ddSaturdays)
			{
				JLabel[] weekList = {lblSat ,lblFri, lblThurs, lblWed, lblTues, lblMon,lblSun};
				String[] days = {"Sat","Fri","Thurs","Wed","Tues","Mon","Sun"};
				for (int i = 0; i < weekList.length; i++) 
				{
					JLabel lbl = weekList[i];
					lbl.setText("<html>" + days[i]+ "<br>" + getWeekDates(i));
				}
					
			}
			else if (e.getSource()==ddType)
			{	
				HashMap<JTextField, JLabel> map = new HashMap<JTextField, JLabel>()
				{{
					put(txtSun, lblTotSun);	 put(txtMon, lblTotMon); put(txtTues, lblTotTues);
					put(txtWed, lblTotWed);  put(txtThurs, lblTotThurs); put(txtFri, lblTotFri);
					put(txtSat, lblTotSat);  put(txtSun1, lblTot1); put(txtSun2, lblTot2); put(txtMon1, lblTot3);
				}};		
				
				String val = ddType.getSelectedItem().toString();
				for (int x =0; x<3; x++)
				{
					JTextField[] weekList = getHorizontalList(x);
					for (JTextField txt: weekList )
					{
						txt.setText("");
						if (map.containsKey(txt))
						{
							JLabel lbl = map.get(txt);
							lbl.setText("");
						}
					}
				}			
				
				switch (val)
				{
					case "Mileage":
						setlblTxt("Daily Mileage @ .56 mile","","");
						setDisabled(false);
						break;
					case  "Entertainment": case  "Meals" :
						setlblTxt("Dinner","Lunch","Breakfast");
					    setDisabled(true);
					    break;		
				    default:
						setlblTxt(val,"","");
					    setDisabled(false);
					    break;				    			    
				}
				mainPanel.setVisible(true);	
			}
			else if (e.getSource()==addButton)
			{
				HashMap<JTextField, JLabel> map = new HashMap<JTextField, JLabel>() 
				{{
					put(txtSun,lblSun);
					put(txtSun1,lblSun);
					put(txtSun2,lblSun);
					put(txtMon,lblMon);
					put(txtMon1,lblMon);
					put(txtMon2, lblMon);
					put(txtTues,lblTues);
					put(txtTues1, lblTues);
					put(txtTues2,lblTues);
					put(txtWed,lblWed);
					put(txtWed1,lblWed);
					put(txtWed2, lblWed);
					put(txtThurs,lblThurs);
					put(txtThurs1,lblThurs);
					put(txtThurs2, lblThurs);
					put(txtFri, lblFri);
					put(txtFri1,lblFri);
					put(txtFri2, lblFri);
					put(txtSat, lblSat);
					put(txtSat1,lblSat);
					put(txtSat2, lblSat);
				}};
				
				
				String tranDate, tranType, tranBU;
				double tranAmount=0;
				for (int x =0; x<3; x++)
				{
					JTextField[] weekList = getHorizontalList(x);
					for (JTextField txt: weekList )
					{

						if (txt.getText().matches("[-+]?\\d*\\.?\\d+")) 
						{
							tranType = ddType.getSelectedItem().toString();
							tranBU = ddBisUnits.getSelectedItem().toString();
							JLabel lbl = map.get(txt);
	
			                tranDate = lbl.getText().substring(lbl.getText().indexOf("<br>")+4, lbl.getText().length());
			                
			                
							tranAmount = Double.parseDouble(txt.getText());
							try 
							{

								ResultSet rs = sqls.sqlInsertNewRecord(tranDate, tranType,  tranAmount, x, id);
								int ids = new Integer(rs.getInt(1));
								System.out.print(ids);
							} 
							catch (SQLException e1) 
							{
								e1.printStackTrace();
							}
						}
					}
				}
			}
			
		}
			
		private void setDisabled(Boolean b)
		{			
			for (int x =1; x<3; x++)
			{
				JTextField[] weekList = getHorizontalList(x);
				for (JTextField txt: weekList )
				{
					txt.setEnabled(b);
				}
			}
		}
		
		@Override
		public void focusLost(FocusEvent arg0)
		{		
			String val = ddType.getSelectedItem().toString();
			switch (val)
			{
				case "Mileage":
					mileageTotal(arg0.getSource());
					weeklyCalc();
					break;
				default:
					dailyCalc();
					weeklyCalc();
					break;					
			}			
		}
		
		@SuppressWarnings("serial")
		private void mileageTotal(Object obj)
		{
			HashMap<Object, JLabel> map = new HashMap<Object, JLabel>()
			{{
				put(txtSun, lblTotSun);		
				put(txtMon, lblTotMon);
				put(txtTues, lblTotTues);
				put(txtWed, lblTotWed);
				put(txtThurs, lblTotThurs);
				put(txtFri, lblTotFri);
				put(txtSat, lblTotSat);
			}};
			
			JLabel lbl = map.get(obj);
			JTextField txt = (JTextField)obj;
					
			if (txt.getText().matches("[-+]?\\d*\\.?\\d+"))
			{		
				lbl.setText("<Html> $" + String.format("%1$,.2f",Double.parseDouble(txt.getText())*.56) +" </html>");		
			}		
		}
				
		@SuppressWarnings("serial")
		private void weeklyCalc()
		{
			HashMap<Integer, JLabel> map = new HashMap<Integer, JLabel>()
			{{put(0, lblTot1); put(1, lblTot2); put(2, lblTot3);}};
			
			for (int x =0; x<3; x++)
			{
				Double weekTotal=0.0;
				String txtDay = new String("");
				JTextField[] weekList = getHorizontalList(x);
				JLabel lbl = map.get(x);
				for (JTextField txt: weekList )
				{
					if ( txt.getText() != null)
					{
						txtDay = txt.getText() ;
					}
					else
					{
						txtDay="0";
					}
					
					if (txtDay.matches("[-+]?\\d*\\.?\\d+"))
					{
						weekTotal+=Double.parseDouble(txtDay);
					}	
					
					lbl.setText("<Html>&nbsp;" + String.format("%1$,.2f",weekTotal)+"</html>");
				}
			}
		}
		
		@SuppressWarnings("serial")
		private void dailyCalc()
		{			
			HashMap<Integer, JLabel> map = new HashMap<Integer, JLabel>()
			{{
				put(0, lblTotSun);	put(1, lblTotMon);put(2, lblTotTues);put(3, lblTotWed);
				put(4, lblTotThurs);put(5, lblTotFri);put(6, lblTotSat);
			}};

	
			for (int x =0; x<7; x++)
			{
				Double dayTotal=0.0;
				JTextField[] dayList = getVerticalList(x);
				JLabel lbl = map.get(x);
				for (JTextField txt: dayList )
				{
					if(txt.getText().matches("[-+]?\\d*\\.?\\d+"))
					{
						dayTotal+=Double.parseDouble(txt.getText());
					}
					lbl.setText("<Html>&nbsp;$" +String.format("%1$,.2f",dayTotal)+ "</html>");
				}
			}		
		}
	
		@Override
		public void focusGained(FocusEvent arg0){}
		
		private JTextField[] getHorizontalList(int x)
		{	
			
			switch (x)
			{				
				case 0:
					JTextField[]  weekList = {txtSun, txtMon, txtTues, txtWed, txtThurs, txtFri, txtSat};
					return weekList;
				case 1:
					JTextField[]  weekList1= {txtSun1, txtMon1, txtTues1, txtWed1, txtThurs1, txtFri1, txtSat1};
					return weekList1;
				case 2:
					JTextField[]  weekList2= {txtSun2, txtMon2, txtTues2, txtWed2, txtThurs2, txtFri2, txtSat2};
					return weekList2;
			}		
			return null;
		}
		
		private JTextField[] getVerticalList(int x)
		{			
			switch (x)
			{
				case 0:
					JTextField[] dayList0= {txtSun, txtSun1, txtSun2};
					return dayList0;
				case 1:
					JTextField[] dayList1= {txtMon, txtMon1, txtMon2};
					return dayList1;
				case 2:
					JTextField[] dayList2= {txtTues, txtTues1, txtTues2};
					return dayList2;
				case 3:
					JTextField[] dayList3= {txtWed, txtWed1, txtWed2};
					return dayList3;
				case 4:				
					JTextField[] dayList4= {txtThurs, txtThurs1, txtThurs2};
					return dayList4;
				case 5:
					JTextField[] dayList5= {txtFri, txtFri1, txtFri2};
					return dayList5;
				case 6:
					JTextField[] dayList6= {txtSat, txtSat1, txtSat2};
					return dayList6;
			}		
			return null;
		}
	
		private void setlblTxt(String lbl1, String lbl2, String lbl3)
		{
			lblInstructions1.setText("<HTML>" + lbl1 +"<br>");
		    lblInstructions2.setText("<HTML>" + lbl2 +"<br>");
		    lblInstructions3.setText("<HTML>" + lbl3 +"<br>");
		}	
	}



}
