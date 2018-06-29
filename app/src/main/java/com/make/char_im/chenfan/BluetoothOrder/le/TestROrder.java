package com.make.char_im.chenfan.BluetoothOrder.le;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestROrder {

	protected static String getHexString(int[] b) {
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

	public static byte[] getFromAssets(Context mContext, String fileName) {

		try {
			InputStreamReader inputReader = new InputStreamReader(mContext
					.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
