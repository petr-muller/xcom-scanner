package cz.larpy.xcom.fieldscanner_v3;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class ScannerActionActivity extends Activity {

	public static final String ACTIVITY = "ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner_action);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}
