package cz.larpy.xcom.fieldscanner_v3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.ResearchEntry;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public class ResearchManager {
  private static Integer researchListResource = null;

  public void insert(SQLiteDatabase db, ResearchRecord pRecord) {
    db.insert(ResearchEntry.TABLE_NAME, null, pRecord.asContentValues());
  }

  public void clear(SQLiteDatabase db) {
    db.execSQL(ScannerDbSql.Research.DELETE);
    db.execSQL(ScannerDbSql.Research.CREATE);
  }

  public void populate(SQLiteDatabase db, Context context) {
    Resources resources = context.getResources();
    String pkgName = context.getPackageName();

    if (researchListResource == null){
      researchListResource = resources.getIdentifier("research","raw", pkgName);
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
        for (int i = track_count - 1; i >= 0; i--) {
          ResearchRecord record = ResearchRecord.createFromResources(resources, pkgName, track, i, forward);
          insert(db, record);
          forward = record;
        }
      }
    } catch (IOException e) {}
  }
}
