package serwer;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CScoreGetter<T> extends FutureTask<T>{

    Socket socket;
    public CScoreGetter(Callable<T> vale) {
        super(vale);
    }
    public void done() {
        if(isCancelled()){
            System.out.println("Anulowano");
        }
        else{
            try {
                System.out.println("Ubito watek: "+Thread.currentThread().getName()+" "+get());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        }

    }
}