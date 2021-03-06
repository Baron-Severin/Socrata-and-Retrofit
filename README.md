# Using Socrata with Retrofit


Retrofit is a library for Android and Java that simplifies the process of making type-safe HTTP requests.  We've written a basic Android application to show off how to use Socrata's APIs with Retrofit.  You can find the Retrofit Github page here.

https://github.com/square/retrofit

First, we sort our dependencies.  Note that Retrofit requires that GSON be added as well.
<pre><code>    // Retrofit
    compile 'com.squareup.retrofit2:retrofit:2.0.2'
    compile 'com.squareup.retrofit2:converter-gson:2.0.2'

    // GSON (required for Retrofit)
    compile 'com.google.code.gson:gson:2.6.2'
</code></pre>

The first aspect of building our query is attaching a base URL.  Later we will add modifiers to refine our query.  In this case, we are using a Seattle public API provided by Socrata.

<pre><code>	public class ApiClient {
	    public static final String BASE_URL = "https://data.seattle.gov";
	    private static Retrofit retrofit = null;

	    public static Retrofit getClient() {
	        if (retrofit==null) {
	            retrofit = new Retrofit.Builder()
	                    .baseUrl(BASE_URL)
	                    .addConverterFactory(GsonConverterFactory.create())
	                    .build();
	        }
	        return retrofit;
	    }
	}
</code></pre>

Next we create an interface that will be automatically subclassed by Retrofit.  Here we're structuring a template for future calls.  We will implement them later with specific parameters.

<pre><code>	public interface ApiInterface {
		//// Example query: https://data.seattle.gov/resource/i5jq-ms7b.json?$$app_token=YOUR-APP-TOKEN-HERE&$where=value<500&$limit=10
    	@GET("resource/i5jq-ms7b.json")
    	Call< List< BuildingPermit > > getPermitsByValue(@Query("$$app_token") String apiToken,
                                                 @Query("$where") String value, @Query("$limit") int limit);

        // Example query: https://data.seattle.gov/resource/i5jq-ms7b.json?$$app_token=YOUR-APP-TOKEN-HERE&category=COMMERCIAL&$limit=10
        @GET("resource/i5jq-ms7b.json")
    	Call< List< BuildingPermit > > getPermitsByCategory(@Query("$$app_token") String apiToken,
                                                 @Query("category") String value, @Query("$limit") int limit);
    }
</code></pre>

Now we construct the model that will be inflated with the API response.  To find out which fields you require, look through the JSON body.  Create a variable for each field you wish to use, and annotate this variable with the @SerializedName() tag.  Within that tag, enter the JSON key as a String.

<B>JSON Response:</B>
<pre><code>
	{
		...
		<B>category</B>: "SINGLE FAMILY / DUPLEX",
		<B>location</B>: {
		type: "Point",
		coordinates: [
		-122.393174,
		47.571636
		]
		},
		<B>value</B>: "0"
		...
	}
</code></pre>

<B>Model:</B>
<pre><code>
	public class BuildingPermit {
		...
		@SerializedName("<B>category</B>")
	    private String category;
	    @SerializedName("<B>location</B>")
	    private BuildingLocation location;
	    @SerializedName("<B>value</B>")
	    private int value;
	    ...
	}
</code></pre>

Note that in the above example, the key 'location' is inflated into a custom child object.  This child object must also use the @SerializedName() annotation for its variables.

Next, when we plan to make our call, we create a client and use it to create a subclass of our ApiInterface, using Retrofit.create().

<pre><code>		Retrofit client = ApiClient.getClient();
        ApiInterface apiInterface = client.create(ApiInterface.class);
</code></pre>

After that, our call is constructed by combining these various components.

<pre><code>	Call< List< BuildingPermit > > call = apiInterface.getPermitsByValue(apiToken,
                "value<" + value, 100);
                enqueueCall(call);
            }
</code></pre>

Our enqueue method automatically queues up the call, then we override onResponse and onFailure to handle each case.  <B>Depending on how the JSON response is formatted, we may need to either inflate an object with various children, or an array of objects.</B>  

We verify that we receive a 200 HTTP response, then use the response.body() to create our objects.  Several helper methods exist within response to help troubleshoot problems, and in particular response.code() and response.message() can be extremely helpful when debugging.

<pre><code>    private void enqueueCall(Call< List< BuildingPermit > > call) {

        call.enqueue(new Callback< List< BuildingPermit>>() {

            @Override
            public void onResponse(Call< List< BuildingPermit > > call, Response< List< BuildingPermit>> response) {

                // If response code is from 200
                if (response.code() > 199 && response.code() < 300) {
                    List< BuildingPermit> permits = response.body();
                } else {
                    Log.d("Invalid response code: ", Integer.toString(response.code()));
                    Log.d("Response message: ", response.message());
                }
            }

            @Override
            public void onFailure(Call< List< BuildingPermit > > call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getBaseContext(), "No data received. Please check your" +
                        " connection and try again", Toast.LENGTH_LONG).show();
            }
        });
    }
</code></pre>

Now we have a list of Java objects, and modular queries that can be easily changed to fit user input.

<img src="/screenshots/screen1.png" alt="Drawing" width= "200"/> 
<img src="/screenshots/screen2.png" alt="Drawing" width= "200"/> 
<img src="/screenshots/screen3.png" alt="Drawing" width= "200"/> 


<B>Full Application Code:</B>

https://github.com/Baron-Severin/Socrata-and-Retrofit


<B>Contributors:</B>
######Severin Rudie: https://github.com/Baron-Severin
######Matthew Duffin: https://github.com/Duffin22
######Michael M Kang: https://github.com/mgkang0206