package serwer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CServer {
	public static final int PULA_WATKOW = 50;
	public static final int PORT = 2016;
	
	
	
	protected static int port;
	protected static ServerSocket server;
	protected static Scanner klawiatura;
	protected static final boolean runCreationTest = false;
	
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
	


	public static void main(String args[]) {
		klawiatura = new Scanner(System.in);
		System.out.println("Start servera");
		CDataBaseControll.init();
		createTest(runCreationTest);
		try {
			port = PORT;
//			System.out.println("Podaj port dla klientow: ");
//			 port = klawiatura.nextInt();
//			 klawiatura.nextLine();
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

				ExecutorService exec = Executors.newFixedThreadPool(PULA_WATKOW);
				exec.execute(new CScoreGetter<String>((new CPolaczenie(socket))));
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {

			CDataBaseControll.disconnect();
			System.out.println("By by");
			
		}
	}
}
