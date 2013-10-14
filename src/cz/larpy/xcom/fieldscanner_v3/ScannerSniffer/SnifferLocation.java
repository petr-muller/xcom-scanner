package cz.larpy.xcom.fieldscanner_v3.ScannerSniffer;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.text.format.Time;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.GpsLocation;
import cz.larpy.xcom.fieldscanner_v3.ScannerParameters;

public class SnifferLocation {
  final private String summary;
  final private Location coords;
  final private String profile;
  final private Time start;
  final private Time end;
  final private Time optimum;
  final private boolean timed;
  final private String record;
  final private int needed;
  final private int id;
  private int sniffed;
  private Double signalStrength = null;
  private Float distance = null;
  private Double distanceRatio = null;
  private Double timeRatio = null;

  public SnifferLocation(String pSummary, double pLat, double pLon, String pProfile, Time pStart, Time pEnd,
      Time pOptimum, boolean pTimed, String pRecord, int pNeeded, int pSniffed, int pId) {
    summary = pSummary;
    coords = new Location("pSummary");
    coords.setLatitude(pLat);
    coords.setLongitude(pLon);
    profile = pProfile;
    start = pStart;
    end = pEnd;
    optimum = pOptimum;
    timed = pTimed;
    record = pRecord;
    needed = pNeeded;
    sniffed = pSniffed;
    id = pId;
  }

  public static SnifferLocation createLocationFromResources(String line) {
    String[] items = line.split(",");

    String summary = items[0];
    double latitude = Double.valueOf(items[1]);
    double longitude = Double.valueOf(items[2]);
    String profile = items[3];
    Time start = new Time(Time.getCurrentTimezone());
    start.parse(items[4]);
    Time end = new Time(Time.getCurrentTimezone());
    end.parse(items[5]);
    Time optimum = new Time(Time.getCurrentTimezone());
    optimum.parse(items[6]);
    boolean timed = Boolean.valueOf(items[7]);
    String record = items[8];
    int needed = Integer.valueOf(items[9]);
    int id = Integer.valueOf(items[10]);

    return new SnifferLocation(summary, latitude, longitude, profile, start, end, optimum, timed, record, needed, 0, id);
  }

  public ContentValues asContentValues() {
    ContentValues cv = new ContentValues();

    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_SUMMARY, summary);
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_LATITUDE, coords.getLatitude());
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_LONGITUDE, coords.getLongitude());
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_SCANNING_PROFILE, profile);
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_START, start.format2445());
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_END, end.format2445());
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_OPTIMUM, optimum.format2445());
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_TIMED, timed);
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_RECORD, record);
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_NEEDED, needed);
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_SNIFFED, sniffed);
    cv.put(ScannerDbContract.GpsLocation.COLUMN_NAME_ID, id);

    return cv;
  }

  public String getSummary() {
    return summary;
  }

  public int getNeeded() {
    return needed;
  }

  public int getSniffed() {
    return sniffed;
  }

  public static SnifferLocation createFromCursor(Cursor c) {
    String summary = c.getString(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_SUMMARY));
    double latitude = c.getDouble(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_LATITUDE));
    double longitude = c.getDouble(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_LONGITUDE));
    String profile = c.getString(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_SCANNING_PROFILE));
    Time start = new Time(Time.getCurrentTimezone());
    start.parse(c.getString(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_START)));
    Time end = new Time(Time.getCurrentTimezone());
    end.parse(c.getString(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_END)));
    Time optimum = new Time(Time.getCurrentTimezone());
    optimum.parse(c.getString(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_OPTIMUM)));
    boolean timed = c.getInt(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_TIMED)) != 0 ? true : false;
    String record = c.getString(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_RECORD));
    int needed = c.getInt(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_NEEDED));
    int sniffed = c.getInt(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_SNIFFED));
    int id = c.getInt(c.getColumnIndexOrThrow(GpsLocation.COLUMN_NAME_ID));

    return new SnifferLocation(summary, latitude, longitude, profile, start, end, optimum, timed, record, needed,
        sniffed, id);
  }

  public boolean visible(Time pTime, Location pLocation) {
    if (discovered()) {
      return false;
    }
    if (!visibleInTime(pTime)) {
      return false;
    }
    if (!visibleAtLocation(pLocation)) {
      return false;
    }
    return true;
  }

  private boolean visibleAtLocation(Location pLocation) {
    float distance = pLocation.distanceTo(coords);
    return (distance < 50);
  }

  private boolean visibleInTime(Time pTime) {
    boolean visible = ((!timed) || (pTime.after(start) && pTime.before(end)));
    return visible;
  }

  private boolean discovered() {
    return (sniffed >= needed);
  }

  private double getDistanceRatio(Location location) {
    distance = location.distanceTo(coords);
    double distanceRatio = (ScannerParameters.DISTANCE_THRESHOLD - distance.doubleValue())
        / ScannerParameters.DISTANCE_THRESHOLD;

    return distanceRatio;
  }

  private double getTimeRatio(Time time) {
    if (!(timed && profile.equals("optimized"))) {
      return 1.0;
    }
    long optimumMS = optimum.toMillis(false);
    long timeMS = time.toMillis(false);

    Long spread;
    Long distance;
    if (time.before(optimum)) {
      long startMS = start.toMillis(false);
      spread = optimumMS - startMS;
      distance = optimumMS - timeMS;
    } else {
      long endMS = end.toMillis(false);
      spread = endMS - optimumMS;
      distance = timeMS - optimumMS;
    }

    double timeRatio = (spread.doubleValue() - distance.doubleValue()) / spread.doubleValue();
    return timeRatio;
  }

  public boolean sniff(Time now, Location location, float snifferSpeed) {
    distanceRatio = getDistanceRatio(location);
    timeRatio = getTimeRatio(now);

    double finalRatio = (distanceRatio + timeRatio) / 2.0;
    signalStrength = snifferSpeed * finalRatio;

    sniffed += signalStrength;

    return (sniffed > needed);
  }

  public Double getSignalStrength() {
    return signalStrength;
  }

  public int getIdentifier() {
    return id;
  }

  public double getDistance() {
    return distance;
  }

  public double getDistanceR() {
    return distanceRatio;
  }

  public double getTimeR() {
    return timeRatio;
  }

  public String getRecord() {
    return record;
  }
};
