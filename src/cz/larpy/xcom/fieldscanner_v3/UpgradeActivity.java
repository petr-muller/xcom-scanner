package cz.larpy.xcom.fieldscanner_v3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cz.larpy.xcom.fieldscanner_v3.ScannerUpgrade.ScannerUpgrade;

public class UpgradeActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upgrade);
  }

  @Override
  protected void onResume() {
    super.onResume();
    ScannerParameters sp = ScannerParameters.load(this);

    TextView snifferSpeed = (TextView) findViewById(R.id.snifferSpeedLabel);
    snifferSpeed.setText(String.valueOf(sp.getSnifferSpeed()));

    ListView upgradeList = (ListView) findViewById(R.id.upgradeList);
    ScannerUpgrade[] upgrades = sp.getUpgrades();
    ArrayAdapter<ScannerUpgrade> adapter = new ArrayAdapter<ScannerUpgrade>(this, R.layout.upgrade_list_item, upgrades);

    upgradeList.setAdapter(adapter);
  }
}