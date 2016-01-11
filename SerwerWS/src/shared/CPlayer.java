package shared;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CPlayer {
	
	private int ID = 0;
	private ObjectInputStream objIn = null;
	private ObjectOutputStream objOut = null;
	
	public CPlayer(int id, ObjectOutputStream out, ObjectInputStream in)
	{
		this.setID(id);
		this.setObjIn(in);
		this.setObjOut(out);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public ObjectInputStream getObjIn() {
		return objIn;
	}

	public void setObjIn(ObjectInputStream objIn) {
		this.objIn = objIn;
	}

	public ObjectOutputStream getObjOut() {
		return objOut;
	}

	public void setObjOut(ObjectOutputStream objOut) {
		this.objOut = objOut;
	}

}
