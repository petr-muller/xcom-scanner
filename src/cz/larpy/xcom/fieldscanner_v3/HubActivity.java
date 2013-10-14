package cz.larpy.xcom.fieldscanner_v3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import cz.larpy.xcom.fieldscanner_v3.ScannerRecords.RecordListActivity;
import cz.larpy.xcom.fieldscanner_v3.ScannerResearch.ResearchActivity;

public class HubActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hub);
    Button scanButton = (Button) findViewById(R.id.scannerButton);
    scanButton.setOnClickListener(initiateScan);

    Button recordListButton = (Button) findViewById(R.id.recordsButton);
    recordListButton.setOnClickListener(goToRecordList);

    Button upgradesButton = (Button) findViewById(R.id.upgradeButton);
    upgradesButton.setOnClickListener(goToUpgrades);

    Button snifferButton = (Button) findViewById(R.id.snifferButton);
    snifferButton.setOnClickListener(startSniffer);

    Button researchButton = (Button) findViewById(R.id.researchButton);
    researchButton.setOnClickListener(goToResearch);
  }

  private final Button.OnClickListener initiateScan = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      IntentIntegrator integrator = new IntentIntegrator(HubActivity.this);
      integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }
  };

  private final Button.OnClickListener goToRecordList = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      Intent i = new Intent(HubActivity.this, RecordListActivity.class);
      startActivity(i);
    }
  };

  private final Button.OnClickListener goToUpgrades = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      Intent i = new Intent(HubActivity.this, UpgradeActivity.class);
      startActivity(i);
    }
  };

  private final Button.OnClickListener startSniffer = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      Intent i = new Intent(HubActivity.this, SnifferActivity.class);
      startActivity(i);
    }
  };

  private final Button.OnClickListener goToResearch = new Button.OnClickListener() {
    @Override
    public void onClick(View v) {
      Intent i = new Intent(HubActivity.this, ResearchActivity.class);
      startActivity(i);
    }
  };

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    if (scanResult != null) {
      String content = scanResult.getContents();
      if (content != null) {
        ScannerAction action = ScannerActionFactory.createScannerAction(content);

        Intent i = new Intent(this, ScannerActionActivity.class);
        i.putExtra(ScannerActionActivity.ACTIVITY, action);
        startActivity(i);
      }
    }
  }
}
