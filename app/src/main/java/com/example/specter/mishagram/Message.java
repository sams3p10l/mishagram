package com.example.specter.mishagram;

public class Message
{
	private String sender;
	private String receiver;
	private String msg;

	Message(String pSender, String pReceiver, String pmsg)
	{
		this.sender = pSender;
		this.receiver = pReceiver;
		this.msg = pmsg;
	}

	public String getMsg()
	{
		return msg;
	}

	public String getSender()
	{
		return sender;
	}

	public String getReceiver()
	{
		return receiver;
	}
}
