import java.sql.*;

import javax.swing.JOptionPane;

public class sqlHandler {

	public static final String KEY_ROWID = "row_id", BU = "BUSINESS_UNIT",
			TYPE = "CHARGE_TYPE", TRAN_DATE = "TRAN_DATE", AMOUNT = "AMOUNT",
			CREATE_DT = "CREATE_DT", EXPENSE_TABLE = "TBL_EXPENSE",
			HEADER_TABLE = "TBL_REPORT_HEADER", USER = "USER",
			RECORD_ID = "HEADER_ID", SUB_TYPE = "POSITION",
			SAT_DATE = "END_SAT", COMMENTS="COMMENTS";

	private StringBuilder qry = new StringBuilder();
	private Connection c = null;
	private PreparedStatement sql;
	private Statement stmt;

	public sqlHandler() {
		this.getConnection();
		try {
			stmt = c.createStatement();
			createAllTables();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getConnection() {
		try {
			if (c == null) {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:expenses.db");
				
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			c = null;
		}
	}

	public void deleteTables() throws SQLException{
		stmt.executeUpdate("DROP TABLE IF EXISTS " + EXPENSE_TABLE);
		stmt.executeUpdate("DROP TABLE IF EXISTS " + HEADER_TABLE);

	}
	
	public void createAllTables() throws SQLException {


		qry.append("CREATE TABLE IF NOT EXISTS \n");

		qry.append(HEADER_TABLE + "\n(\n");

		qry.append(KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n");

		qry.append(USER + " TEXT NOT NULL,\n");

		qry.append(BU + " TEXT NOT NULL,\n");

		qry.append(CREATE_DT + " DATETIME DEFAULT CURRENT_TIMESTAMP\n");

		qry.append(")");

		stmt.executeUpdate(qry.toString());

		qry.delete(0, qry.length());
		
		

		qry.append("CREATE TABLE IF NOT EXISTS ");

		qry.append(EXPENSE_TABLE + "\n(\n");

		qry.append(KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n");

		qry.append(TYPE + " TEXT NOT NULL,\n");

		qry.append(SUB_TYPE + " TEXT NOT NULL,\n");
		
		qry.append(RECORD_ID + " INTEGER NOT NULL,\n");

		qry.append(TRAN_DATE + " TEXT NOT NULL,\n");

		qry.append(COMMENTS + " TEXT,\n");
		
		qry.append(AMOUNT + " REAL NOT NULL,\n");

		qry.append(CREATE_DT + " DATETIME DEFAULT CURRENT_TIMESTAMP\n");

		qry.append(")");

		stmt.executeUpdate(qry.toString());

		qry.delete(0, qry.length());
		
//		System.out.println("TABLES CREATED");
	}

	public ResultSet sqlInsertNewHeader(String usr, String bu){
		try {
			sql = c.prepareStatement("INSERT INTO TBL_REPORT_HEADER (" + USER + "," + BU + ") VALUES (?,?);");
			sql.setString(1, usr);
			sql.setString(2, bu);
			sql.execute();

			return stmt.executeQuery("SELECT last_insert_rowid() ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public ResultSet sqlInsertNewRecord(String[] bundle)  {
		try {
			sql = c.prepareStatement(" INSERT INTO " + EXPENSE_TABLE + " ("+ RECORD_ID + ", " + TRAN_DATE + ", " + AMOUNT + ", " + TYPE + ", " + SUB_TYPE + ", " + COMMENTS + ") VALUES (?,?,?,?,?,?)");
			sql.setString(1, bundle[0]);
			sql.setString(2, bundle[1]);
			sql.setString(3, bundle[2]);
			sql.setString(4, bundle[3]);
			sql.setString(5, bundle[4]);
			sql.setString(6, bundle[5]);
			sql.execute();
			return stmt.executeQuery("SELECT last_insert_rowid() ");
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, "Broke in insert.");
			e1.printStackTrace();
		}

		return null;
	}

	public ResultSet sqlSelect(String str)  {
		try {
			return stmt.executeQuery(str);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void sqlDelete(String str)  {
		try {
			sql = c.prepareStatement(str);
			sql.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
