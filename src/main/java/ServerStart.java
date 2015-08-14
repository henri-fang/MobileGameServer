import com.game.server.MainServer;

public class ServerStart {

	public static void main(String[] args) {
		new Thread(new MainServer()).start();
	}
}
