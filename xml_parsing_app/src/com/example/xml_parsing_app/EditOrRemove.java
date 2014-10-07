package com.example.xml_parsing_app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditOrRemove extends Activity implements OnClickListener {

	int position, delete = 1;
	EditText etName, etDescription;
	TextView tvAlert;
	String product_id, product_name, product_description, pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_or_remove);

		// Define textViews and editTexts.
		tvAlert = (TextView) findViewById(R.id.tvAlert2);
		etName = (EditText) findViewById(R.id.etName2);
		EditText etId = (EditText) findViewById(R.id.etId2);
		// Make edit text for id to be not editable.
		etId.setKeyListener(null);
		etDescription = (EditText) findViewById(R.id.etDescription2);

		// Define buttons.
		Button bSaveChanges = (Button) findViewById(R.id.bSaveChanges);
		Button bDelete = (Button) findViewById(R.id.bDelete);
		bSaveChanges.setOnClickListener(this);
		bDelete.setOnClickListener(this);

		// Retrieve data from main activity.
		Bundle extras = getIntent().getExtras();
		position = extras.getInt("position");
		product_id = extras.getString("product_id");

		etId.setText(product_id);
		parseXML();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bSaveChanges:
			if (etName.length() > 1 && etDescription.length() > 1) {
				try {
					editXml();
					tvAlert.setText("Successfull change!");
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				tvAlert.setText("Name length and description length must be at "
						+ "least 2 characters.");
			}
			break;
		case R.id.bDelete:
			delete = 0;
			try {
				editXml();
				tvAlert.setText("Product deleted.");
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			delete = 1;
			break;
		}
	}

	public void editXml() throws ParserConfigurationException {
		try {
			File xmlFile = new File(
					"/data/data/com.example.xml_parsing_app/databases/products.xml");
			// Create the documentBuilderFactory
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			// Create the documentBuilder
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			// Create the Document by parsing the file
			Document document = documentBuilder.parse(xmlFile);
			// Get the root element of the xml Document;
			Element documentElement = document.getDocumentElement();

			// Get products by tag name
			// Use item(0) to get the first node with tag name "Product"
			Node product = document.getElementsByTagName("Product").item(
					position);

			// Edit products.
			NodeList list = product.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if ("Name".equals(node.getNodeName())) {
					node.setTextContent(etName.getText().toString());
				}
				if ("Description".equals(node.getNodeName())) {
					node.setTextContent(etDescription.getText().toString());
				}
			}

			if (delete == 0) {
				// Obtain a node
				Element element = (Element) document.getElementsByTagName(
						"Product").item(position);
				// Remove the node
				element.getParentNode().removeChild(element);
			}
			Transformer tFormer = TransformerFactory.newInstance()
					.newTransformer();
			// Set output file to xml
			tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
			// Write the document back to the file
			Source source = new DOMSource(document);
			Result result = new StreamResult(xmlFile);
			tFormer.transform(source, result);

		} catch (TransformerException ex) {

		} catch (SAXException ex) {

		} catch (IOException ex) {

		}
	}

	public void parseXML() {
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

			// Get the Name of the selected item.
			ItemMaster item2 = itemsList.get(position);
			product_name = item2.getName();
			etName.setText(product_name);
			// Get the Name of the selected item.
			ItemMaster item3 = itemsList.get(position);
			product_description = item3.getDescription();
			etDescription.setText(product_description);

		} catch (Exception e) {
			Log.w("AndroidParseXMLActivity", e);
		}

	}

}
