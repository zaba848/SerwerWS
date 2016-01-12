package shared;

public class CCommand {

	public enum COMMANDS {
	 // sterowanie 0 - 999
		BEGIN				(111 ),		// pierwszy komunikat do klienta po odebraniu id i dodaniu do "zalogowanych"
		END 				(222 ),		// ostatni komunikta / zamkniecie gniazada
		GET_SESJA			(333 ),		// zwraca id sesji
		START_GAME			(444 ),
		END_GAME			(555 ),
		
	 //rozgrywka 1000 - 3000
		WHO_I_AM			(1000),		// do wysylania swojego id na poczatku sesji
		ENEMY				(1005),		// pobiera id przeciwnika
		GRAME_ID			(1010),		// pobiera id gry
		SEND_MOVE			(1015),		// wysyla twoj ruch
		GET_MOVE			(1020),		// odbiera ruch przeciwnika
		SEND_BATTELFIELD	(1025),		// wysyla uklad okretow
		GET_BATTELFIELD		(1030);		// pobiera uklad okretow
		
//		komunikacja
		
		
	 
	    int ID;
	 
	    private COMMANDS(int comandID) {
	        ID = comandID;
	    }
	 
	    @Override
	    public String toString() {
	        String str = super.toString();
	        return str;
	    }
	}
}
