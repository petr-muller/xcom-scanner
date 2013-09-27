package cz.larpy.xcom.fieldscanner_v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

    Intent i = getIntent();
    ScannerAction action = i.getParcelableExtra(ACTIVITY);
    TextView tv = (TextView) findViewById(R.id.activityFinishedText);

    action.execute();
    tv.setText(action.getMessage());
  }
}
