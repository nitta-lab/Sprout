package framework.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.os.AsyncTask;

abstract public class HttpAsyncConnection extends AsyncTask<Void, Void, String> {

	private HttpURLConnection conn;
	private StringBuffer message = new StringBuffer();
	protected Activity activity = null;
	private String method;
	private static String clientSessionID = null;

	public HttpAsyncConnection(String url) {
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public HttpAsyncConnection(String url,Activity activity) {
		this(url);
		this.activity = activity;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public void doPost() {
		setMethod("POST");
		execute();
	}
	
	public void doGet() {
		setMethod("GET");
		execute();
	}
	
	public void doPut() {
		setMethod("PUT");
		execute();
	}
	
	public void doDelete() {
		setMethod("DELETE");
		execute();
	}

	private void setMethod(String method) {
		this.method = method;
	}
	
	@Override
	protected String doInBackground(Void... urls) {
		doAnything();
		return doReceive();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	public void onPostExecute(String response) {
		receive(response);
	}

	/**
	 * サーバからの応答を受信する
	 * @param response サーバからの応答
	 */
	abstract protected void receive(String response);

	// request
	public void doAnything() {
		try {
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			// POST or GET or PUT or DELETE
			conn.setRequestMethod(method);

			conn.setDoOutput(true);
			if(clientSessionID != null) {
				conn.setRequestProperty("Cookie", clientSessionID);
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
			writer.write(message.toString());
			message.delete(0,message.length());
			writer.flush();
			writer.close();


			if(clientSessionID == null ) {
				clientSessionID = conn.getHeaderField("Set-Cookie");

			}
		} catch (IOException e) {
			notConnection();
			e.printStackTrace();
		}
	}

	// response
	public String doReceive() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuilder builder = new StringBuilder();
			while((line = reader.readLine()) != null)
				builder.append(line);
			reader.close();
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void notConnection(){};

	/**
	 *  param set
	 * @param key
	 * @param value
	 */
	public void addParam(String key, String value) {
		if(message.length() > 0) {
			message.append("&");
		}
		message.append(key+"="+value);
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;


	}
}
