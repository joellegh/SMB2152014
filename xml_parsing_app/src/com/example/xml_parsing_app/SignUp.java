package com.example.xml_parsing_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);

		// Define the sign up button.
		Button bSignUp = (Button) findViewById(R.id.bSignUp);

		// Set the onClick Listener to execute code each time a button is
		// pressed.
		bSignUp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// Define editTexts
		EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
		EditText etLastName = (EditText) findViewById(R.id.etLastName);
		EditText et_username = (EditText) findViewById(R.id.etUsername1);
		EditText et_pass = (EditText) findViewById(R.id.etPass1);

		// Define textViews
		TextView tvFirstNameAlert = (TextView) findViewById(R.id.tvFirstNameAlert);
		TextView tvLastNameAlert = (TextView) findViewById(R.id.tvLastNameAlert);
		TextView tvUnameAlert = (TextView) findViewById(R.id.tvUsernameAlert);
		TextView tvPassAlert = (TextView) findViewById(R.id.tvPassAlert);

		switch (v.getId()) {
		case R.id.bSignUp:

			// Validate inputs
			if (etFirstName.getText().toString().length() == 0) {
				tvFirstNameAlert.setText("Please insert a first name.");
			} else if (etFirstName.getText().toString().length() < 3) {
				tvFirstNameAlert
						.setText("First name must be at least 3 characters.");
			} else {
				tvFirstNameAlert.setText("");
			}

			if (etLastName.getText().toString().length() == 0) {
				tvLastNameAlert.setText("Please insert a last name.");
			} else if (etLastName.getText().toString().length() < 3) {
				tvLastNameAlert
						.setText("Last name must be at least 3 characters.");
			} else {
				tvLastNameAlert.setText("");
			}

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
			if (etFirstName.getText().toString().length() >= 3
					&& etLastName.getText().toString().length() >= 3
					&& et_username.getText().toString().length() >= 3
					&& et_pass.getText().toString().length() >= 3) {

				Toast.makeText(this, "Sign Up complete!", Toast.LENGTH_LONG)
						.show();

				DatabaseHelper db = new DatabaseHelper(getApplicationContext());
				db.insertValues(et_username.getText().toString(), et_pass
						.getText().toString());
				db.close();
			}
			break;
		}
	}
}
