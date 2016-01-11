package serwer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CKlient {

    public static Socket socket ;
    public static Scanner klawiatura ;
    public static BufferedReader input ;
    public static PrintWriter output ;
    public static String NIUStudenta ;
	
	
	
	private static void log(String msg)
	{
		System.out.println(msg);
	}
	
	private static void connect()
	{
		klawiatura = new Scanner(System.in);
		int port = 1234;	// default for fast testing
		String serwerName = "149.202.59.135";
		try {
			log("Podaj nazwe serwera");
			//serwerName = klawiatura.nextLine();
			log("Podaj port oczekujacy");
			//port = klawiatura.nextInt();
			//klawiatura.nextLine(); 		// buff cleaning
		} catch (NumberFormatException e) {
			log("Blad, podaj port" + e);
			return;
		}
		try {
//			byte[] ipAddr = new byte[]{127, 0, 0, 1};
			socket = new Socket(serwerName, port);
			log("Klient podlaczony");
		} catch (Exception e) {
			System.err.println(e);
		}

	}
	
	public static void disconnect()
	{
		try {
			socket.close();
		} catch (IOException e) {
			log("To many question... User Colosed.");
			e.printStackTrace();
		}
	}
		
		public static void init()
		{
			
		
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			log("Blad tworzenia bufora odczytu "+e);
			e.printStackTrace();
		}
		try {
			output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		} catch (IOException e) {
			log("Blad tworzenia bufora zapisu "+e);
			e.printStackTrace();
		}

		}
		
		public static String read()
		{String readed = null;
			try {
				readed = input.readLine();
//				System.out.println("readed: "+readed);
				return readed;
			} catch (IOException e) {
				log("Blad odczytu przez klienta "+e);
				e.printStackTrace();
			}
			return readed;
		}
	public static void main(String args[]) {

		int max_numb = 5;
		connect();
		init();
		String readed = read();
		log(readed);
		String dane = klawiatura.nextLine();
		output.println(dane);
		int iterator = 0;
		while (max_numb>=0) 
		{
			for (iterator = 0; iterator < 6; iterator++) {
				log(read());
		}

			dane = klawiatura.nextLine();
			output.println(dane);
			max_numb--;
		}
				
		disconnect();
	}
}
