package sharma.sugandha.mausam;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity{

	private static String TAG = "MausamResultActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_layout);
		Bundle bundle = getIntent().getExtras();
		WebView tv = (WebView)findViewById(R.id.textView1);
		ImageView iv = (ImageView)findViewById(R.id.imageView1);
		String result;
		
        if((result = bundle.getString("resultString"))!= null)
        {
        	String imageUrl = result.substring(result.indexOf("h"), result.indexOf(">"));
        	imageUrl = imageUrl.substring(0, imageUrl.indexOf("\""));
        	int statusCode = Character.getNumericValue(imageUrl.charAt(imageUrl.length()-6)) * 10 + Character.getNumericValue(imageUrl.charAt(imageUrl.length()-5));
        	Log.d(TAG, "Status Code for: "+imageUrl+" "+statusCode);
        	Calendar cal = Calendar.getInstance(); 
        	int hourofday = cal.get(Calendar.HOUR_OF_DAY);
        	if(hourofday >=6 && hourofday <=17){
        		new FetchImage(iv).execute("http://l.yimg.com/us.yimg.com/i/us/nws/weather/gr/"+statusCode+"d.png");
        	}else{
        		new FetchImage(iv).execute("http://l.yimg.com/us.yimg.com/i/us/nws/weather/gr/"+statusCode+"n.png");
        	}
        	
        	
        	String data = "<html><body bgcolor=\"#000000\" text = \"white\">"+result.substring(result.indexOf("Current"), result.indexOf("<a"))+"</body></html>";
          	tv.loadData(data, "text/html", "UTF-8");
        }else{}
	}
	
	public class FetchImage extends AsyncTask<String, Void, Bitmap>{
		private ImageView iv;
		
		public FetchImage(ImageView iv){
			this.iv = iv;
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			InputStream in = null;
	        int response = -1;
	                
	        try{
	        	URL url = new URL(params[0]); 
		        URLConnection conn = url.openConnection();
	            HttpURLConnection httpConn = (HttpURLConnection) conn;
	            httpConn.setAllowUserInteraction(false);
	            httpConn.setInstanceFollowRedirects(true);
	            httpConn.setRequestMethod("GET");
	            httpConn.connect(); 
	 
	            response = httpConn.getResponseCode();                 
	            if (response == HttpURLConnection.HTTP_OK) {
	                in = httpConn.getInputStream();                                 
	            }                     
	        }
	        catch (Exception ex)
	        {
	            Log.d(TAG, ex.toString());          
	        }
	        
	        Bitmap bitmap = null;
	                
	        try {
	            bitmap = BitmapFactory.decodeStream(in);
	            //bitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
	            in.close();
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	        return bitmap;                    
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			iv.setImageBitmap(result);
		}
		
	}

}
