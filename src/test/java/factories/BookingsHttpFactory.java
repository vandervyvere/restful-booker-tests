package factories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import properties.BookingProperties;
import models.BookingIdModel;
import models.BookingModel;
import models.CredentialModel;
import models.GenericHttpResponseModel;
import models.TokenModel;

public class BookingsHttpFactory {
	

	private ObjectMapper _objectMapper = new ObjectMapper();
	private BookingsDataFactory _bookingDataFactory = new BookingsDataFactory();
	   
	public TokenModel getAuthToken() throws UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException {
	
		CredentialModel credentials = _bookingDataFactory.getLoginDetails();
				
		GenericHttpResponseModel authresponse = postRequest(credentials, BookingProperties.AUTH_URL, null);		
		TokenModel token = _objectMapper.readValue(authresponse.httpResponseBody, TokenModel.class);
				
		return token;
	}
	
	public GenericHttpResponseModel getRequest(String requestUrl, String queryParameters, String authType) throws ClientProtocolException, IOException {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		if(authType == BookingProperties.AUTH_TYPE_COOKIE) {
			
			httpClient = HttpClientBuilder
					.create()
					.setDefaultCookieStore(
							getCoockieStore(getAuthToken().token)
							)
					.build();
		}
		
		GenericHttpResponseModel genericHttpResonseModel = new GenericHttpResponseModel();
		
		if(queryParameters != null) {
			requestUrl += queryParameters;
		}
		
		HttpGet httpGet = new HttpGet(requestUrl);
		RequestConfig requestConfig = RequestConfig.custom().build();		

		httpGet.setConfig(requestConfig);
		httpGet.addHeader("Content-Type", "application/json");
		httpGet.addHeader("Accept", "*/*");

		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
		
		String response = httpResponseReader(httpResponse);
		
		genericHttpResonseModel.statusCode = httpResponse.getStatusLine().getStatusCode();
		genericHttpResonseModel.reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
		genericHttpResonseModel.httpResponseBody = response;
		
		httpResponse.close();
		
		return genericHttpResonseModel;
		
	}

	/**
	 * 
	 * @param requestBody
	 * @param requestUrl
	 * @return
	 * @throws JsonProcessingException
	 * @throws UnsupportedCharsetException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws UnsupportedOperationException
	 */
	public GenericHttpResponseModel postRequest(Object requestBody, String requestUrl, String authType) throws JsonProcessingException,
			UnsupportedCharsetException, IOException, ClientProtocolException, UnsupportedOperationException {
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		if(authType == BookingProperties.AUTH_TYPE_COOKIE) {
			
			httpClient = HttpClientBuilder
					.create()
					.setDefaultCookieStore(
							getCoockieStore(getAuthToken().token)
							)
					.build();
		}
		
		GenericHttpResponseModel genericHttpResonseModel = new GenericHttpResponseModel();
		
		HttpPost httpPost = new HttpPost(requestUrl);
		RequestConfig requestConfig = RequestConfig.custom().build();		

		
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("Accept", "*/*");

		StringEntity entity = new StringEntity(_objectMapper.writeValueAsString(requestBody), ContentType.APPLICATION_JSON);
		httpPost.setEntity(entity);
		
		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
		
		String response = httpResponseReader(httpResponse);
		
		genericHttpResonseModel.statusCode = httpResponse.getStatusLine().getStatusCode();
		genericHttpResonseModel.reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
		genericHttpResonseModel.httpResponseBody = response;
		
		httpResponse.close();
		
		return genericHttpResonseModel;
	}
	
