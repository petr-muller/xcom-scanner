package cz.larpy.xcom.fieldscanner_v3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.RecordEntry;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbSql.Records;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public class RecordManager {
  private static Integer recordListResource = null;

  public void insert(SQLiteDatabase db, ScannerRecord pRecord) {
    db.insert(RecordEntry.TABLE_NAME, null, pRecord.asContentValues());
  }

  public void clear(SQLiteDatabase db) {
    db.execSQL(Records.DELETE);
    db.execSQL(Records.CREATE);
  }

  public void populate(SQLiteDatabase db, Context context) {
    Resources resources = context.getResources();
    String pkgName = context.getPackageName();

    if (recordListResource == null){
      recordListResource = resources.getIdentifier("records","raw", pkgName);
    }

    InputStream inputStream = resources.openRawResource(recordListResource);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    try {
      line = reader.readLine();
      while (line != null) {
        ScannerRecord record = ScannerRecord.createRecordFromResources(resources, line, pkgName);
        insert(db, record);
        line = reader.readLine();
      }
    } catch (IOException e) {}
  }

  public ScannerRecord unlockRecord(SQLiteDatabase db, String record) {
    return null;
  }
}
