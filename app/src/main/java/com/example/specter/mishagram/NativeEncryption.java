package com.example.specter.mishagram;

public class NativeEncryption
{
	static{
		System.loadLibrary("NativeEncryption");
	}

	public native String cryption(String input);
}
