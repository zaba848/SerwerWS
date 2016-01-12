package serwer;

import java.util.Vector;

import shared.CPackage;
import shared.CPlayer;

public class CGame implements Comparable<CGame>{



	private CPlayer ID_1 = null;
	private CPlayer ID_2 = null;
	private int ID_GAME = 0;
	
	public CGame(int idgame, CPlayer id1,CPlayer id2)
	{
		this.setID_1(id1);
		this.setID_2(id2);
		this.setID_GAME(idgame);
	}
	
	public CGame()
	{
		this.setID_1(null);
		this.setID_2(null);
		this.setID_GAME(0);
	}
	
	@Override
	public String toString() {
		return "CGame=" + getID_GAME() + ", ID_1=" + getID_1() + ", ID_2=" + getID_2() + ";";
	}

	public CPlayer getID_1() {
		return ID_1;
	}

	public void setID_1(CPlayer iD_1) {
		ID_1 = iD_1;
	}

	public CPlayer getID_2() {
		return ID_2;
	}

	public void setID_2(CPlayer iD_2) {
		ID_2 = iD_2;
	}

	public int getID_GAME() {
		return ID_GAME;
	}

	public void setID_GAME(int iD_GAME) {
		ID_GAME = iD_GAME;
	}

	@Override
	public int compareTo(CGame o) {

	return ID_GAME - o.ID_GAME;
			
		

		
	}


}
