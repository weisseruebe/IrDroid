import java.util.List;
import java.util.Scanner;

public class LircJava {

	private static Remote remote;
	private static RemoteDescription remoteDescription;

	public static void main(String[] args) throws Exception {
		remoteDescription = new RemoteDescription();
		remoteDescription = new RemoteLoader().load("/Users/andreasrettig/Desktop/remotes/apple/A1294");
		
		remote = new Remote("/dev/tty.usbserial-A8004Zfe15");
		Thread.sleep(1000);
		System.out.println("++++++");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String b = sc.nextLine();
			if (b.equals("a")) {
				send("chplus");
			} else if (b.equals("s")) {
				remote.writeToPort("send " + 99 + "\r");
			} else {
				send("UP");
			}

		}
	}

	private static void send(String string) throws Exception {
		List<Integer> pulses = remoteDescription.getPulses(string);
		System.out.println("Send "+pulses.size());
		int i = 0;
		for (Integer puls : pulses) {
			remote.writeToPort("d " + (i++) + " " + puls + "\r");
			Thread.sleep(2);
		}
		remote.writeToPort("send " + pulses.size() + "\r");
	}

}
