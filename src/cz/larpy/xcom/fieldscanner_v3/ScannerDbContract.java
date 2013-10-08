package cz.larpy.xcom.fieldscanner_v3;

import android.provider.BaseColumns;

public final class ScannerDbContract {
  private ScannerDbContract() {};

  public static abstract class RecordEntry implements BaseColumns {
    public static final String TABLE_NAME = "record";
    public static final String COLUMN_NAME_RECORD_ID = "record_id";
    public static final String COLUMN_NAME_IDENTIFIER = "identifier";
    public static final String COLUMN_NAME_HASTEXT = "has_text";
    public static final String COLUMN_NAME_HASSOUND = "has_sound";
    public static final String COLUMN_NAME_HASIMAGE = "has_image";
    public static final String COLUMN_NAME_LOCKED = "locked";
    public static final String COLUMN_NAME_WHEN_UNLOCKED = "when_unlocked"; 
  }

  public static abstract class ResearchEntry implements BaseColumns {
    public static final String TABLE_NAME = "research_item";
    public static final String COLUMN_NAME_SUMMARY = "summary";
    public static final String COLUMN_NAME_TRACK = "track";
    public static final String COLUMN_NAME_LEVEL = "level";
    public static final String COLUMN_NAME_DISCOVERS = "makes_visible";
    public static final String COLUMN_NAME_VISIBLE = "visible";
    public static final String COLUMN_NAME_LOCKED = "locked";
    public static final String COLUMN_NAME_FLUFF = "fluff";
    public static final String COLUMN_NAME_RULES = "rules";
    public static String[] getAllColumns() {
      String[] cols = { COLUMN_NAME_SUMMARY, COLUMN_NAME_TRACK, COLUMN_NAME_LEVEL,
                        COLUMN_NAME_DISCOVERS, COLUMN_NAME_VISIBLE, COLUMN_NAME_LOCKED,
                        COLUMN_NAME_FLUFF, COLUMN_NAME_FLUFF };
      return cols;
    }
  }

  public static abstract class GpsLocation implements BaseColumns {
    public static final String TABLE_NAME = "location";
    public static final String COLUMN_NAME_SUMMARY = "summary";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGITUDE = "longitude";
    public static final String COLUMN_NAME_SCANNING_PROFILE = "location";
    public static final String COLUMN_NAME_SCANNING_DURATION = "duration";
    public static final String COLUMN_NAME_START = "start";
    public static final String COLUMN_NAME_STOP = "stop";
    public static final String COLUMN_NAME_RECORD = "record";
  }
}