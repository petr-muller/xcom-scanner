package cz.larpy.xcom.fieldscanner_v3.ScannerResearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cz.larpy.xcom.fieldscanner_v3.R;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.ResearchEntry;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbSql;

public class ResearchManager {
  SharedPreferences researchData;
  Context context;

  public ResearchManager(Context pContext) {
    context = pContext;
    researchData = context.getSharedPreferences(context.getString(R.string.research_data_shpref_key),
        Context.MODE_PRIVATE);
  }

  private static Integer researchListResource = null;

  public void insert(SQLiteDatabase db, ResearchRecord pRecord) {
    db.insert(ResearchEntry.TABLE_NAME, null, pRecord.asContentValues());
  }

  public void clear(SQLiteDatabase db) {
    db.execSQL(ScannerDbSql.Research.DELETE);
    db.execSQL(ScannerDbSql.Research.CREATE);
  }

  public void populate(SQLiteDatabase db) {
    Resources resources = context.getResources();
    String pkgName = context.getPackageName();

    if (researchListResource == null) {
      researchListResource = resources.getIdentifier("research", "raw", pkgName);
    }

    InputStream inputStream = resources.openRawResource(researchListResource);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    try {
      line = reader.readLine();
      while (line != null) {
        String[] track_record = line.split(" ");
        String track = track_record[0];
        String track_count_string = track_record[1];
        int track_count = Integer.valueOf(track_count_string);

        ResearchRecord forward = null;
        for (int i = track_count; i > 0; i--) {
          ResearchRecord record = ResearchRecord.createFromResources(resources, pkgName, track, i, forward);
          insert(db, record);
          forward = record;
        }
        line = reader.readLine();
      }
    } catch (IOException e) {
    }
  }

  public int getResearchPoints() {
    return researchData.getInt(context.getString(R.string.research_points_shpref_key), 0);
  }

  public boolean addResearchPoint(String pKey) {
    boolean alreadyIn = researchData.getBoolean(pKey, false);
    if (alreadyIn) {
      return false;
    }

    int currentPoints = getResearchPoints();
    SharedPreferences.Editor editor = researchData.edit();
    editor.putBoolean(pKey, true);
    editor.putInt(context.getString(R.string.research_points_shpref_key), currentPoints + 1);
    editor.commit();

    return true;
  }

  public ResearchRecord[] getVisibleResearch(SQLiteDatabase db) {
    ArrayList<ResearchRecord> al = new ArrayList<ResearchRecord>();

    String filterByVisible = ScannerDbContract.ResearchEntry.COLUMN_NAME_VISIBLE + " != 0";
    String orderByTrackAndLevel = ScannerDbContract.ResearchEntry.COLUMN_NAME_TRACK + ","
        + ScannerDbContract.ResearchEntry.COLUMN_NAME_LEVEL;

    Cursor c = db.query(ScannerDbContract.ResearchEntry.TABLE_NAME, null, filterByVisible, null, null, null,
        orderByTrackAndLevel);
    c.moveToFirst();
    while (!c.isAfterLast()) {
      ResearchRecord rc = ResearchRecord.createFromCursor(c);
      c.moveToNext();
      al.add(rc);
    }
    return al.toArray(new ResearchRecord[al.size()]);
  }

  public ResearchRecord getResearchRecord(String track, int level, SQLiteDatabase db) {
    String filter = ScannerDbContract.ResearchEntry.COLUMN_NAME_TRACK + "=='" + track + "' AND "
        + ScannerDbContract.ResearchEntry.COLUMN_NAME_LEVEL + "==" + level;
    Cursor c = db.query(ScannerDbContract.ResearchEntry.TABLE_NAME, null, filter, null, null, null, null);

    c.moveToFirst();
    while (!c.isAfterLast()) {
      ResearchRecord rc = ResearchRecord.createFromCursor(c);
      c.close();
      return rc;
    }
    return null;
  }

  public void unlock(String track, int level, SQLiteDatabase db) {
    ContentValues unlockCV = new ContentValues();
    unlockCV.put(ScannerDbContract.ResearchEntry.COLUMN_NAME_LOCKED, false);

    ContentValues showCV = new ContentValues();
    showCV.put(ScannerDbContract.ResearchEntry.COLUMN_NAME_VISIBLE, 1);

    String unlockFilter = ScannerDbContract.ResearchEntry.COLUMN_NAME_TRACK + "=='" + track + "' AND "
        + ScannerDbContract.ResearchEntry.COLUMN_NAME_LEVEL + "==" + level;

    String showFilter = ScannerDbContract.ResearchEntry.COLUMN_NAME_TRACK + "=='" + track + "' AND "
        + ScannerDbContract.ResearchEntry.COLUMN_NAME_LEVEL + "==" + (1 + level);

    db.update(ScannerDbContract.ResearchEntry.TABLE_NAME, unlockCV, unlockFilter, null);
    db.update(ScannerDbContract.ResearchEntry.TABLE_NAME, showCV, showFilter, null);

    int currentPoints = getResearchPoints();
    SharedPreferences.Editor editor = researchData.edit();
    editor.putInt(context.getString(R.string.research_points_shpref_key), currentPoints - 1);
    editor.commit();
  }
}
