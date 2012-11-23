package org.wzy.http;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.wzy.utils.AppLogger;

import android.util.Log;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.HttpStatus;
import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.StatusLine;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.CookieStore;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.HttpRequestRetryHandler;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;
import ch.boye.httpclientandroidlib.client.protocol.ClientContext;
import ch.boye.httpclientandroidlib.client.utils.URIBuilder;
import ch.boye.httpclientandroidlib.conn.ConnectTimeoutException;
import ch.boye.httpclientandroidlib.conn.scheme.PlainSocketFactory;
import ch.boye.httpclientandroidlib.conn.scheme.Scheme;
import ch.boye.httpclientandroidlib.conn.scheme.SchemeRegistry;
import ch.boye.httpclientandroidlib.conn.ssl.SSLSocketFactory;
import ch.boye.httpclientandroidlib.impl.client.BasicCookieStore;
import ch.boye.httpclientandroidlib.impl.client.DecompressingHttpClient;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.client.cache.CacheConfig;
import ch.boye.httpclientandroidlib.impl.client.cache.CachingHttpClient;
import ch.boye.httpclientandroidlib.impl.conn.PoolingClientConnectionManager;
import ch.boye.httpclientandroidlib.params.CoreProtocolPNames;
import ch.boye.httpclientandroidlib.params.HttpConnectionParams;
import ch.boye.httpclientandroidlib.protocol.BasicHttpContext;
import ch.boye.httpclientandroidlib.protocol.HttpContext;
import ch.boye.httpclientandroidlib.util.EntityUtils;

public class HttpUtility {
	private static HttpUtility httpUtility = new HttpUtility();
    private HttpClient httpClient = null;
    
    private HttpUtility() {
    	SchemeRegistry schemeRegistry = new SchemeRegistry();
    	schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(
                new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        
        PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(schemeRegistry);
        connectionManager.setMaxTotal(9);
        
        DefaultHttpClient backend = new DefaultHttpClient(connectionManager);
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

            public boolean retryRequest(
                    IOException exception,
                    int executionCount,
                    HttpContext context) {
                if (executionCount >= 5) {
                    // Do not retry if over max retry count
                    return false;
                }
                return  true;
            }
        };
        backend.setHttpRequestRetryHandler(myRetryHandler);
        
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setMaxCacheEntries(1000);
        cacheConfig.setMaxObjectSize(8192);
        
        httpClient = new CachingHttpClient(new DecompressingHttpClient(backend), cacheConfig);
        HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
        HttpConnectionParams.setSoTimeout(httpClient.getParams(), 8000);
        
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
    }
    
    public static HttpUtility getInstance() {
        return httpUtility;
    }
    
	public String executeNormalTask(HttpMethod httpMethod, String url) {
		switch (httpMethod) {
		case Get:
			return doGet(url);
		}
		return "";
	}
	
	private String doGet(String url) {
		HttpGet httpGet = new HttpGet();
		URIBuilder uriBuilder;
		
		try {
			uriBuilder = new URIBuilder(url);
			httpGet.setURI(uriBuilder.build());
			
		} catch (URISyntaxException e) {
			AppLogger.w("doGet " + e.getMessage());
		}
		
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		HttpResponse response = getHttpResponse(httpGet, localContext);
		
		if (response != null) {
            String result = handleResponse(response);
            httpGet.releaseConnection();
            return result;
        } else {
            httpGet.abort();
            httpGet.releaseConnection();
            return "";
        }
	}
	
	private HttpResponse getHttpResponse(HttpRequestBase httpRequest, HttpContext localContext) {
		HttpResponse response = null;
		try {
            if (localContext != null) {
                response = httpClient.execute(httpRequest, localContext);
            } else {
                response = httpClient.execute(httpRequest);
            }
        } catch (ConnectTimeoutException e) {
        	AppLogger.w("getHttpResponse " + e.getMessage());
        	httpRequest.abort();
        } catch (ClientProtocolException e) {
        	AppLogger.w("getHttpResponse " + e.getMessage());
        	httpRequest.abort();
        } catch (IOException e) {
        	AppLogger.w("getHttpResponse " + e.getMessage());
        	httpRequest.abort();
        }
		return response;
	}
	
	private String handleResponse(HttpResponse httpResponse) {
		StatusLine status = httpResponse.getStatusLine();
		int statusCode = status.getStatusCode();
		
		if (statusCode != HttpStatus.SC_OK) {
			Log.w("wzy1", "handleResponse error");
			return handleError(httpResponse);
		}
		
		return readResult(httpResponse);
	}
	
	private String readResult(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		String result = "";
		try {
            result = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (IOException e) {
        	AppLogger.w(e.getMessage());
        }

		return result;
	}
	
	private String handleError(HttpResponse httpResponse) {
		StatusLine status = httpResponse.getStatusLine();
        int statusCode = status.getStatusCode();
        
        String result = "";
        if (statusCode != HttpStatus.SC_OK) {
        	result = readResult(httpResponse);
        	String err = null;
            int errCode = 0;
            try {
                JSONObject json = new JSONObject(result);                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        return result;
	}
}
