package cz.larpy.xcom.fieldscanner_v3.ScannerRecords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.RecordEntry;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbSql.Records;

public class RecordManager {
  private static Integer recordListResource = null;
  private final Context context;

  public RecordManager(Context context) {
    this.context = context;
  }

  public void insert(SQLiteDatabase db, ScannerRecord pRecord) {
    db.insert(RecordEntry.TABLE_NAME, null, pRecord.asContentValues());
  }

  public void clear(SQLiteDatabase db) {
    db.execSQL(Records.DELETE);
    db.execSQL(Records.CREATE);
  }

  public void populate(SQLiteDatabase db) {
    Resources resources = context.getResources();
    String pkgName = context.getPackageName();

    if (recordListResource == null) {
      recordListResource = resources.getIdentifier("records", "raw", pkgName);
    }

    InputStream inputStream = resources.openRawResource(recordListResource);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    try {
      line = reader.readLine();
      while (line != null) {
        String[] items = line.split(",");
        String id = items[0];
        String summary = items[1];
        String message = items[2];
        ScannerRecord record = ScannerRecord.createRecordFromResources(id, summary, message, resources, pkgName);
        insert(db, record);
        line = reader.readLine();
      }
    } catch (IOException e) {
    }
  }

  public ScannerRecord unlockRecord(SQLiteDatabase db, String record) {
    ScannerRecord rc = getRecord(db, record);
    rc.unlock();
    save(db, rc);
    return rc;
  }

  private void save(SQLiteDatabase db, ScannerRecord rc) {
    ContentValues cv = rc.asContentValues();
    String selection = RecordEntry.COLUMN_NAME_IDENTIFIER + "=='" + rc.getIdentifier() + "'";

    db.update(RecordEntry.TABLE_NAME, cv, selection, null);
  }

  public List<ScannerRecord> getUnlockedRecords(SQLiteDatabase db) {
    ArrayList<ScannerRecord> records = new ArrayList<ScannerRecord>();

    String filter = ScannerDbContract.RecordEntry.COLUMN_NAME_LOCKED + "==0";
    String sortOrder = ScannerDbContract.RecordEntry.COLUMN_NAME_WHEN_UNLOCKED + " DESC";
    Cursor c = db.query(ScannerDbContract.RecordEntry.TABLE_NAME, null, filter, null, null, null, sortOrder);
    c.moveToFirst();
    while (!c.isAfterLast()) {
      ScannerRecord rc = ScannerRecord.createFromCursor(c);
      records.add(rc);
      c.moveToNext();
    }
    c.close();
    return records;
  }

  public ScannerRecord getRecord(SQLiteDatabase db, String identifier) {
    String filter = ScannerDbContract.RecordEntry.COLUMN_NAME_IDENTIFIER + "=='" + identifier + "'";
    Cursor c = db.query(ScannerDbContract.RecordEntry.TABLE_NAME, null, filter, null, null, null, null);

    c.moveToFirst();
    if (!c.isAfterLast()) {
      ScannerRecord rc = ScannerRecord.createFromCursor(c);
      c.close();
      return rc;
    }
    c.close();
    return null;
  }
}
