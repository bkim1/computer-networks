package peer;

import utils.PeerUtils;

public class PeerSendThread implements Runnable {
	private PeerUtils peerU;

	public PeerSendThread(PeerUtils peerU) {
		this.peerU = peerU;
	}

	public void run() {
		System.out.println(Thread.currentThread().getName() + ": Starting up the send thread!" + "\n" +
						   "If you want to leave this group, then enter '/leave' to leave");
		String fromKeyboard = null;
		try {
			while (!(fromKeyboard = this.peerU.readFromKeyboard()).equalsIgnoreCase("/leave")) {
				// System.out.println(Thread.currentThread().getName() + ": Sending");
				this.peerU.sendToSocket(fromKeyboard);
			}
		} catch (Exception E) {  }
	}
}
