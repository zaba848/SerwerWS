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
	private BufferedReader input = null;
	private PrintWriter output = null;
	private ObjectInputStream objIn = null;
	private InputStream inObjStream = null;
	private OutputStream outObjStream = null;
	private ObjectOutputStream objOut = null;
	private CPackage packIn;
	private CPackage packOut;

	private boolean exit = false;


	String ID = null;

	public CPolaczenie(Socket socket) {
		this.socket = socket;
	}

	public void init() {
		exit = false;
		packIn = new CPackage();
		packOut = new CPackage();
		
		try {
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.out.println("Blad tworzenia zaczepu odczytu.");
			e.printStackTrace();
		}
		try {
			output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		} catch (IOException e) {
			System.out.println("Blad tworzenia zaczepu zapisu/wysylania.");
			e.printStackTrace();
		}
		try {
			inObjStream = socket.getInputStream();
			objIn =  new ObjectInputStream(inObjStream);
		} catch (IOException e) {
			System.out.println("Blad tworzenia strumnienia odczytu objektu");
			e.printStackTrace();
		}
		
		 try {
			outObjStream = socket.getOutputStream();
			objOut = new ObjectOutputStream(outObjStream);
		} catch (IOException e) {
			System.out.println("Blad tworzenia strumnienia zapisu objektu");
			e.printStackTrace();
		}

	}
	
	protected void end()
	{
		try {
			objOut.close();
			outObjStream.close();
			objIn.close();
			inObjStream.close();
			output.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Blad zamkniecia polaczenia");
			e.printStackTrace();
		}

	}
	
	protected CPackage readObj()
	{
		try {
			packIn = (CPackage)objIn.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Blad nie znaleziono klasy");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Blad odczytu objektu");
			e.printStackTrace();
		}
		return packIn;
	}
	
	protected void readWrite(CPackage pack)
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
		// jak to zutowac?!
		String data = readed.getData();
		data.replace(";", " ");
		data.replace("CPlayer=", ";");
		data.replace(", input=", ";");
		data.replace(", output=", ";");
		data.trim();
		String rawPlayer[] = data.split(";");
		CPlayer player = new CPlayer((int)rawPlayer[0], (ObjectOutputStream)rawPlayer[1], (ObjectInputStream)rawPlayer[2]);
		CServer.Wait.put(CServer.getID(),player);
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
						
		}break;
		case ENEMY			    :
		{
			
			
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
		packIn.reset();

	}

	@Override
	public String call() throws Exception {
		init();

		{
			CPackage begin = new CPackage();
			while(begin.getCommand() != COMMANDS.BEGIN)
			{
				begin = readObj();
			}
			WhoIAm(begin);
		}
		try {
			while(!exit)
			{
				readObj();
				menu(packIn.getCommand());
			}
			
			
			end();
			
			

		} catch (Exception e) {
			System.out.println("Blad poczlaczenia: " + ID + ", error: " + e);
			e.printStackTrace();
			System.exit(1);
		}
		return "Disconnect: " + ID;
	}

	public CPackage getPackIn() {
		return packIn;
	}

	public void setPackOut(CPackage pack) {
		this.packOut = pack;
	}

}
