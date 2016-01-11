package shared;


public class CSession {

	private int ID;
	private CPlayer player1;
	private CPlayer player2;
	
	public CSession(int id, CPlayer player1, CPlayer player2)
	{
		setID(id);
		this.setPlayer1(player1);
		this.setPlayer2(player2);
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public CPlayer getPlayer1() {
		return player1;
	}

	public void setPlayer1(CPlayer player1) {
		this.player1 = player1;
	}

	public CPlayer getPlayer2() {
		return player2;
	}

	public void setPlayer2(CPlayer player2) {
		this.player2 = player2;
	}
	
}
