package com.devspark.positioning;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author e.shishkin
 *
 */
public class MainActivity extends Activity {
	private static final String LOG_TAG = MainActivity.class.getSimpleName();
	
	private ListView list;
	private EditText edit;
	private ImageButton searchBtn;
	
	private AddressArrayAdapter adapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.v(LOG_TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        list = (ListView) findViewById(android.R.id.list);
        edit = (EditText) findViewById(android.R.id.edit);
        searchBtn = (ImageButton) findViewById(R.id.search_btn);
        
        adapter = new AddressArrayAdapter(this);
        list.setAdapter(adapter);
        
        searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SearchTask().execute(new String[] {edit.getText().toString()});
			}
		});
    }
    
    private class SearchTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... params) {
			String locationName = params[0];
			Log.v(LOG_TAG, String.format("search() called: locationName=[%s]", locationName));
			List<Address> addresses = null;
	    	Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
	    	try {
	    		addresses = geocoder.getFromLocationName(locationName, 5);
	    	} catch (IOException e) {
				Log.w(LOG_TAG, e);
			}
			return addresses;
		}
		
		@Override
		protected void onPostExecute(List<Address> result) {
			if (result != null && !result.isEmpty()) {
				adapter.setData(result);
			}
		}
    	
    }
    
    private class AddressArrayAdapter extends ArrayAdapter<Address> {
    	
	    private LayoutInflater mLayoutInflater;
	    
	    public AddressArrayAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
	    
	    public void setData(List<Address> data) {
            clear();
            if (data != null) {
                for (Address address : data) {
                    add(address);
                }
            }
        }
	    
	    private class ViewHolder {
			TextView text1;
	        TextView text2;
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	final ViewHolder holder;
	    	View view = convertView;
	        if (view == null) {
	            view = mLayoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
	            holder = new ViewHolder();
	            holder.text1 = (TextView) view.findViewById(android.R.id.text1);
	            holder.text2 = (TextView) view.findViewById(android.R.id.text2);
	            view.setTag(holder);
	        } else {
	        	holder = (ViewHolder) view.getTag();
	        }
	        Address address = getItem(position);
	        holder.text1.setText(address.getAddressLine(0));
		    holder.text2.setText(address.getAddressLine(1));
	        return view;
	    }
	}
    
}