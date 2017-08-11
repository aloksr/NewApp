package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonPostArrayRequest extends JsonRequest<JSONObject> {

	JSONArray params;
	Map<String,String> headers;
	public JsonPostArrayRequest(int method,Map<String,String> headers,String url, JSONArray params,Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST,headers, url, null, listener, errorListener);
		this.params = params;
		this.headers=headers;
	}

	@Override
	public byte[] getBody() {
		if (this.params != null && this.params.length() > 0) {
			return encodeParameters(this.params, getParamsEncoding());
		}
		return null;

	}

	private byte[] encodeParameters(JSONArray params, String paramsEncoding) {
		try {
			return params.toString().getBytes(paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}
}