	public GenericHttpResponseModel patchRequest(Object requestBodyObject, String requestUrl, String authType) throws JsonProcessingException,
		UnsupportedCharsetException, IOException, ClientProtocolException, UnsupportedOperationException {
					
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		if(authType == BookingProperties.AUTH_TYPE_COOKIE) {
			
			httpClient = HttpClientBuilder
					.create()
					.setDefaultCookieStore(
							getCoockieStore(getAuthToken().token)
							)
					.build();
		}
		
		GenericHttpResponseModel genericHttpResonseModel = new GenericHttpResponseModel();
		
		HttpPatch httpPatch = new HttpPatch(requestUrl);
		RequestConfig requestConfig = RequestConfig.custom().build();		
		
		httpPatch.setConfig(requestConfig);		
		httpPatch.addHeader("Content-Type", "application/json");
		httpPatch.addHeader("Accept", "*/*");
		
		if(authType == BookingProperties.AUTH_TYPE_BASIC) {
			httpPatch.addHeader("Authorization", getBasicAuthenticationHeader(
					_bookingDataFactory.getLoginDetails().username,
					_bookingDataFactory.getLoginDetails().password
					));
		}
		
		String requestBody = _objectMapper.setSerializationInclusion(Include.NON_NULL).writeValueAsString(requestBodyObject);
			
		StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
		httpPatch.setEntity(entity);
		
		CloseableHttpResponse httpResponse = httpClient.execute(httpPatch);
		
		String response = httpResponseReader(httpResponse);
					
		genericHttpResonseModel.statusCode = httpResponse.getStatusLine().getStatusCode();
		genericHttpResonseModel.reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
		genericHttpResonseModel.httpResponseBody = response;
		
		httpResponse.close();
		
		return genericHttpResonseModel;
	}
	
	public GenericHttpResponseModel deleteRequest(String requestUrl, String authType) throws JsonProcessingException,
	UnsupportedCharsetException, IOException, ClientProtocolException, UnsupportedOperationException {
	
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		if(authType == BookingProperties.AUTH_TYPE_COOKIE) {
			
			httpClient = HttpClientBuilder
					.create()
					.setDefaultCookieStore(
							getCoockieStore(getAuthToken().token)
							)
					.build();
		}

		GenericHttpResponseModel genericHttpResonseModel = new GenericHttpResponseModel();
		
		HttpDelete httpDelete = new HttpDelete(requestUrl);
		
		if(authType == BookingProperties.AUTH_TYPE_BASIC) {
			httpDelete.addHeader("Authorization", getBasicAuthenticationHeader(
					_bookingDataFactory.getLoginDetails().username,
					_bookingDataFactory.getLoginDetails().password
					));
		}
		
		CloseableHttpResponse httpResponse = httpClient.execute(httpDelete);
		
		String response = httpResponseReader(httpResponse);
						
		genericHttpResonseModel.statusCode = httpResponse.getStatusLine().getStatusCode();
		genericHttpResonseModel.reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
		genericHttpResonseModel.httpResponseBody = response;
		
		httpResponse.close();
		
		return genericHttpResonseModel;

	}
	

	private CookieStore getCoockieStore(String token) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("token", token);
		cookie.setDomain(BookingProperties.BOOKING_DOMAIN);
		cookie.setPath("/booking");
		cookieStore.addCookie(cookie);
		return cookieStore;
	}

	private String getBasicAuthenticationHeader(String username, String password) {
		
		String credentialsToEncode = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(credentialsToEncode.getBytes());
		
	}
	
	/**
	 * @param httpResponse
	 * @return
	 * @throws IOException
	 * @throws UnsupportedOperationException
	 */
	private String httpResponseReader(CloseableHttpResponse httpResponse)
			throws IOException, UnsupportedOperationException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();
		return response.toString();
	}
	
	public GenericHttpResponseModel createBooking(BookingModel randomBookingRequest) throws JsonProcessingException, UnsupportedCharsetException, ClientProtocolException, UnsupportedOperationException, IOException {
				
		GenericHttpResponseModel createdBooking = postRequest(randomBookingRequest, BookingProperties.BOOKING_URL, null);			
			
		return createdBooking;
		
	}
	
	public BookingIdModel[] getExistingBookingIds() throws ClientProtocolException, IOException {
		
		GenericHttpResponseModel bookingIdResponse = getRequest(BookingProperties.BOOKING_URL, null, BookingProperties.AUTH_TYPE_NOAUTH);
		
		BookingIdModel[] bookingIdList = _objectMapper.readValue(bookingIdResponse.httpResponseBody, BookingIdModel[].class);
		
		return bookingIdList;
	}
	
	public CloseableHttpClient getCloseableHttpClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
	{
	  CloseableHttpClient httpClient = HttpClients.custom()
    		.setProxy(new HttpHost("localhost", 8888))
//				.setDefaultCookieStore(
//						getCoockieStore(getAuthToken().token)
//						)
        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).
            setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
            {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                {
                    return true;
                }
            }).build()).build();

	  return httpClient;
	}
	
}
