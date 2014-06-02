package pl.edu.agh.services;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

public class LocationSearchingService extends AsyncTask<String, Void, LatLng> {

	private String address;
	
	public LocationSearchingService(String address) {
		this.address = address;
	}
	
	@Override
	protected LatLng doInBackground(String... params) {
		JSONObject json = getLocationInfoByAddress(address);
		return getLatLngFromJSON(json);
	}
	
	private JSONObject getLocationInfoByAddress(String address) {
		StringBuilder builder = new StringBuilder();
		try {
			address = address.replaceAll(" ", "%20");
			HttpPost httpPost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false%22");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while((b = stream.read()) != -1) {
				builder.append((char) b);
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(builder.toString());
		} catch(JSONException ex) {
			ex.printStackTrace();
		}
		return jsonObject;
	}
	
	private LatLng getLatLngFromJSON(JSONObject jsonObject) {
		double longitude = 0.0;
		double latitude = 0.0;
		try {
			longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");
			latitude= ((JSONArray)jsonObject.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");
		} catch(JSONException ex) {
			ex.printStackTrace();
		}
		return new LatLng(latitude, longitude);
	}
	
}
