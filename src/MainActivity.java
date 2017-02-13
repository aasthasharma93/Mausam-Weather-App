package sharma.sugandha.mausam;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class MainActivity extends Activity implements OnClickListener{
	
	private static String TAG = "MausamMainActivity";
    
    List<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    List<CityData> data;
    DatabaseHandler db;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_main);
		
		Button addCity = (Button)findViewById(R.id.button1);
		addCity.setOnClickListener(this);
		
		
		db = new DatabaseHandler(this);
        Log.d(TAG, "Reading all data in onCreate()");
        
         data = db.getAllData();
         if(data.isEmpty()){}
         else{
        	 for(CityData cd : data){
        		 list.add(cd.getCity());
        	 }
         }
        
		ListView listView = (ListView) findViewById(R.id.listView1);	
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
		listView.setAdapter(adapter); 
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String zip = db.getData(adapter.getItem(position));
				new FeedClass(getApplicationContext(), 1, "").execute(zip);
			}
		}); 
	}

	@Override
	public void setTheme(int resid) {
		super.setTheme(resid);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Add City");
			alert.setMessage("Enter Zip Code");
	
			final EditText input = new EditText(this);
			alert.setView(input);
	
			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String value = input.getText().toString();
							new FeedClass(getApplicationContext(), 0, value).execute(value); 
							
						}
					});
			alert.show();
		}
		
	}
	
	public class FeedClass extends AsyncTask<String, Void, String>{

		Context context;
		int i;
		String zip;
	    private FeedClass(Context context, int i, String zip) {
	        this.context = context.getApplicationContext();
	        this.i = i;
	        this.zip = zip;
	    }
		
		@Override
		protected String doInBackground(String... arg0) {
			
			try{
				String feedUrl = "http://weather.yahooapis.com/forecastrss?z="+arg0[0]+"&u=c";	
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new URL(feedUrl).openStream());
				doc.getDocumentElement().normalize(); 
				if(i==1){
					NodeList nList = doc.getElementsByTagName("item");
		 
				    Node nNode = nList.item(0);
				    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
				      Element eElement = (Element) nNode;
				      NodeList nlList = eElement.getElementsByTagName("description").item(0).getChildNodes();
				      Node nValue = (Node) nlList.item(0);
				      return nValue.getNodeValue();
		 
				    }
				}else{
					NodeList nList = doc.getElementsByTagName("item");
					 
				    Node nNode = nList.item(0);
				    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
				      Element eElement = (Element) nNode;
				      NodeList nlList = eElement.getElementsByTagName("title").item(0).getChildNodes();
				      Node nValue = (Node) nlList.item(0);
				      
				      return nValue.getNodeValue();
		 
				    }
				}

			}catch(Exception e){
				Log.d(TAG, e.toString());
			}
			
			return arg0[0];
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(i==1){
				Intent intent = new Intent(context, ResultActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("resultString", result);
				context.startActivity(intent);
			}else{
				result = result.substring(result.indexOf("for "), result.indexOf(" at"));
				String city = result.substring(result.indexOf(" "));
				db.addData(new CityData(city, zip));
				list.add(city);
				adapter.notifyDataSetChanged();
			}
		}
		
	}

}
