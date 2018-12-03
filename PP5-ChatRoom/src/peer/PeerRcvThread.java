package peer;

import utils.PeerUtils;

public class PeerRcvThread implements Runnable {
    private PeerUtils peerU;
    
	public PeerRcvThread(PeerUtils peerU){
		this.peerU = peerU;
    }
    
	public void run(){
		String fromSocket = null;
		try {
			while (true) {
				fromSocket = this.peerU.readFromSocket();
				// System.out.println(Thread.currentThread().getName() + ": Receiving");
				this.peerU.sendToTerminal(fromSocket);
			}
		} catch (Exception E) {  }
	}	
}
