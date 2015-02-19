import java.sql.*;

public class sqlHandler {

	public static final String KEY_ROWID = "row_id", BU = "BUSINESS_UNIT",
			TYPE = "CHARGE_TYPE", TRAN_DATE = "TRAN_DATE", AMOUNT = "AMOUNT",
			CREATE_DT = "CREATE_DT", EXPENSE_TABLE = "TBL_EXPENSE",
			HEADER_TABLE = "TBL_REPORT_HEADER", USER = "USER",
			RECORD_ID = "HEADER_ID", TABLE_POSITION = "POSITION",
			SAT_DATE = "END_SAT";

	private StringBuilder qry = new StringBuilder();
	private Connection c = null;
	private PreparedStatement sql;
	private Statement stmt;

	public sqlHandler() {
		this.getConnection();
		try {
			stmt = c.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void getConnection() {
		try {
			if (c == null) {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:expenses.db");
				System.out.println("Opened database successfully");
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			c = null;
		}
	}

	public void createAllTables() throws SQLException {

		stmt.executeUpdate("DROP TABLE IF EXISTS " + EXPENSE_TABLE);
		stmt.executeUpdate("DROP TABLE IF EXISTS " + HEADER_TABLE);

		qry.append("CREATE TABLE  \n");

		qry.append(HEADER_TABLE + "\n(\n");

		qry.append(KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n");

		qry.append(USER + " TEXT NOT NULL,\n");

		qry.append(BU + " TEXT NOT NULL,\n");

		qry.append(SAT_DATE + " TEXT NOT NULL,\n");

		qry.append(CREATE_DT + " DATETIME DEFAULT CURRENT_TIMESTAMP\n");

		qry.append(")");

		stmt.executeUpdate(qry.toString());

		qry.delete(0, qry.length());

		qry.append("CREATE TABLE ");

		qry.append(EXPENSE_TABLE + "\n(\n");

		qry.append(KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, \n");

		qry.append(TYPE + " TEXT NOT NULL,\n");

		qry.append(RECORD_ID + " INTEGER NOT NULL,\n");

		qry.append(TRAN_DATE + " TEXT NOT NULL,\n");

		qry.append(AMOUNT + " REAL NOT NULL,\n");

		qry.append(TABLE_POSITION + " INTEGER NOT NULL,\n");

		qry.append(CREATE_DT + " DATETIME DEFAULT CURRENT_TIMESTAMP\n");

		qry.append(")");

		stmt.executeUpdate(qry.toString());

		qry.delete(0, qry.length());
	}

	public ResultSet sqlInsertNewHeader(String usr, String bu, String date)
			throws SQLException {
		sql = c.prepareStatement("INSERT INTO TBL_REPORT_HEADER (" + USER + "," + BU + ", " + SAT_DATE + ") VALUES (?,?,?);");
		sql.setString(1, usr);
		sql.setString(2, bu);
		sql.setString(3, date);
		sql.execute();

		return stmt.executeQuery("SELECT last_insert_rowid() ");
	}

	public ResultSet sqlInsertNewRecord(String tranDate, String tranType, double tranAmount, int x, int id) throws SQLException {
		sql = c.prepareStatement(" INSERT INTO " + EXPENSE_TABLE + " ("+ RECORD_ID + ", " + TRAN_DATE + ", " + AMOUNT + ", " + TYPE + ", " + TABLE_POSITION + ") VALUES (?,?,?,?,?)");
		sql.setInt(1, id);
		sql.setString(2, tranDate);
		sql.setDouble(3, tranAmount);
		sql.setString(4, tranType);
		sql.setInt(5, x);
		sql.execute();

		return stmt.executeQuery("SELECT last_insert_rowid() ");
	}

	public ResultSet sqlSelect(String str) throws SQLException {
		return stmt.executeQuery(str);
	}

	public void sqlDelete(String str) throws SQLException {
		sql = c.prepareStatement(str);
		sql.execute();
	}
}
