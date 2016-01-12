package shared;

import java.util.Vector;

public class CPlayer {
	
	private int ID = 0;
	public Vector<CPackage> chat = null;	

	
	public CPlayer(int id/*, ObjectOutputStream out, ObjectInputStream in*/)
	{
		this.setID(id);
		chat = new Vector<CPackage> ();
	}
	
	public CPlayer()
	{
		this.setID(0);
		chat = new Vector<CPackage> ();

	}
	
	@Override
	public String toString() {
		return "CPlayer=" + getID() + /*", input=" + getObjIn() + ", output=" + getObjOut() + */";";
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
