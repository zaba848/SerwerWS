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
	private CPackage packIn ;
	private CPackage packOut;
	private CPlayer my;
	private Boolean addedToWait;


	private boolean exit = false;


	String ID = null;

	public CPolaczenie(Socket socket) {
		this.socket = socket;
	}

	public void init() {
		exit = false;
		setPackIn(new CPackage());
		packOut = new CPackage();
		setMy(new CPlayer());
		addedToWait = false;
		
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
			setPackIn((CPackage)objIn.readObject());
		} catch (ClassNotFoundException e) {
			System.out.println("Blad nie znaleziono klasy");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Blad odczytu objektu");
			e.printStackTrace();
		}
		return getPackIn();
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
		data.replace("CPlayer=", " ");
		data.trim();
		my.setID( Integer.parseInt(data));
		int tryNumber = 5;
		addedToWait = addToWait(my, tryNumber);			// piec razy probuje sie dodac do listy oczekujacych
	}
	
	private Boolean addToWait(CPlayer player, int tryNumb)
	{
		if(!CServer.Wait.contains(my))
		{
			CServer.Wait.add(my);
			return true;
			
		}else
		{
			if(tryNumb >= 0)
			{
				tryNumb--;
				addedToWait = addToWait(my,tryNumb);		// zeby zrozumiec rekurencje najpierw trzeba zrozumiec rekurencje
			}
		}
		return false;
	}
	
	protected void enemy()
	{

		int tryNumber = 50;
		addedToWait = addToWait(my, tryNumber);			// piec razy probuje sie dodac do tablicy oczekujacych
	}
	
	private Boolean startBattle(CPlayer id1, int addTry)
	{
		CPlayer enemy = CServer.popEnemy();
		if(enemy != new CPlayer())
			{
				CServer.setGame(my, enemy);
			}
		else
		{
			CServer.Enemy.add(my);
		}
		
		return false;
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
			WhoIAm(readObj());			
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
		getPackIn().reset();

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
				menu(getPackIn().getCommand());
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
	
	public CPlayer getPackOut() {
		return my;
	}

	public CPlayer getMy() {
		return my;
	}

	public void setMy(CPlayer my) {
		this.my = my;
	}

	public void setPackIn(CPackage packIn) {
		this.packIn = packIn;
	}

}
