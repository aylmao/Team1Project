package dataManagers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.apache.http.HttpResponse;

import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.claims.ApprovedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ProgressClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ReturnedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;
import ca.ualberta.cs.team1travelexpenseapp.gsonUtils.GsonUtils;
import ca.ualberta.cs.team1travelexpenseapp.gsonUtils.RuntimeTypeAdapterFactory;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public abstract class ClaimListManager {

	protected Context context;
	protected ClaimList claimList;
	protected static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/claim/";
	protected static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/claim/_search";
	private static boolean registered = false;
	
	abstract public void saveClaims();
	abstract public void loadClaims();
	abstract public void removeClaim(Claim claim);
	abstract public void onConnect();
	
	//from http://stackoverflow.com/a/22081826 March 29 2015
	private static final RuntimeTypeAdapterFactory<Claim> adapter = RuntimeTypeAdapterFactory.of(Claim.class);

	    
    public ClaimListManager(){
    	if(!registered){
    		GsonUtils.registerType(adapter);
    		adapter.registerSubtype(Claim.class);
            adapter.registerSubtype(ApprovedClaim.class);
            adapter.registerSubtype(ProgressClaim.class);
            adapter.registerSubtype(ReturnedClaim.class);
            adapter.registerSubtype(SubmittedClaim.class);
            registered=true;
    	}
    }

	/**
	 * From https://github.com/rayzhangcl/ESDemo March 28, 2015
	 * get the http response and return json string
	 */
	protected String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}

	/**
	 * Context must be set to save/load from file
	 * @param context
	 */
	public void setContext(Context context) {
		this.context=context;
	}

	protected boolean NetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
