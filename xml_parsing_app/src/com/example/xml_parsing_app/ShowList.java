package com.example.xml_parsing_app;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ShowList extends Activity implements OnClickListener {

	ListView lv;
	Button bRefresh, bAdd;
	int listSize, last_product_id, selected_pos = 0;
	String product_id = "", last_p_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_list);

		// Define buttons
		bRefresh = (Button) findViewById(R.id.bRefresh);
		bRefresh.setOnClickListener(this);
		bAdd = (Button) findViewById(R.id.bAdd);
		bAdd.setOnClickListener(this);

		// Define listView
		lv = (ListView) findViewById(R.id.lvProducts);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Return time schedule for a specific routeId..
				Class<?> ourClass;
				try {
					ourClass = Class
							.forName("com.example.xml_parsing_app.EditOrRemove");
					Intent ourIntent = new Intent(ShowList.this, ourClass);
					ourIntent.putExtra("position", position);

					// Get id from parseXML and send it to EditOrRemove class.
					selected_pos = position;
					parseXML();
					ourIntent.putExtra("product_id", product_id);
					startActivity(ourIntent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// Populate the list with products.
		parseXML();
	}

	public void parseXML() {
		ArrayList<String> products_list = new ArrayList<String>();
		String parsedData = "";
		try {
			/** Handling XML */
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			ItemXMLHandler myXMLHandler = new ItemXMLHandler();
			xr.setContentHandler(myXMLHandler);
			InputStream is = new FileInputStream(
					"/data/data/com.example.xml_parsing_app/databases/products.xml");
			InputSource inStream = new InputSource(is);
			xr.parse(inStream);

			ArrayList<ItemMaster> itemsList = myXMLHandler.getItemsList();

			for (int i = 0; i < itemsList.size(); i++) {
				parsedData = "";
				ItemMaster item = itemsList.get(i);
				parsedData = parsedData + "Product id: " + item.getId() + "\n";
				parsedData = parsedData + "Name: " + item.getName() + "\n";
				parsedData = parsedData + "Description: "
						+ item.getDescription();
				products_list.add(parsedData);
			}

			// Get the id of the selected item.
			ItemMaster item2 = itemsList.get(selected_pos);
			product_id = item2.getId();
			// Get the id of the last item.
			ItemMaster item3 = itemsList.get(itemsList.size() - 1);
			last_p_id = item3.getId();

			listSize = itemsList.size();
			Log.w("AndroidParseXMLActivity", "Done");
		} catch (Exception e) {
			Log.w("AndroidParseXMLActivity", e);
		}
		// Add results into the listView.
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, products_list);
		lv.setAdapter(arrayAdapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Class<?> addProduct_class = null;
		try {
			addProduct_class = Class
					.forName("com.example.xml_parsing_app.AddProduct");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent AddProductIntent = new Intent(ShowList.this, addProduct_class);
		last_product_id = Integer.valueOf(last_p_id);
		AddProductIntent.putExtra("last_product_id", last_product_id);
		switch (v.getId()) {
		case R.id.bAdd:
			startActivity(AddProductIntent);
			break;
		case R.id.bRefresh:
			// Refresh list of products.
			parseXML();
			break;
		}
	}
}
