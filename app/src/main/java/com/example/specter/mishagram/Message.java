package com.example.specter.mishagram;

public class Message
{
	private String msg;

	Message(String pmsg)
	{
		msg = pmsg;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
