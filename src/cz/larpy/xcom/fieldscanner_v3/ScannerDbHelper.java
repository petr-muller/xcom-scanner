package cz.larpy.xcom.fieldscanner_v3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScannerDbHelper extends SQLiteOpenHelper {
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "XCOMScanner.db";

  public ScannerDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  private void createAllTables(SQLiteDatabase db) {
    db.execSQL(ScannerDbSql.Records.CREATE);
    db.execSQL(ScannerDbSql.Research.CREATE);
    db.execSQL(ScannerDbSql.Location.CREATE);
  }
  private void dropAllTables(SQLiteDatabase db) {
    db.execSQL(ScannerDbSql.Records.DELETE);
    db.execSQL(ScannerDbSql.Research.DELETE);
    db.execSQL(ScannerDbSql.Location.DELETE);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    createAllTables(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    clear(db);
  }

  public void clear(SQLiteDatabase db) {
    dropAllTables(db);
    createAllTables(db);
  }
}
