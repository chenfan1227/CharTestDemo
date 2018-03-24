package com.ccy.lnb.en.BluetoothOrder.le;

import android.content.Context;

import java.io.UnsupportedEncodingException;

class BaseCommandSender {
	protected static final char startCommand = '@';

	/**
	 * 瀛楃瀛楄妭鏁版嵁杞�16杩涘埗
	 * 
	 * @param b
	 * @return
	 */
	protected String getHexString(byte[] b) {
		String hexs = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xff);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexs = hexs + " " + hex;
		}

		return hexs;
	}

	protected String getHexString(int[] b) {
		String hexs = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i]);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexs = hexs + hex;
		}

		return hexs;
	}

	/**
	 * 妫�楠岀爜鐢熸垚鏂规硶
	 * 
	 * @param buffer
	 * @return
	 */
	protected byte CRC8(byte[] buffer) {
		byte crc = 0;
		for (int j = 1; j < buffer.length - 1; j++) {
			crc ^= buffer[j];
			for (int i = 0; i < 8; i++) {
				if ((crc & 0x01) != 0) {
					crc >>= 1;// 鍙樹负3
					crc ^= 0x8c;
				} else {
					crc >>= 1;// 鍙樹负3
				}
			}
		}
		return crc;
	}

	protected int CRC8(int[] buffer) {
		int crc = 0;
		for (int j = 1; j < buffer.length - 1; j++) {
			crc ^= buffer[j];
			for (int i = 0; i < 8; i++) {
				if ((crc & 0x01) != 0) {
					crc >>= 1;// 鍙樹负3
					crc ^= 0x8c;
				} else {
					crc >>= 1;// 鍙樹负3
				}
			}
		}
		return crc;
	}
}

public class DriverMessageCommandSender extends BaseCommandSender {

	private static final char ADCommand = 'L';

	private static final char ADCommandR = 'R';

	private int adTimeLong;
	private String AdContent;

	public DriverMessageCommandSender() {
	}

	public DriverMessageCommandSender(int adTimeLong, String message) {
		this.adTimeLong = adTimeLong;
		this.AdContent = message;
	}

	public static void main(String[] args) {

		DriverMessageCommandSender sender = new DriverMessageCommandSender(8,
				"啊");

		String commandLog = sender.buildCommandPackage(sender.getAdContent());

		System.out.println(commandLog);
	}

	public String getRByte(Context c, int o) {
		int leng = 106;

		int[] cmdByteArray = new int[leng];

		cmdByteArray[0] = (int) startCommand;
		cmdByteArray[1] = 102;

		cmdByteArray[2] = (int) ADCommandR;

		if (o > 256) {
			int byteFirst = o / 256;
			int byteSecond = o % 256;
			cmdByteArray[3] = ((byte) byteFirst) & 0xff;
			cmdByteArray[4] = ((byte) byteSecond) & 0xff;
		} else {
			cmdByteArray[3] = (byte) 0;
			cmdByteArray[4] = ((byte) o) & 0xff;
		}

		byte[] b = TestROrder.getFromAssets(c, "Zhukong.bin");

		for (int i = 0; i < 100; i++) {
			cmdByteArray[i + 6] = b[i] & 0xff;
		}
		cmdByteArray[105] = CRC8(cmdByteArray);

		return TestROrder.getHexString(cmdByteArray);
	}

	// 发送L指令
	public String buildCommandPackage(String inputAdMessage) {
		String command = "";
		int len;
		try {

			len = AdContent.getBytes("GBK").length;

			int dataLength = len + 6;
			// byte[] cmdByteArray = new byte[dataLength]; // @ Length L 00 24
			// 浣�
			// 濂� 1 CRC
			int[] cmdByteArray = new int[dataLength];

			cmdByteArray[0] = (int) startCommand;
			cmdByteArray[1] = 2 + len;
			cmdByteArray[2] = (int) ADCommand;

			// 杞崲鏃堕暱
			if (adTimeLong > 256) {
				int byteFirst = adTimeLong / 256;
				int byteSecond = adTimeLong % 256;
				cmdByteArray[3] = ((byte) byteFirst) & 0xff;
				cmdByteArray[4] = ((byte) byteSecond) & 0xff;
			} else {
				cmdByteArray[3] = (byte) 0;
				cmdByteArray[4] = ((byte) adTimeLong) & 0xff;
			}

			// 杞崲骞垮憡鍐呭
			byte[] tempContent = inputAdMessage.getBytes("GBK");

			int[] intContent = transfer(tempContent);

			int contentLength = tempContent.length;
			System.arraycopy(intContent, 0, cmdByteArray, 5, contentLength);

			cmdByteArray[contentLength + 5] = CRC8(cmdByteArray);

			command = getHexString(cmdByteArray);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return command;
	}

	private int[] transfer(byte[] tempContent) {
		// TODO Auto-generated method stub
		int[] result = new int[tempContent.length];
		for (int i = 0; i < tempContent.length; i++) {
			result[i] = tempContent[i] & 0xff;
		}
		return result;
	}

	public int getAdTimeLong() {
		return adTimeLong;
	}

	public void setAdTimeLong(int adTimeLong) {
		this.adTimeLong = adTimeLong;
	}

	public String getAdContent() {
		return AdContent;
	}

	public void setAdContent(String adContent) {
		AdContent = adContent;
	}

}
