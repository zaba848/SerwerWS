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

import shared.CCommand;
import shared.CCommand.COMMANDS;
import shared.CPackage;

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
	
	protected void readObj()
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
	
	
	protected void WhoIAm()
	{
		CServer.Game.p
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
		while(packIn.getCommand() != COMMANDS.BEGIN)
		{
			readObj();
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
