package cz.larpy.xcom.fieldscanner_v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

public class ScannerActionActivity extends Activity {

  public static final String ACTIVITY = "ACTIVITY";
  private ScannerAction savedAction = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scanner_action);
    if (savedInstanceState != null) {
      savedAction = (ScannerAction) savedInstanceState.getParcelable(ACTIVITY);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();

    Intent i = getIntent();
    if (savedAction == null) {
      savedAction = i.getParcelableExtra(ACTIVITY);
    }

    if (savedAction.notYetExecuted()) {
      savedAction.execute(this);
    }
    TextView tv = (TextView) findViewById(R.id.activityFinishedText);
    tv.setText(savedAction.getMessage());
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(ACTIVITY, (Parcelable) savedAction);
  }
}
