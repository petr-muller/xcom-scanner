package cz.larpy.xcom.fieldscanner_v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HubActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hub);
    Button scanButton = (Button) findViewById(R.id.scannerButton);
    scanButton.setOnClickListener(initiateScan);
  }

  private final Button.OnClickListener initiateScan = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      IntentIntegrator integrator = new IntentIntegrator(HubActivity.this);
      integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }
  };

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
        resultCode, intent);
    if (scanResult != null) {
      String content = scanResult.getContents();
      ScannerAction action = ScannerActionFactory.createScannerAction(content);

      Intent i = new Intent(this, ScannerActionActivity.class);
      i.putExtra(ScannerActionActivity.ACTIVITY, (Parcelable) action);

      startActivity(i);
    }
  }
}
