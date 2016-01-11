package shared;

public class CCommand {

	public enum COMMANDS {
	 // sterowanie 0 - 999
		BEGIN				(555 ),		// pierwszy komunikat do serwera, po nawiazaniu jest traktowana jako pusta koneda
		END 				(666 ),		// ostatni komunikta / zamkniecie gniazada
		GET_SESJA			(777 ),		// zwraca id sesji
		
	 //rozgrywka 1000 - 3000
		WHO_I_AM			(1000),		// wysyla swoje id	
		ENEMY				(1005),		// pobiera id przeciwnika
		GRAME_ID			(1010),		// pobiera id gry
		SEND_MOVE			(1015),		// wysyla twoj ruch
		GET_MOVE			(1020),		// odbiera ruch przeciwnika
		SEND_BATTELFIELD	(1025),		// wysyla uklad okretow
		GET_BATTELFIELD		(1030);		// pobiera uklad okretow
	 
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
