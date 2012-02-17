import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;



public class Remote {

	private SerialPort port;
	private OutputStream outputStream;

	public Remote(String portname) throws UnsupportedCommOperationException, IOException, PortInUseException, NoSuchPortException, TooManyListenersException{
		port = (SerialPort)CommPortIdentifier.getPortIdentifier(portname).open("iPov",2000);
		port.setSerialPortParams(115200, 8, 1, 0);
		port.notifyOnDataAvailable(true);
		outputStream = port.getOutputStream();

		port.addEventListener(new SerialPortEventListener(){

			@Override
			public void serialEvent(SerialPortEvent arg0) {
				if(arg0.getEventType() == SerialPortEvent.DATA_AVAILABLE){
					byte[] in = read();
					System.out.print(new String(in));
				}
			}

		});
	}
	
	public byte[] read() {
		byte[] buffer = new byte[1024];
		try {
			int len = port.getInputStream().read(buffer);
			byte[] tmp = new byte[len];
			System.arraycopy(buffer,0,tmp,0,len);
			port.getInputStream().close();
			return tmp;
		} catch (IOException e) {
			e.printStackTrace();
		
		}
		return new byte[0]; 
	}
	
	public void writeToPort(byte[] data) throws IOException {
		outputStream.write(data);
	}

	public void writeToPort(String string) throws IOException {
		writeToPort(string.getBytes());		
	}
}
