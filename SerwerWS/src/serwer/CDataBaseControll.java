package serwer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CDataBaseControll {

	static final String driver		 = "com.mysql.jdbc.Driver";
	static final String hostName 	 = "localhost";
	static final String port 		 = "3306";
	static final String dataBaseName = "bddatabase";
//	static final String dataBaseName = "test11";
	private static String login 	 = "root";
	private static String password 	 = "start";
	private static String URL 		 = "jdbc:mysql://"+hostName+":"+port+"/"+dataBaseName+"";
	private static Connection connection;
	private static Statement command;
	
	
	
	private static void checkConnection()
	{
		System.out.println("Info -- checkConnection -- sprawdzanie polaczenia z baza.");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("Blad -- checkConnection -- Nie znaleziono sterownika JDBC.");
			e.printStackTrace();
			return;
		}
	}
	
	public static void disconnect()
	{
		try {
			command.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Blad -- checkConnection -- Blad zamkniecia polaczenia z baza.");
			e.printStackTrace();
		}
		
	}
	
	public static void connect()
	{
		 connection = null;

		try {
			connection = DriverManager.getConnection(URL,login, password);
			command = connection.createStatement();
		} catch (SQLException e) {
			System.out.println("Blad -- checkConnection -- Blad polaczenia z baza.");
			e.printStackTrace();
			return;
		}
	}
	
/*	private static ResultSet executeQuery(String sql) {
		try {
			return command.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("Blad wykonia komendy:"+sql);
			e.printStackTrace();
		}
		return null;
	}*/
	
	private static void log(String msq)
	{
		System.out.println(msq);
	}
	
	synchronized public static String getQuestionPat(int partNumber, int questionNumber)
	{
		ResultSet results = schowQuestion(questionNumber);
		try {
			if(results.next())

		switch (partNumber) {
		case 0:
			try {
				return "	Question:  "+results.getString(2);
			} catch (SQLException e) {
				log("Blad odczytu czesci: 0");
				e.printStackTrace();
			}
		case 1:
			try {
				return "1. "+results.getString(3);
			} catch (SQLException e) {
				log("Blad odczytu czesci: 1");
				e.printStackTrace();
			}
		case 2:
			try {
				return "2. "+results.getString(4);
			} catch (SQLException e) {
				log("Blad odczytu czesci: 2");
				e.printStackTrace();
			}
		case 3:
			try {
				return "3. "+results.getString(5);
			} catch (SQLException e) {
				log("Blad odczytu czesci: 3");
				e.printStackTrace();
			}
		case 4:
			try {
				return "4. "+results.getString(6);
			} catch (SQLException e) {
				log("Blad odczytu czesci: 4");
				e.printStackTrace();
			}

		default:
			return showAnswer(questionNumber);
		}
		} catch (SQLException e1) {
			System.out.println("Blad odczytu z bazy");
			e1.printStackTrace();
		}
		return null;
	}
	
	synchronized public static ResultSet schowQuestion(int number)
	{
		String sql = "SELECT * FROM `question` WHERE id = "+number;
		ResultSet results = executeComand(sql);
		return results;
	}
	
	synchronized public static String showAnswer (int number)
	{
		String sql = "SELECT correctAnswer FROM `correctanswer` WHERE idQuestion = "+(number);
		ResultSet results = executeComand(sql);
		try {
			if(results.next())

			try {
				return results.getString("correctAnswer");
			} catch (SQLException e) {
				log("Blad odczytu poprawnej odpowiedzi");
				e.printStackTrace();
			}
		} catch (SQLException e) {
			System.out.println("Blad odczytu z bazy ");
			e.printStackTrace();
		}
		return "";
	}
	
//	/*synchronized*/ public static int showAnswer (int number)
//	{
//		String sql = "SELECT correctAnswer FROM correctanswer WHERE id = "+number;
//		ResultSet results = executeComand(sql);
//		
//		try {
//			return results.getInt("correctAnswer");
//		} catch (SQLException e) {
//			log("Blad odczytu poprawnej odpowiedzi");
//			e.printStackTrace();
//		}
//		return 10;
//	}
	
	
	static private ResultSet executeComand(String sql)
	{
		try {
			return command.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("Blad wykonia komendy: "+sql);
			e.printStackTrace();
			
		}
		return null;
	}
	
	static public void sendToDataBase(String userId, int score,int questionNumb)
	{
			 updateDataBase("INSERT INTO `useranswer`( `idUser`, `score`, `questionNumber`)"
						+ " VALUES (" + userId + "," + score + "," + questionNumb + ")");

	}
	
	static public void updateDataBase(String sql)
	{
		 try {
			command.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Blad wstawiania do bazy: "+sql);
			e.printStackTrace();
		}
	}
	
	synchronized public static void init()
	{
		checkConnection();
		System.out.println("MySQL JDBC Driver Registered!");
		connect();
	}
	
	public static void createDataBase(Statement st, String name)
	{
		if (executeUpdate(st, "create Database "+name+";") != -1)
			System.out.println("Baza utworzona");
		else
			System.out.println("Baza nieutworzona!");
		if (executeUpdate(st, "USE "+name+";") != -1)
			System.out.println("Baza wybrana");
		else
			System.out.println("Baza niewybrana!");
		if (executeUpdate(st, "CREATE TABLE `correctanswer` (idQuestion int NOT NULL AUTO_INCREMENT, correctanswer int(1), PRIMARY KEY(idQuestion))") != -1)
			System.out.println("Tabela utworzona");
		else
			System.out.println("Tabela nie utworzona!");
		if (executeUpdate(st, "CREATE TABLE `question` (id int NOT NULL AUTO_INCREMENT, question char(100),answer1 char(100),answer2 char(100),answer3 char(100),answer4 char(100), PRIMARY KEY(id))") != -1)
			System.out.println("Tabela utworzona");
		else
			System.out.println("Tabela nie utworzona!");
		if (executeUpdate(st, "CREATE TABLE `useranswer` (idTransaction int NOT NULL AUTO_INCREMENT, idUser int(6),score int(2),questionNumber int(3),answer3 char(100), PRIMARY KEY(idTransaction))") != -1)
			System.out.println("Tabela utworzona");
		else
			System.out.println("Tabela nie utworzona!");	
	}
	
	public static Connection getConnection(String adres, String port) {
		 
		Connection conn = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + adres + ":" + port + "/", login, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to database");
		return conn;
	}
	
	private static Statement createStatement(Connection connection) {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		;
		return null;
	}
	
	private static int executeUpdate(Statement s, String sql) {
		try {
			return s.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static void main(String[] argv) {

		String dbName = "sbdatabase";

		checkConnection();
		System.out.println("MySQL JDBC Driver Registered!");
		
		Connection con = getConnection(hostName,port);
		Statement st = createStatement(con);
		
		createDataBase(st,dbName);
		
		if (con != null) {
		System.out.println(""+dbName+" Success!!!");		
		} else {
			System.out.println("Failed to make connection!");
		}
		
		try {
			st.close();
			con.close();
		} catch (SQLException e) {
			System.out.println("Blad zmakniecia bazy.");
			e.printStackTrace();
		}
	  }
	
	
	
	
}
