package cz.larpy.xcom.fieldscanner_v3;

import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.GpsLocation;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.RecordEntry;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.ResearchEntry;

public final class ScannerDbSql {
  private ScannerDbSql() {};

  private static final String TEXT_TYPE = " TEXT";
  private static final String INTEGER_TYPE = " INTEGER";
  private static final String PK = " PRIMARY KEY";
  private static final String NN = " NOT NULL";
  private static final String AI = " AUTOINCREMENT";
  private static final String DEFAULT = " DEFAULT";
  
  private static final String COMMA_SEP = ",";

  public static final class Records {

    public static final String CREATE =
        "CREATE TABLE " + RecordEntry.TABLE_NAME + " (" +
            RecordEntry.COLUMN_NAME_RECORD_ID + INTEGER_TYPE + PK + AI + COMMA_SEP +
            RecordEntry.COLUMN_NAME_IDENTIFIER + TEXT_TYPE + NN + COMMA_SEP +
            RecordEntry.COLUMN_NAME_HASTEXT + INTEGER_TYPE + NN + COMMA_SEP +
            RecordEntry.COLUMN_NAME_HASSOUND + INTEGER_TYPE + NN + COMMA_SEP +
            RecordEntry.COLUMN_NAME_HASIMAGE + INTEGER_TYPE + NN + COMMA_SEP +
            RecordEntry.COLUMN_NAME_LOCKED + INTEGER_TYPE + NN + DEFAULT + " 1" + COMMA_SEP +
            RecordEntry.COLUMN_NAME_WHEN_UNLOCKED + TEXT_TYPE +
        " )";

    public static final String DELETE = "DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME;
  }

  public static final class Research {
    public static final String CREATE =
        "CREATE TABLE" + ResearchEntry.TABLE_NAME + " (" +
            ResearchEntry.COLUMN_NAME_VISIBLE + INTEGER_TYPE + NN + COMMA_SEP +
            ResearchEntry.COLUMN_NAME_TRACK + TEXT_TYPE + NN + COMMA_SEP +
            ResearchEntry.COLUMN_NAME_SUMMARY + TEXT_TYPE + NN + COMMA_SEP +
            ResearchEntry.COLUMN_NAME_RULES + TEXT_TYPE + NN + COMMA_SEP +
            ResearchEntry.COLUMN_NAME_LOCKED + INTEGER_TYPE + NN + COMMA_SEP +
            ResearchEntry.COLUMN_NAME_FLUFF + TEXT_TYPE + NN + COMMA_SEP +
            ResearchEntry.COLUMN_NAME_LEVEL + INTEGER_TYPE + NN +
        " )";
    public static final String DELETE = "DROP TABLE IF EXISTS " + ResearchEntry.TABLE_NAME;
  }

  public static final class Location {
    public static final String CREATE =
        "CREATE TABLE" + GpsLocation.TABLE_NAME + " (" +
            GpsLocation.COLUMN_NAME_SUMMARY + TEXT_TYPE + NN + COMMA_SEP +
            GpsLocation.COLUMN_NAME_START + TEXT_TYPE + COMMA_SEP +
            GpsLocation.COLUMN_NAME_STOP + TEXT_TYPE + COMMA_SEP +
            GpsLocation.COLUMN_NAME_SCANNING_PROFILE + TEXT_TYPE + NN + COMMA_SEP +
            GpsLocation.COLUMN_NAME_SCANNING_DURATION + INTEGER_TYPE + NN + COMMA_SEP +
            GpsLocation.COLUMN_NAME_LATITUDE + TEXT_TYPE + NN + COMMA_SEP +
            GpsLocation.COLUMN_NAME_LONGITUDE + TEXT_TYPE + NN + COMMA_SEP +
            " )";
    public static final String DELETE = "DROP TABLE IF EXISTS " + GpsLocation.TABLE_NAME;
  }
}
