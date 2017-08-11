package com.android.volley.toolbox;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonObjectRequestArrayResponse extends JsonRequest<JSONArray> {

	JSONObject params;
	private final Map<String, String> headers;
	public JsonObjectRequestArrayResponse(int method,Map<String, String> headers,String url, JSONObject params,Response.Listener<JSONArray> listener,
			Response.ErrorListener errorListener) {
		super(Method.POST, headers,url, null, listener, errorListener);
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

	private byte[] encodeParameters(JSONObject params, String paramsEncoding) {
		try {
			return params.toString().getBytes(paramsEncoding);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
		}
	}

	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}
}