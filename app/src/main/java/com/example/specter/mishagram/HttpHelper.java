package com.example.specter.mishagram;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpHelper
{
	private Context context;

	private static final int SUCCESS = 200;
	private static final int BAD_RQ = 400;
	private static final int NOT_FOUND = 404;

	HttpHelper(Context pContext)
	{
		this.context = pContext;
	}

	public JSONArray getJSONContacts() throws IOException, JSONException
	{
		URL url = new URL(MainActivity.GET_CONTACT_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		SharedPreferences pref = context.getSharedPreferences("sharedPref", 0);
		String sessionID = pref.getString("cookie", "");

		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("sessionid", sessionID);
		connection.setReadTimeout(10000);
		connection.setConnectTimeout(10000);

		try{
			connection.connect();
		} catch (IOException e) {
			return null;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;

		while((line = br.readLine()) != null)
		{
			sb.append(line).append("\n");
		}

		br.close();
		String jsonString = sb.toString();

		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();

		connection.disconnect();

		if(responseCode == SUCCESS)
		{
			return new JSONArray(jsonString);
		}
		else if (responseCode == BAD_RQ || responseCode == NOT_FOUND || responseCode == 409)
			return new JSONArray(responseMessage);
		else
			return null;

	}

	public JSONArray getJSONMessage(String toWhom) throws IOException, JSONException
	{
		String fullURL = MainActivity.SEND_MSG_URL + "/" + toWhom;
		SharedPreferences pref = context.getSharedPreferences("sharedPref", 0);
		String sessionID = pref.getString("cookie", "");

		URL url = new URL(fullURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("sessionid", sessionID);
		connection.setReadTimeout(10000);
		connection.setConnectTimeout(10000);

		try{
			connection.connect();
		} catch (IOException e) {
			return null;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;

		while((line = br.readLine()) != null)
		{
			sb.append(line).append("\n");
		}

		br.close();
		String jsonString = sb.toString();

		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();

		connection.disconnect();

		if(responseCode == SUCCESS)
		{
			return new JSONArray(jsonString);
		}
		else if (responseCode == BAD_RQ || responseCode == NOT_FOUND || responseCode == 409)
			return new JSONArray(responseMessage);
		else
			return null;
	}

	public String postJSONObject(String urlString, JSONObject jsonToPost) throws IOException
	{
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		connection.setRequestProperty("Accept", "application/json");

		if(urlString.equals(MainActivity.SEND_MSG_URL))
		{
			SharedPreferences pref = context.getSharedPreferences("sharedPref", 0);
			String sessionID = pref.getString("cookie", "");
			connection.setRequestProperty("sessionid", sessionID);
		}

		connection.setDoInput(true);
		connection.setDoOutput(true);

		try{
			connection.connect();
		} catch (IOException e) {
			return "Can't connect to server";
		}

		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

		dos.writeBytes(jsonToPost.toString());
		dos.flush();
		dos.close();

		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();

		if(urlString.equals(MainActivity.LOGIN_URL))
		{
			SharedPreferences.Editor editor = context.getSharedPreferences("sharedPref", 0).edit();
			String cookieHeader = connection.getHeaderField("sessionid");
			editor.putString("cookie", cookieHeader);
			editor.apply();
		}

		if(responseCode == SUCCESS)
		{
			return "successful";
		}
		else if (responseCode == BAD_RQ || responseCode == NOT_FOUND || responseCode == 409)
			return responseMessage;
		else
			return null;
	}

	public boolean postLogout() throws IOException
	{
		URL url = new URL(MainActivity.LOGOUT_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		SharedPreferences pref = context.getSharedPreferences("sharedPref", 0);
		String sessionID = pref.getString("cookie", "");

		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("sessionid", sessionID);

		connection.setDoInput(true);
		connection.setDoOutput(true);

		try{
			connection.connect();
		} catch (IOException e) {
			return false;
		}

		int responseCode = connection.getResponseCode();

		return responseCode == SUCCESS;
	}

	public String deleteMessage(JSONObject jsonToDelete) throws IOException
	{
		URL url = new URL(MainActivity.SEND_MSG_URL);			//DOUBLE CHECK
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		SharedPreferences pref = context.getSharedPreferences("sharedPref", 0);
		String sessionID = pref.getString("cookie", "");

		connection.setRequestMethod("DELETE");
		connection.setRequestProperty("sessionid", sessionID);
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setRequestProperty("Accept","application/json");

		connection.setDoInput(true);
		connection.setDoOutput(true);

		try {
			connection.connect();
		} catch (IOException e) {
			return "Can't connect to server";
		}

		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

		dos.writeBytes(jsonToDelete.toString());
		dos.flush();
		dos.close();

		int responseCode = connection.getResponseCode();
		String responseMsg = connection.getResponseMessage();

		connection.disconnect();

		if(responseCode == SUCCESS)
		{
			return "successful";
		}
		else if (responseCode == BAD_RQ || responseCode == NOT_FOUND || responseCode == 409)
			return responseMsg;
		else
			return null;

	}

	public String newMessageCheck() throws IOException
	{
		URL url = new URL(MainActivity.NOTIFICATION_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		SharedPreferences pref = context.getSharedPreferences("sharedPref", 0);
		String sessionID = pref.getString("cookie", "");

		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("sessionid", sessionID);
		connection.setReadTimeout(10000);
		connection.setConnectTimeout(10000);

		try{
			connection.connect();
		} catch (IOException e) {
			return null;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;

		if((line = br.readLine()) != null) //while
		{
			sb.append(line);
		}

		br.close();
		String serverResponse = sb.toString();

		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();

		connection.disconnect();

		if(responseCode == SUCCESS)
		{
			return serverResponse;
		}
		else if (responseCode == BAD_RQ || responseCode == NOT_FOUND || responseCode == 409)
			return responseMessage;
		else
			return null;

	}
}
