package com.example.specter.mishagram;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper
{
	private static final int SUCCESS = 200;
	private static final int BAD_RQ = 400;
	private static final int NOT_FOUND = 404;

	public JSONArray getJSONArray(String urlString) throws IOException, JSONException
	{
		//da l treba init connectiona na null?
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
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

		Log.d("HTTP GET", "JSON data- " + jsonString);

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

	public JSONObject getJSONObject(String urlString) throws IOException, JSONException
	{
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
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

		//Log.d("HTTP GET", "JSON data- " + jsonString);

		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();

		connection.disconnect();

		if(responseCode == SUCCESS)
		{
			return new JSONObject(jsonString);
		}
		else if (responseCode == BAD_RQ || responseCode == NOT_FOUND || responseCode == 409)
			return new JSONObject(responseMessage);
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

		Log.i("STATUS", String.valueOf(connection.getResponseCode()));
		Log.i("MSG" , connection.getResponseMessage());

		if(responseCode == SUCCESS)
		{
			return "successful";
		}
		else if (responseCode == BAD_RQ || responseCode == NOT_FOUND || responseCode == 409)
			return responseMessage;
		else
			return null;
	}
}
