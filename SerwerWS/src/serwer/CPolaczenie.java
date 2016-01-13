package serwer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import shared.CCommand.COMMANDS;
import shared.CPackage;
import shared.CPlayer;

public class CPolaczenie implements Callable<String> {


	private Socket socket 					= null			;
	private Socket enemySoc 				= null			;
	private ObjectInputStream 	objIn 		= null			;
	private ObjectOutputStream 	objOut 		= null			;
	private ObjectInputStream 	fromEnemy 	= null			; 
	private ObjectOutputStream 	toEnemy 	= null			;
	private CPackage packInMy 				= new CPackage();
	private CPackage packOutMy				= new CPackage();
	private CPackage packInEnemy 			= new CPackage();
	private CPackage packOutEnemy			= new CPackage();
	private CPlayer my						= new CPlayer()	;
	private CPlayer enemy					= new CPlayer()	;

	private Boolean addedToWait 			= false			;
	private Boolean exit 					= false			;
	private int sesionID 					= 0				;
	private int gameID						= 0				;
	private String ID 						= null			;

	
	
	
	public CPolaczenie(Socket socket, int sesionID) {
		this.socket = socket;
		this.setSesionID(sesionID);
	}

	protected void init() {
			
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
	
	protected void beginGame()
	{
		setGameID(CServer.beginGame(my));
		if(getGameID() > 10)
		{
			enemySoc = CServer.getEnemy(my, CServer.getGame(my, getGameID()), getGameID());
			if(enemySoc != null)
			{
				try {
						fromEnemy 	= new ObjectInputStream(enemySoc.getInputStream());
						toEnemy 	= new ObjectOutputStream(enemySoc.getOutputStream());
					} catch (IOException e)
					{
						System.out.println("Blad laczenia z przeciwnikiem");
						e.printStackTrace();
					}
				
			}
			System.out.println("Blad pobierania gniazda przeciwnika");
			
		}else
		{
			boolean exit = false;
			while(!exit)
			{
				packInMy = readChat();
				if(packInMy != null)
				{
					if(packInMy.getCommand() == COMMANDS.START_GAME)
					{
						gameID = Integer.parseInt(packInMy.getData());
						exit = true;
					}
				}
			}
			// sluchaj polaczenia od wroga
		}
	}
	
	protected CPackage readChat()
	{
		if(my.chat.size() > 0)
		return my.chat.remove(0);
		return null;
	}
	
	protected void sendToEnemy(CPackage msgToEnemy)
	{
		try {
			toEnemy.writeObject(msgToEnemy);
		} catch (IOException e) {
			System.out.println("Blad wysylania objektu do przeciwnika");
			e.printStackTrace();
		}
	}
	protected CPackage getFromEnemy()
	{
		try {
			packInEnemy = (CPackage)fromEnemy.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Blad nie znaleziono klasy");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Blad odczytu objektu od przeciwnika");
			e.printStackTrace();
		}
		return packInEnemy;
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
	
	
	protected void updateMy()
	{
		
	}
	
	

	protected void menu(COMMANDS comand)
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

	public int getSesionID() {
		return sesionID;
	}

	public void setSesionID(int sesionID) {
		this.sesionID = sesionID;
	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

}
