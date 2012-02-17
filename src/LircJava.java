import java.util.List;
import java.util.Scanner;

public class LircJava {

	private static Remote remote;
	private static RemoteDescription remoteDescription;

	public static void main(String[] args) throws Exception {
		remoteDescription = new RemoteDescription();
		remoteDescription = new RemoteLoader().load("/Users/andreasrettig/Desktop/remotes/apple/A1294");
//		remoteDescription = new RemoteLoader().load("/Users/andreasrettig/Desktop/remotes/panasonic/N2QAYB000329");
		remote = new Remote("/dev/tty.usbserial-A8004Zfe");
		Thread.sleep(1000);
		System.out.println("++++++");
		for (String s:remoteDescription.codes.keySet()){
			System.out.println(s);
		}
		Scanner sc = new Scanner(System.in);
		while (true) {
			String b = sc.nextLine();
			if (b.equals("")) {
				send("DOWN");
			} else  {
				send(b);
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
