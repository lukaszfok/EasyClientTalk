import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;




public class EasyTalkServer extends EasyClientTalk {

	ArrayList strumienieWyjsciowe;
	
	public class ObslugaKlientow implements Runnable {
		
	BufferedReader czytelnik;
	Socket gniazdo;
	
	public ObslugaKlientow(Socket clientSocket) {
		try {
			gniazdo = clientSocket;
			InputStreamReader isReader = new InputStreamReader(gniazdo.getInputStream());
			czytelnik = new BufferedReader(isReader);
			} catch(Exception ex) {ex.printStackTrace();}
		} // koniec konstruktora
	
	public void run() {
		String wiadomosc;
		try {
			while ((wiadomosc = czytelnik.readLine()) != null) {
					System.out.println("Odczytano: " + wiadomosc);
					rozeslijDoWszystkich(wiadomosc);
			} // koniec pÚtli
			} catch(Exception ex) {ex.printStackTrace();}
		} // koniec metody
	} // koniec klasy wewnÚtrznej
	
	
	public static void main(String[] args) {
		new EasyTalkServer().doRoboty();
	}
	
	public void doRoboty() {
			strumienieWyjsciowe = new ArrayList();
				try {
					ServerSocket serverSock = new ServerSocket(5000);
					while(true) {
						Socket gniazdoKlienta = serverSock.accept();
						PrintWriter pisarz = new PrintWriter(gniazdoKlienta.getOutputStream());
						strumienieWyjsciowe.add(pisarz);
						Thread t = new Thread(new ObslugaKlientow(gniazdoKlienta));
						t.start();
						System.out.println("mamy poïÈczenie");
					}
					} catch(Exception ex) {
						ex.printStackTrace ();
					}
	} // koniec metody

	public void rozeslijDoWszystkich(String message) {
			Iterator it = strumienieWyjsciowe.iterator();
			while(it.hasNext()) {
				try {
					PrintWriter pisarz = (PrintWriter) it.next();
					pisarz.println(message);
					pisarz.flush();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			} // koniec pÚtli
		} // koniec metody
}
