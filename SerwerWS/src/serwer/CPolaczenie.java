package serwer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import shared.CCommand.COMMANDS;
import shared.CPackage;
import shared.CPlayer;

public class CPolaczenie implements Callable<String> {


	private Socket socket = null;
	private ObjectInputStream 	objIn = null;
	private ObjectOutputStream 	objOut = null;
	private ObjectInputStream 	fromEnemy = null; 
	private ObjectOutputStream 	toEnemy = null;
	private CPackage packInMy ;
	private CPackage packOutMy;
	private CPackage packInEnemy ;
	private CPackage packOutEnemy;
	private CPlayer my;
	private CPlayer enemy;

	private Boolean addedToWait;
	private int sesionID;


	private boolean exit = false;


	String ID = null;

	public CPolaczenie(Socket socket, int sesionID) {
		this.socket = socket;
		this.sesionID = sesionID;
	}

	public void init() {

		
		try {
			objIn =  new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("Blad tworzenia strumnienia odczytu objektu");
			e.printStackTrace();
		}
		
		 try {
			objOut = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Blad tworzenia strumnienia zapisu objektu");
			e.printStackTrace();
		}

	}
	
	protected void end()
	{
		try {
			objOut.close();
			objIn.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Blad zamkniecia polaczenia");
			e.printStackTrace();
		}

	}
	
	protected CPackage readMyObj()
	{
		try {
			packInMy = (CPackage)objIn.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Blad nie znaleziono klasy");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Blad odczytu objektu");
			e.printStackTrace();
		}
		return packInMy;
	}
	
	protected void writeObj(CPackage pack)
	{
		try {
			objOut.writeObject(pack);
		} catch (IOException e) {
			System.out.println("Blad wysylania objektu");
			e.printStackTrace();
		}
	}
	
	
	protected void WhoIAm(CPackage readed)
	{
		// dodac sprawdzanie z urzytkownikami
//		String data = readed.getData();
//		data.replace(";", " ");
//		data.replace("CPlayer=", " ");
//		data.trim();
//		my.setID( Integer.parseInt(data));
//		int tryNumber = 5;
//		addedToWait = addToWait(my, tryNumber);			// piec razy probuje sie dodac do listy oczekujacych
	}
	
	
	private void updateMy()
	{
		
	}
	
	

	private void menu(COMMANDS comand)
	{
		switch(comand)
		{

		case END 			    :
		{
			exit = true;
		}break;
		case WHO_I_AM		    :
		{
			WhoIAm(readMyObj());			
		}break;
		case ENEMY			    :
		{
			///
			
		}break;
		case GRAME_ID		    :
		{
			
			
		}break;
		case SEND_MOVE		    :
		{
			
			
		}break;
		case GET_MOVE		    :
		{
			
			
		}break;
		case SEND_BATTELFIELD   :
		{
			
			
		}break;
		case GET_BATTELFIELD	: 
		{
			
			
		}break;
		
		
		
		
		
		case BEGIN			    :
		default:
			break;
		}
		packInMy.reset();

	}

	@Override
	public String call() throws Exception {
		init();

		{
			CPackage begin = new CPackage();
			while(begin.getCommand() != COMMANDS.BEGIN)
			{
				begin = readMyObj();
			}
			WhoIAm(begin);
		}
		try {
			while(!exit)
			{
				readMyObj();
				menu(packInMy.getCommand());
			}
			
			
			end();
			
			

		} catch (Exception e) {
			System.out.println("Blad poczlaczenia: " + ID + ", error: " + e);
			e.printStackTrace();
			System.exit(1);
		}
		return "Disconnect: " + ID;
	}

}
