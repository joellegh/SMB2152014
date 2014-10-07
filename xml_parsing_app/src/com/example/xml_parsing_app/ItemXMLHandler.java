package com.example.xml_parsing_app;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ItemXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = "";
	ItemMaster item = null;
	private ArrayList<ItemMaster> itemsList = new ArrayList<ItemMaster>();

	public ArrayList<ItemMaster> getItemsList() {
		return itemsList;
	}

	// Called when tag starts
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;
		currentValue = "";
		if (localName.equals("Product")) {
			item = new ItemMaster();
		}
	}

	// Called when tag closing
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		/** set value */
		if (localName.equalsIgnoreCase("id"))
			item.setId(currentValue);
		else if (localName.equalsIgnoreCase("name"))
			item.setName(currentValue);
		else if (localName.equalsIgnoreCase("description"))
			item.setDescription(currentValue);
		else if (localName.equalsIgnoreCase("Product"))
			itemsList.add(item);
	}

	// Called to get tag characters
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}

	}

}
