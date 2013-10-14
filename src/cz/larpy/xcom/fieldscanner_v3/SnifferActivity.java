package cz.larpy.xcom.fieldscanner_v3;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cz.larpy.xcom.fieldscanner_v3.ScannerRecords.RecordManager;
import cz.larpy.xcom.fieldscanner_v3.ScannerSniffer.SnifferLocation;
import cz.larpy.xcom.fieldscanner_v3.ScannerSniffer.SnifferLocationAdapter;
import cz.larpy.xcom.fieldscanner_v3.ScannerSniffer.SnifferLocationManager;

public class SnifferActivity extends Activity implements LocationListener {
  private SnifferLocationManager snifferLocationManager;
  private ScannerDbHelper dbHelper;
  private LocationManager androidLocationManager;
  private RecordManager recordManager;
  private Timer timer;
  private Location location = null;
  private ScannerParameters parameters = null;
  private boolean lost_connection = false;
  private int updates_after_lost_connection = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sniffer);
    snifferLocationManager = new SnifferLocationManager(this);
    dbHelper = new ScannerDbHelper(this);
    androidLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    parameters = ScannerParameters.load(this);
    recordManager = new RecordManager(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    timer = new Timer();
    timer.scheduleAtFixedRate(new LocationUpdateTask(), 3000, 1000);
    androidLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    update();
  }

  @Override
  protected void onPause() {
    super.onPause();
    timer.cancel();
    androidLocationManager.removeUpdates(this);
    location = null;
  }

  @Override
  public void onLocationChanged(Location pLocation) {
    lost_connection = false;
    updates_after_lost_connection = 0;
    location = pLocation;
  }

  @Override
  public void onProviderDisabled(String provider) {
    location = null;
  }

  @Override
  public void onProviderEnabled(String provider) {
    location = null;

  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    lost_connection = true;
  }

  public void updateStatusMessage(String pStatus) {
    TextView statusTw = (TextView) findViewById(R.id.snifferStatus);
    statusTw.setText(pStatus);
  }

  public void update() {
    ListView lw = (ListView) findViewById(R.id.snifferList);
    lw.setAdapter(null);

    if (lost_connection) {
      if (updates_after_lost_connection < 5) {
        updates_after_lost_connection += 1;
      } else {
        location = null;
      }
    }

    if (location == null) {
      updateStatusMessage("Probíhá scan, čekejte");
      return;
    }

    Time now = new Time(Time.getCurrentTimezone());
    now.setToNow();

    List<SnifferLocation> visibleSignals = snifferLocationManager.getVisibleSignals(dbHelper.getReadableDatabase(),
        now, location);

    if (visibleSignals == null) {
      updateStatusMessage("Žádný signál v dosahu");
      return;
    } else {
      updateStatusMessage("Zachyceno signálů=" + visibleSignals.size());
    }

    SQLiteDatabase writableDB = dbHelper.getWritableDatabase();
    for (SnifferLocation snifferLocation : visibleSignals) {
      if (snifferLocation.sniff(now, location, parameters.getSnifferSpeed())) {
        Toast.makeText(this, "Sniffing dokončen:_" + snifferLocation.getSummary(), Toast.LENGTH_SHORT).show();
        // recordManager.unlockRecord(writableDB, snifferLocation.getRecord());
      }
      snifferLocationManager.update(snifferLocation, writableDB);
    }

    SnifferLocationAdapter adapter = new SnifferLocationAdapter(this, R.layout.sniffer_item, visibleSignals);
    lw.setAdapter(adapter);
  };

  private class LocationUpdateTask extends TimerTask {

    @Override
    public void run() {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          SnifferActivity.this.update();
        }
      });
    }
  }
}
