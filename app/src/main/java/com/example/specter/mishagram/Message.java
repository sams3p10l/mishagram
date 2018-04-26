package com.example.specter.mishagram;

public class Message
{
	private int id;
	private String sender;
	private String receiver;
	private String msg;

	Message(int pID, String pSender, String pReceiver, String pmsg)
	{
		this.id = pID;
		this.sender = pSender;
		this.receiver = pReceiver;
		this.msg = pmsg;
	}

	public int getID()
	{
		return id;
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
