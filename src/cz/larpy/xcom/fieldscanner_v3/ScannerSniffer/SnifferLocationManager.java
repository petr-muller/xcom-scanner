package cz.larpy.xcom.fieldscanner_v3.ScannerSniffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.format.Time;
import cz.larpy.xcom.fieldscanner_v3.R;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.GpsLocation;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbSql;

public class SnifferLocationManager {
  SharedPreferences researchData;
  Context context;

  public SnifferLocationManager(Context pContext) {
    context = pContext;
  }

  public void clear(SQLiteDatabase db) {
    db.execSQL(ScannerDbSql.Location.DELETE);
    db.execSQL(ScannerDbSql.Location.CREATE);
  }

  public void populate(SQLiteDatabase db) {
    Resources resources = context.getResources();
    InputStream inputStream = resources.openRawResource(R.raw.locations);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    try {
      line = reader.readLine();
      while (line != null) {
        SnifferLocation location = SnifferLocation.createLocationFromResources(line);
        insert(db, location);
        line = reader.readLine();
      }
    } catch (IOException e) {
    }
  }

  private void insert(SQLiteDatabase db, SnifferLocation location) {
    db.insert(ScannerDbContract.GpsLocation.TABLE_NAME, null, location.asContentValues());
  }

  public List<SnifferLocation> getVisibleSignals(SQLiteDatabase db, Time pTime, Location pLocation) {
    ArrayList<SnifferLocation> al = new ArrayList<SnifferLocation>();
    Cursor c = db.query(ScannerDbContract.GpsLocation.TABLE_NAME, null, null, null, null, null, null);
    c.moveToFirst();
    while (!c.isAfterLast()) {
      SnifferLocation location = SnifferLocation.createFromCursor(c);
      if (location.visible(pTime, pLocation)) {
        al.add(location);
      }
      c.moveToNext();
    }
    if (al.size() == 0) {
      return null;
    }
    return al;
  }

  public void update(SnifferLocation snifferLocation, SQLiteDatabase writableDB) {
    ContentValues cv = new ContentValues();
    cv.put(GpsLocation.COLUMN_NAME_SNIFFED, snifferLocation.getSniffed());
    String selection = GpsLocation.COLUMN_NAME_ID + " = '" + String.valueOf(snifferLocation.getIdentifier()) + "'";

    writableDB.update(GpsLocation.TABLE_NAME, cv, selection, null);
  }
}