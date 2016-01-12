package serwer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import shared.CPackage;
import shared.CPlayer;

public class CServer {
//	public static final int PULA_WATKOW = 50;
	public static final int PORT = 2016;
	
	
	
	protected static int port;	// w razie potrzeby podania z palca
	protected static ServerSocket server;
	protected static Scanner klawiatura;
	protected static final boolean runCreationTest = false;
	protected static int ID = 100;
	
//	public static Map<Integer, CSession> Game = new HashMap<Integer, CSession>();
	public static List<CGame>   Game  = new ArrayList<CGame>();
	public static List<CPlayer> Wait  = new ArrayList<CPlayer> ();	
	public static List<CPlayer> Enemy = new ArrayList<CPlayer> ();	

	protected static List<Object> Thread = new ArrayList<Object>();
	
	
	public static synchronized CPlayer popEnemy()
	{
		if(Enemy.size() > 0)
		{
			CPlayer enemy = Enemy.get(Enemy.size() - 1);	// powinno pobrac ostani element
			Enemy.remove(enemy);	// powinno usunac ostani element
			return enemy;
		}
		return new CPlayer();	// nie wiem jak to zadziala
	}
	
	public static synchronized void setGame(CPlayer player1, CPlayer player2)
	{
		ID++;
		Game.add(new CGame(ID,player1,player2));
		
	}
	
	private static void createTest(boolean test)
	{
		if(test)
		{
			CDataBaseControll.updateDataBase("CREATE TABLE test (id int NOT NULL AUTO_INCREMENT, imie char(30), lata char(3), punkty char(3), PRIMARY KEY(id))");
			System.out.println("Utworzenie tabeli test.");
			CDataBaseControll.updateDataBase("TRUNCATE TABLE test");
			System.out.println("Wyczyszczono tabele test.");
			CDataBaseControll.updateDataBase("DROP TABLE test");
			System.out.println("Usunieto tablele test.");
		}
	}
	
	synchronized public static int getID()
	{
		return (ID + port);
	}
	


	public static void main(String args[]) {
		klawiatura = new Scanner(System.in);
		System.out.println("Start servera");
		CDataBaseControll.init();
		createTest(runCreationTest);
		port = PORT;
		if(PORT < 100)
		try {
			System.out.println("Podaj port dla klientow: ");
			 port = klawiatura.nextInt();
			 klawiatura.nextLine();
		} catch (NumberFormatException e) {
			System.err.println("Bledny numer portu: " + e);
			return;
		}
		try {
			server = new ServerSocket(port);
			System.out.println("Serwer na porcie: " + port);
			
			
			while (true) {
				Socket socket = server.accept();
				System.out.println("Polaczono");
				Thread.add(new CPolaczenie(socket));
//				ExecutorService exec = Executors.newFixedThreadPool(PULA_WATKOW);
//				exec.execute(new CScoreGetter<String>((new CPolaczenie(socket))));
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {

			CDataBaseControll.disconnect();
			System.out.println("By by");
			
		}
	}
}
