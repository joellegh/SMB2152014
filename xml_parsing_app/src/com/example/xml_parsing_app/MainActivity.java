package com.example.xml_parsing_app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button sign_in_button = (Button) findViewById(R.id.bSignIn);
		Button sign_up_button = (Button) findViewById(R.id.bSignUp);
		sign_in_button.setOnClickListener(this);
		sign_up_button.setOnClickListener(this);

		// Create the database.
		DatabaseHelper info = new DatabaseHelper(this);
		try {
			info.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		info.openDataBase();
		info.close();
		// Check if file already exists.
		if (checkFile() == false) {
			// Copy assets files to sdcard so i can edit the xml.
			try {
				copyAssets();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void copyAssets() throws IOException {
		// TODO Auto-generated method stub
		// Open your local db as the input stream.
		InputStream myInput;
		myInput = this.getAssets().open("products.xml");
		// Path to the just created empty db.
		String outFileName = "/data/data/com.example.xml_parsing_app/databases/"
				+ "products.xml";
		// Open the empty db as the output stream.
		OutputStream myOutput;
		myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile.
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams.
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/*
	 * Check if the xml file already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkFile() {
		File f = new File("/data/data/com.example.xml_parsing_app/databases/"
				+ "products.xml");
		if (f.exists()) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Class<?> showList_class = null;
		Class<?> signup_class = null;

		// Define editTexts
		EditText et_username = (EditText) findViewById(R.id.etUsername);
		EditText et_pass = (EditText) findViewById(R.id.etPass);

		// Define TextViews
		TextView tvUnameAlert = (TextView) findViewById(R.id.tvUsernameAlert);
		TextView tvPassAlert = (TextView) findViewById(R.id.tvPassAlert);
		TextView tvLoginAlert = (TextView) findViewById(R.id.tvLoginAlert);

		/*
		 * Define the classes and the intents that needed for opening the
		 * specific activities each time a button is pressed.
		 */
		try {
			showList_class = Class
					.forName("com.example.xml_parsing_app.ShowList");
			signup_class = Class.forName("com.example.xml_parsing_app.SignUp");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent ShowListIntent = new Intent(MainActivity.this, showList_class);
		Intent SignUpIntent = new Intent(MainActivity.this, signup_class);

		switch (v.getId()) {
		// if sign in button is pressed.
		case R.id.bSignIn:
			// Validate inputs
			if (et_username.getText().toString().length() == 0) {
				tvUnameAlert.setText("Please insert a username.");
			} else if (et_username.getText().toString().length() < 3) {
				tvUnameAlert.setText("Username must be at least 3 inputs.");
			} else {
				tvUnameAlert.setText("");
			}
			if (et_pass.getText().toString().length() == 0) {
				tvPassAlert.setText("Please insert a password.");
			} else if (et_pass.getText().toString().length() < 3) {
				tvPassAlert.setText("Password must be at least 3 inputs.");
			} else {
				tvPassAlert.setText("");
			}

			// If OK start the activity.
			if (et_username.getText().toString().length() >= 3
					&& et_pass.getText().toString().length() >= 3) {

				// Search the database for the user.
				DatabaseHelper db = new DatabaseHelper(getApplicationContext());
				int user_id = db.user_id(et_username.getText().toString(),
						et_pass.getText().toString());
				db.close();

				// If user exists
				if (user_id != 0) {
					tvLoginAlert.setText("Successfull login!");
					startActivity(ShowListIntent);
				} else {
					tvLoginAlert.setText("Wrong username or password!");
				}
			}
			break;
		// if sign-up button is pressed we start the Sign-Up screen.
		case R.id.bSignUp:
			startActivity(SignUpIntent);
			break;
		}
	}
}
