package com.https.fileuploader.util;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/**
 * This class provides utility function for sending HTTP POST, PATCH, DELETE
 * request to a different endpoint
 */
@Component
public class HttpUtility {

	private String traccarBaseUrl;

	private String traccarUserName;

	private String traccarUserPassword;
//	
//	@Autowired
//	private ExecutorService executorService;

	public HttpUtility() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HttpUtility(String traccarBaseUrl, String traccarUserName, String traccarUserPassword) {
		super();
		this.traccarBaseUrl = traccarBaseUrl;
		this.traccarUserName = traccarUserName;
		this.traccarUserPassword = traccarUserPassword;
	}

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private static int responseCount(com.squareup.okhttp.Response response) {
		int result = 1;
		while ((response = response.priorResponse()) != null) {
			result++;
		}
		return result;
	}

	public String httpPost(String url, JsonObject body) {

		com.squareup.okhttp.Response response = null;
		try {
			OkHttpClient client = new OkHttpClient();
			// set to avoid socket timeout connection error
			client.setConnectTimeout(0, TimeUnit.SECONDS);
			client.setReadTimeout(0, TimeUnit.SECONDS);
			client.setWriteTimeout(0, TimeUnit.SECONDS);
			client.setAuthenticator(new Authenticator() {
				@Override
				public Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) {
					if (responseCount(response) >= 3) {
						return null; // If we've failed 3 times, give up. - in real life, never give up!!
					}
					String credential = Credentials.basic(traccarUserName, traccarUserPassword);
					return response.request().newBuilder().header("Authorization", credential).build();
				}

				@Override
				public Request authenticateProxy(Proxy proxy, com.squareup.okhttp.Response response) {
					return null;
				}

			});

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody requestBody = RequestBody.create(mediaType, body.toString());
			Request request = new Request.Builder().url(encodeUrl(traccarBaseUrl + url)).post(requestBody)
					.addHeader("Accept", "application/json").addHeader("cache-control", "no-cache").build();

			response = client.newCall(request).execute();
			String result;
			if (!response.isSuccessful()) {
				return null;
			} else {
				result = response.body().string();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// body close to avoid memory leak
			try {
				if (response != null) {
					response.body().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String httpDelete(String url, JsonObject body) {

		com.squareup.okhttp.Response response = null;
		try {
			OkHttpClient client = new OkHttpClient();
			// set to avoid socket timeout connection error
			client.setConnectTimeout(0, TimeUnit.SECONDS);
			client.setReadTimeout(0, TimeUnit.SECONDS);
			client.setWriteTimeout(0, TimeUnit.SECONDS);
			client.setAuthenticator(new Authenticator() {
				@Override
				public Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) {
					if (responseCount(response) >= 3) {
						return null; // If we've failed 3 times, give up. - in real life, never give up!!
					}
					String credential = Credentials.basic(traccarUserName, traccarUserPassword);
					return response.request().newBuilder().header("Authorization", credential).build();
				}

				@Override
				public Request authenticateProxy(Proxy proxy, com.squareup.okhttp.Response response) {
					return null;
				}

			});

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody requestBody = RequestBody.create(mediaType, body.toString());
			Request request = new Request.Builder().url(encodeUrl(traccarBaseUrl + url)).delete(requestBody)
					.addHeader("Accept", "application/json").addHeader("cache-control", "no-cache").build();

			response = client.newCall(request).execute();
			String result;
			if (!response.isSuccessful()) {
				return null;
			} else {
				result = response.body().string();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// body close to avoid memory leak
			try {
				if (response != null) {
					response.body().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String httpGet(String url) {

		com.squareup.okhttp.Response response = null;
		try {
			OkHttpClient client = new OkHttpClient();
			// set to avoid socket timeout connection error
			client.setConnectTimeout(0, TimeUnit.SECONDS);
			client.setReadTimeout(0, TimeUnit.SECONDS);
			client.setWriteTimeout(0, TimeUnit.SECONDS);
			client.setAuthenticator(new Authenticator() {
				@Override
				public Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
					if (responseCount(response) >= 3) {
						return null; // If we've failed 3 times, give up. - in real life, never give up!!
					}
					String credential = Credentials.basic(traccarUserName, traccarUserPassword);
					return response.request().newBuilder().header("Authorization", credential).build();
				}

				@Override
				public Request authenticateProxy(Proxy proxy, com.squareup.okhttp.Response response)
						throws IOException {
					return null;
				}

			});

			Request request = new Request.Builder().url(encodeUrl(traccarBaseUrl + url)).get()
					.addHeader("Accept", "application/json").addHeader("cache-control", "no-cache").build();

			response = client.newCall(request).execute();
			String result = null;
			if (!response.isSuccessful()) {
				return null;
			} else {
				result = response.body().string();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// body close to avoid memory leak
			try {
				if (response != null) {
					response.body().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// encode url which browser accepts
	public String encodeUrl(String url) {
		// to encode dataname
		if (url.contains(" "))
			url = url.replace(" ", "%20");
		if (url.contains("'"))
			url = url.replace("'", "%27");

		return url;
	}

	public String httpDeleteV2(String url) {

		com.squareup.okhttp.Response response = null;
		try {
			OkHttpClient client = new OkHttpClient();
			// set to avoid socket timeout connection error
			client.setConnectTimeout(0, TimeUnit.SECONDS);
			client.setReadTimeout(0, TimeUnit.SECONDS);
			client.setWriteTimeout(0, TimeUnit.SECONDS);
			client.setAuthenticator(new Authenticator() {
				@Override
				public Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) {
					if (responseCount(response) >= 3) {
						return null; // If we've failed 3 times, give up. - in real life, never give up!!
					}
					String credential = Credentials.basic(traccarUserName, traccarUserPassword);
					return response.request().newBuilder().header("Authorization", credential).build();
				}

				@Override
				public Request authenticateProxy(Proxy proxy, com.squareup.okhttp.Response response) {
					return null;
				}

			});

			Request request = new Request.Builder().url(encodeUrl(traccarBaseUrl + url)).delete()
					.addHeader("Accept", "application/json").addHeader("cache-control", "no-cache").build();

			response = client.newCall(request).execute();
			String result;
			if (!response.isSuccessful()) {
				return null;
			} else {
				result = response.body().string();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// body close to avoid memory leak
			try {
				if (response != null) {
					response.body().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String httpPostRequestPushAlert(String url, String body) {
		com.squareup.okhttp.Response response = null;
		Request request = null;
		OkHttpClient client = null;
		try {
			client = new OkHttpClient();
			// set to avoid socket timeout connection error
			client.setConnectTimeout(0, TimeUnit.SECONDS);
			client.setReadTimeout(0, TimeUnit.SECONDS);
			client.setWriteTimeout(0, TimeUnit.SECONDS);

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody requestBody = RequestBody.create(mediaType, body);
			request = new Request.Builder().url(encodeUrl(url)).post(requestBody)
					.addHeader("Accept", "application/json").addHeader("cache-control", "no-cache").build();

			response = client.newCall(request).execute();
			String result;
			if (!response.isSuccessful()) {
				return null;
			} else {
				result = response.body().string();
				System.out.println("Event forwarding success");
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// body close to avoid memory leak
			try {
				client = null;
				request = null;
				if (response != null) {
					response.body().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String httpGetV2(String url) {

		com.squareup.okhttp.Response response = null;
		try {
			OkHttpClient client = new OkHttpClient();
			// set to avoid socket timeout connection error
			client.setConnectTimeout(0, TimeUnit.SECONDS);
			client.setReadTimeout(0, TimeUnit.SECONDS);
			client.setWriteTimeout(0, TimeUnit.SECONDS);
			client.setAuthenticator(new Authenticator() {
				@Override
				public Request authenticate(Proxy proxy, com.squareup.okhttp.Response response) throws IOException {
					if (responseCount(response) >= 3) {
						return null; // If we've failed 3 times, give up. - in real life, never give up!!
					}
					String credential = Credentials.basic(traccarUserName, traccarUserPassword);
					return response.request().newBuilder().header("Authorization", credential).build();
				}

				@Override
				public Request authenticateProxy(Proxy proxy, com.squareup.okhttp.Response response)
						throws IOException {
					return null;
				}

			});

			Request request = new Request.Builder().url(encodeUrl(url)).get().addHeader("Accept", "application/json")
					.addHeader("cache-control", "no-cache").build();

			response = client.newCall(request).execute();
			String result = null;
			if (!response.isSuccessful()) {
				return null;
			} else {
				result = response.body().string();
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// body close to avoid memory leak
			try {
				if (response != null) {
					response.body().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public final void httpPostRequestPushAlertAsync(String url, String body) {
		try {
			Client requestClient = ClientBuilder.newClient();
			Invocation.Builder requestBuilder = requestClient.target(url).request();
			requestBuilder.async().post(Entity.json(body), new InvocationCallback<Object>() {
				@Override
				public void completed(Object o) {
					LOGGER.info("Event forwarding succeeded");
					requestClient.close();
				}

				@Override
				public void failed(Throwable throwable) {
					throwable.printStackTrace();
					LOGGER.info("Event forwarding failed");
					requestClient.close();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final void httpPostRequestPushAlertAsyncNew(String url, String body) {
//		Client requestClient = ClientBuilder.newClient();
//        Invocation.Builder requestBuilder = requestClient.target(url).request();
//        executorService.execute(() -> {
//            try {
//                requestBuilder.async().post(Entity.json(body), new InvocationCallback<Object>() {
//                    @Override
//                    public void completed(Object o) {
//                        System.out.println("Event forwarding succeeded");
//                    }
//
//                    @Override
//                    public void failed(Throwable throwable) {
//                        throwable.printStackTrace();
//                        System.out.println("Event forwarding failed");
//                    }
//                }).get(); 
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
	}

	public void shutdown() {
//        // Shut down the executor service
//        executorService.shutdown();
	}

	private static final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(300);

	public void executerServiceForAPICallAsynchronously(String url, String body) {

//		Runnable apiCallTask = httpPostAsync(url, body);

		LOGGER.info("Thread Size " + executorService.getPoolSize());
		LOGGER.info("Executor Active Thread count " + executorService.getActiveCount());

//		System.out.println("Executor Active Thread count " + executorService.getActiveCount());

		executorService.submit(() -> sendHttpPostRequest(url, body));

//		System.out.println("POOL SIZE :: " + executorService.getCorePoolSize());
//		System.out.println("TASK COUNT :: " + executorService.getTaskCount());
//		System.out.println("LARGEST POOLSIZE : " + executorService.getLargestPoolSize());
//		System.out.println("Executor complete count " + executorService.getCompletedTaskCount());
		LOGGER.info("Executor complete count " + executorService.getCompletedTaskCount());

//        try {
//            executorService.shutdown();
//            executorService.awaitTermination(5, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
	}

	private void httpPostAsync(String url, String body) {
		try {
			Client requestClient = ClientBuilder.newClient();
			Invocation.Builder requestBuilder = requestClient.target(url).request();
			requestBuilder.async().post(Entity.json(body), new InvocationCallback<Object>() {
				@Override
				public void completed(Object o) {
					LOGGER.info("Event forwarding succeeded");
					requestClient.close();
				}

				@Override
				public void failed(Throwable throwable) {
					throwable.printStackTrace();
					LOGGER.info("Event forwarding failed");
					requestClient.close();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendHttpPostRequest(String url, String body) {
		com.squareup.okhttp.Response response = null;
		Request request = null;
		OkHttpClient client = null;

		try {
			client = new OkHttpClient();
			client.setConnectTimeout(0, TimeUnit.SECONDS);
			client.setReadTimeout(0, TimeUnit.SECONDS);
			client.setWriteTimeout(0, TimeUnit.SECONDS);

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody requestBody = RequestBody.create(mediaType, body);
			request = new Request.Builder().url(url).post(requestBody).addHeader("Accept", "application/json")
					.addHeader("cache-control", "no-cache").build();

			response = client.newCall(request).execute();
			String result;

			if (!response.isSuccessful()) {
				LOGGER.info("Event forwarding failed");
				return;
			} else {
				result = response.body().string();
				LOGGER.info("Event forwarding succeeded");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the response body to avoid memory leak
			if (response != null) {
				try {
					response.body().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
