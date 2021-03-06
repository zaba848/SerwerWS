package serwer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Watchable;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import shared.CCommand.COMMANDS;
import shared.CPackage;
import shared.CPlayer;

public class CServer {
	public static final int PULA_WATKOW = 20	;	// ograniczenia sprzetowe
	public static final int PORT 		= 2016	;
	
	
	
	protected static ServerSocket server;
	protected static int ID 		= 100;
	protected static int sesionID 	= 100;
	

	protected static final HashMap<Integer, CGame> 	GAME 		= new HashMap<Integer, CGame>	();
	protected static final Vector <CPlayer> 		WAIT_QUEUE	= new Vector<CPlayer>				();
	protected static final HashMap<Integer, Socket> THREADS 	= new HashMap<Integer, Socket>	();

	
	private static synchronized int getSesionID()
	{
		sesionID++;
		return sesionID;
	}
	
	public static synchronized void waitForEnemy(CPlayer player)
	{
		
		WAIT_QUEUE.add(player);
	}
	
	public static synchronized Integer beginGame(CPlayer player)  // dodac jakas ochrone przed fauszywymiwejsciami
	{
		if(WAIT_QUEUE.size() > 0)
		{
			CPlayer enemy = WAIT_QUEUE.firstElement();
			if(enemy != player)
 {
				Integer gameID = startGame(player,WAIT_QUEUE.firstElement());
				// powinno dzialac
				WAIT_QUEUE.firstElement().chat.addElement(new CPackage(COMMANDS.START_GAME,gameID.toString()));
				
				
	/*
				/// bardziej ryzykowna wersja i trzeba by bylo wymyslic komunikacje od strony watku klienta
	*/
//				THREADS.get(WAIT_QUEUE.firstElement().getID());
//				ObjectOutputStream toEnemy = null;;				/// bardziej ryzykowna wersja 
//				try {
//					toEnemy = new ObjectOutputStream(
//							THREADS.get(WAIT_QUEUE.firstElement().getID()).getOutputStream());
//				} catch (IOException e) {
//					System.out.println("Blad servera, nieudana proba stworzenia strumienia do przeciwnika");
//					e.printStackTrace();
//				}
//				try {
//					toEnemy.writeObject(new CPackage(COMMANDS.START_GAME, gameID.toString()));
//				} catch (IOException e) {
//					System.out.println("Blad servera, nieudana proba wyslania swojego ID do pzeciwnika");
//				}
//				THREADS.get(WAIT_QUEUE.firstElement().getID());
				WAIT_QUEUE.remove(0);
				
				
				return gameID;		// jak to zadzia�a to bede spiewa� w pracy
			}
			
		}
		waitForEnemy(player);
		return 10;
	}
	
	public static synchronized CPlayer getGame(CPlayer my, int gameID)
	{
		
		CGame enemy = GAME.get(gameID);
		if(enemy != null)
		if((enemy.getID_1() == my) )	// teoretycznie nie mozliwe, ale...
		{
			return enemy.getID_2();
		}else
		if((enemy.getID_2() == my) )
		{
			return enemy.getID_1();
		}
		return null;
	}
	
	public static synchronized Socket getEnemy(CPlayer my, CPlayer enemy, int gameID)
	{
		CGame game = GAME.get(gameID);
		if(game != null)
		if(((game.getID_1() == my) && (game.getID_2() == enemy)) || ((game.getID_1() == enemy) && (game.getID_2() == my)))
		{
			return THREADS.get(enemy.getID());
		}
		return null;
		
	}
	
	protected static synchronized Integer startGame(CPlayer player1, CPlayer player2)
	{		
		ID++;
		GAME.put(ID+PORT, new CGame(ID+PORT, player1, player2));
		
		// stworz tabele ID+PORT MySQl
		
		return ID+PORT;
	}
	
	
	
	public static synchronized boolean endGame(Integer gameID, CPlayer player1,CPlayer player2 )
	{
		CGame game = new CGame();
		game = GAME.get(gameID);
		if(((game.getID_1() == player1) && (game.getID_2() == player2)) || ((game.getID_1() == player2) && (game.getID_2() == player1)))
		{
			GAME.remove(gameID);
			return true;
		}
		
		return false;
	}

//	private static void createTest(boolean test)
//	{
//		if(test)
//		{
//			CDataBaseControll.updateDataBase("CREATE TABLE test (id int NOT NULL AUTO_INCREMENT, imie char(30), lata char(3), punkty char(3), PRIMARY KEY(id))");
//			System.out.println("Utworzenie tabeli test.");
//			CDataBaseControll.updateDataBase("TRUNCATE TABLE test");
//			System.out.println("Wyczyszczono tabele test.");
//			CDataBaseControll.updateDataBase("DROP TABLE test");
//			System.out.println("Usunieto tablele test.");
//		}
//	}
	


	public static void main(String args[]) {
		System.out.println("Start servera");
		CDataBaseControll.init();

		try {
			server = new ServerSocket(PORT);
			System.out.println("Serwer na porcie: " + PORT);
			
			
			ExecutorService exec = Executors.newFixedThreadPool(PULA_WATKOW);
			while (true) {
//				Socket socket = server.accept();
				int id = getSesionID();
				THREADS.put(id, server.accept());
				System.out.println("Polaczono: "+id);
				exec.execute(new CScoreGetter<String>((new CPolaczenie(THREADS.get(id),id))));
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {

			CDataBaseControll.disconnect();
			System.out.println("By by");
			
		}
	}
}
