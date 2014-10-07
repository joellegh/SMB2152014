package com.example.xml_parsing_app;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddProduct extends Activity implements OnClickListener {

	EditText etName;
	EditText etDescription;
	TextView tvAlert;
	int last_product_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);

		// Define save button.
		Button bSave = (Button) findViewById(R.id.bSave);
		bSave.setOnClickListener(this);

		// Define editTexts and textViews.
		etName = (EditText) findViewById(R.id.etName);
		etDescription = (EditText) findViewById(R.id.etDescription);
		tvAlert = (TextView) findViewById(R.id.tvAlert);

		// Retrieve data from caller activity.
		Bundle extras = getIntent().getExtras();
		last_product_id = extras.getInt("last_product_id");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bSave:
			if (etName.length() > 1 && etDescription.length() > 1) {
				try {
					addProduct(etName.getText().toString(), etDescription
							.getText().toString());
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tvAlert.setText("Successfull save!");
			} else {
				tvAlert.setText("Name length and description length must be at "
						+ "least 2 characters.");
			}
			break;
		}
	}

	public void addProduct(String name, String description)
			throws ParserConfigurationException {
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
			System.out.println("documentElement:" + documentElement.toString());
			// Get childNodes of the rootElement
			// Create a id element
			Element id_node = document.createElement("Id");
			String lpid = Integer.toString(last_product_id + 1);
			id_node.setTextContent(lpid);
			Element name_node = document.createElement("Name");
			name_node.setTextContent(name);
			Element desc_node = document.createElement("Description");
			desc_node.setTextContent(description);
			// Create a Product element
			Element productElement = document.createElement("Product");
			// append id_node to Node element;
			productElement.appendChild(id_node);
			productElement.appendChild(name_node);
			productElement.appendChild(desc_node);
			// append Node to rootNode element
			documentElement.appendChild(productElement);
			document.replaceChild(documentElement, documentElement);
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

}
