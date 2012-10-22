package com.dugga.android;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TaskInquiryActivity extends Activity {
	private ScrollView scrollView;
	final String TYP_FLDLST = "FLDLST";
	final String TYP_MSG = "MSG";
	final String TYP_SCR = "SCR";
	final String TYP_BTN = "BTN";
	final String TYP_LBL = "LBL";
	final String TYP_EDITTXT = "EDITTXT";
	final String TYP_VIEWTXT = "VIEWTXT";
	final String TYP_LIST = "LIST";
	final String TYP_SPIN = "SPIN";
	final String TYP_DIV = "DIV";
	final String TYP = "TYPE";
	final String NAME = "NAME";
	final String VALUE = "VALUE";
	final String URL = "http://myMachine.dugga.com:1666/dxdweb/dxdwebi";
	
	final String ACT_LISTPROJECTS = "listProjects";
	final String ACT_LISTTASKS = "listTasks";
	final String ACT_LISTTASKDETAILS = "listTaskDetails";
	private String action;
	private String lastSelectedProject;
	private String lastSelectedProjectTaskSubtask;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		scrollView = (ScrollView) findViewById(R.id.scrollView1);
	}

	private class ListProjectsTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			if (action.equals(ACT_LISTPROJECTS)) {
				JSONObject jsonObject=new JSONObject();
				try {
					jsonObject.put("action", ACT_LISTPROJECTS);
				} catch (Exception e) {}
				response = sendToServer(URL, jsonObject);
			} else if (action.equals(ACT_LISTTASKS)) {
				JSONObject jsonObject=new JSONObject();
				try {
					jsonObject.put("action", ACT_LISTTASKS);
					jsonObject.put("selectedProject",lastSelectedProject);
					response = sendToServer(URL, jsonObject);
				} catch (Exception e) {}				
			} else if (action.equals(ACT_LISTTASKDETAILS)) {
				JSONObject jsonObject=new JSONObject();
				try {
					jsonObject.put("action", ACT_LISTTASKDETAILS);
					jsonObject.put("selectedProject",lastSelectedProjectTaskSubtask.substring(0, 4));
					jsonObject.put("selectedTask",lastSelectedProjectTaskSubtask.substring(4, 8));
					jsonObject.put("selectedSubtask",lastSelectedProjectTaskSubtask.substring(8, 10));
					response = sendToServer(URL, jsonObject);
				} catch (Exception e) {}				
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			setContentView(processJSON(result));
		}
	}
	
	public String sendToServer(String url, JSONObject jsonObject) {
		String result = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			StringEntity stringEntity = new StringEntity(jsonObject.toString());
			httppost.setEntity(stringEntity);
			HttpResponse response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = entity.getContent();
				result = convertStreamToString(inputStream);
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public View processJSON(String jsonStr) {
		HorizontalScrollView hsv = null;
		ScrollView sv = null;

		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray topElems = json.names();

			for (int i1 = 0; i1 < topElems.length(); i1++) {
				JSONObject form = json.getJSONObject("FORM");

				hsv = new HorizontalScrollView(this);
				sv = new ScrollView(this);
				hsv.addView(sv);
				LinearLayout ll = new LinearLayout(this);
				ll.setOrientation(LinearLayout.VERTICAL);
				sv.addView(ll);
				
				Button backButton = new Button(this);
				backButton.setText("Back");
				backButton.setContentDescription(action);
				backButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						processButtonEvent(v);
					}
				});
				ll.addView(backButton);
				
				JSONArray fldLst = form.getJSONArray(TYP_FLDLST);
				for (int i2 = 0; i2 < fldLst.length(); i2++) {
					JSONObject fld = fldLst.getJSONObject(i2);
					if (fld.get(TYP).equals(TYP_BTN)) {
						Button b = new Button(this);
						b.setText(fld.getString(VALUE));
						b.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								processButtonEvent(v);
							}
						});
						ll.addView(b);
					} else if (fld.get(TYP).equals(TYP_EDITTXT)) {
						EditText et = new EditText(this);
						et.setText(Html.fromHtml(fld.getString(VALUE)));
						ll.addView(et);
					} else if (fld.get(TYP).equals(TYP_LBL)) {
						TextView tv = new TextView(this);
						tv.setText(Html.fromHtml(fld.getString(VALUE)));
						tv.setContentDescription(fld.getString(NAME));
						ll.addView(tv);
					} else if (fld.get(TYP).equals(TYP_VIEWTXT)) {
						TextView tv = new TextView(this);
						tv.setText(Html.fromHtml(fld.getString(VALUE)));
						tv.setContentDescription(fld.getString(NAME));
						ll.addView(tv);
					} else if (fld.get(TYP).equals(TYP_LIST)) {
						addDivider(ll);
						TextView tv = new TextView(this);
						tv.setText(Html.fromHtml(fld.getString(VALUE)));
						tv.setContentDescription(fld.getString(NAME));
						tv.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								processItemSelection(v);
							}
						});
						ll.addView(tv);
					} else if (fld.get(TYP).equals(TYP_DIV)) {
						addDivider(ll);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		return hsv;
	}

	private void addDivider(LinearLayout linearLayout) {
		TextView divider = new TextView(this);
		divider.setBackgroundColor(divider.getCurrentTextColor());
		divider.setHeight(1);
		linearLayout.addView(divider);
	}

	protected void processItemSelection(View v) {
		if (action.equals(ACT_LISTPROJECTS)) {
			action = ACT_LISTTASKS;
			lastSelectedProject = v.getContentDescription().toString();
			ListProjectsTask tasks = new ListProjectsTask();
			tasks.execute(new String[] { URL });
		} else if (action.equals(ACT_LISTTASKS)) {
			action = ACT_LISTTASKDETAILS;
			lastSelectedProjectTaskSubtask = v.getContentDescription().toString();
			ListProjectsTask taskDetails = new ListProjectsTask();
			taskDetails.execute(new String[] { URL });
		}
	}

	public void processButtonEvent(View v) {
		Button btn = (Button) v;
		if (btn.getText().toString().equals("Back")) {
			if (btn.getContentDescription().equals(ACT_LISTTASKDETAILS)) {
				action = ACT_LISTTASKS;
				ListProjectsTask tasks = new ListProjectsTask();
				tasks.execute(new String[] { URL });
			}
			if (btn.getContentDescription().equals(ACT_LISTTASKS)) {
				action = ACT_LISTPROJECTS;
				ListProjectsTask projects = new ListProjectsTask();
				projects.execute(new String[] { URL });
			}
			if (btn.getContentDescription().equals(ACT_LISTPROJECTS)) listProjects(v);
		}
	}

	private static String convertStreamToString(InputStream is) {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    	StringBuilder sb = new StringBuilder();
    	
    	String line = null;
    	try {
    		while ((line = reader.readLine()) != null) {
    			sb.append(line + "\n");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			is.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return sb.toString();
    }
	
	private String[] parseItems(String string) {
		String[] items = string.split(";");
		return items;
	}
	
	public void listProjects(View view) {
		action = ACT_LISTPROJECTS;
		ListProjectsTask projects = new ListProjectsTask();
		projects.execute(new String[] { URL });
	}


}