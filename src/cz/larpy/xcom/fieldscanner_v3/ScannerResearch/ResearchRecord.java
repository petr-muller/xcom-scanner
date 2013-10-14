package cz.larpy.xcom.fieldscanner_v3.ScannerResearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.ResearchEntry;

public final class ResearchRecord {
  public static final ResearchRecord createFromResources(Resources pResources, String pPkgName, String pTrack,
      int pLevel, ResearchRecord pDiscovers) {
    String resourceName = "research_" + pTrack + "_" + String.valueOf(pLevel);
    int resourceId = pResources.getIdentifier(resourceName, "raw", pPkgName);

    InputStream inputStream = pResources.openRawResource(resourceId);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    String summary;
    String fluff;
    String rules;

    try {
      summary = reader.readLine();
      fluff = reader.readLine();
      rules = reader.readLine();
    } catch (IOException e) {
      return null;
    }

    ResearchRecord r = new ResearchRecord(summary, fluff, rules, pTrack, pLevel, pDiscovers);
    if (pLevel == 1) {
      r.show();
    }
    return r;
  }

  public static final ResearchRecord createFromCursor(Cursor c) {
    boolean visible = c.getInt(c.getColumnIndexOrThrow(ResearchEntry.COLUMN_NAME_VISIBLE)) != 0 ? true : false;
    String track = c.getString(c.getColumnIndexOrThrow(ResearchEntry.COLUMN_NAME_TRACK));
    String summary = c.getString(c.getColumnIndexOrThrow(ResearchEntry.COLUMN_NAME_SUMMARY));
    String rules = c.getString(c.getColumnIndexOrThrow(ResearchEntry.COLUMN_NAME_RULES));
    String fluff = c.getString(c.getColumnIndexOrThrow(ResearchEntry.COLUMN_NAME_FLUFF));
    int level = c.getInt(c.getColumnIndexOrThrow(ResearchEntry.COLUMN_NAME_LEVEL));
    boolean locked = c.getInt(c.getColumnIndexOrThrow(ResearchEntry.COLUMN_NAME_LOCKED)) != 0 ? true : false;

    return new ResearchRecord(summary, fluff, rules, track, level, null, locked, visible);
  }

  final private String summary;
  final private String track;
  final private int level;
  final private ResearchRecord makesVisible;
  private boolean visible = false;
  private boolean locked = true;
  final private String fluff;
  final private String rules;

  private ResearchRecord(String pSummary, String pFluff, String pRules, String pTrack, int pLevel,
      ResearchRecord pUnlocks, boolean pLocked, boolean pVisible) {
    summary = pSummary;
    fluff = pFluff;
    rules = pRules;
    track = pTrack;
    level = pLevel;
    makesVisible = pUnlocks;
    visible = pVisible;
    locked = pLocked;
  }

  private ResearchRecord(String pSummary, String pFluff, String pRules, String pTrack, int pLevel,
      ResearchRecord pUnlocks) {
    summary = pSummary;
    fluff = pFluff;
    rules = pRules;
    track = pTrack;
    level = pLevel;
    makesVisible = pUnlocks;
  }

  public void lock() {
    locked = true;
  }

  public void unlock() {
    locked = false;
  }

  public void hide() {
    visible = false;
  }

  public void show() {
    visible = true;
  }

  public ContentValues asContentValues() {
    ContentValues cv = new ContentValues();
    cv.put(ResearchEntry.COLUMN_NAME_VISIBLE, visible);
    cv.put(ResearchEntry.COLUMN_NAME_TRACK, track);
    cv.put(ResearchEntry.COLUMN_NAME_SUMMARY, summary);
    cv.put(ResearchEntry.COLUMN_NAME_RULES, rules);
    cv.put(ResearchEntry.COLUMN_NAME_LOCKED, locked);
    cv.put(ResearchEntry.COLUMN_NAME_LEVEL, level);
    cv.put(ResearchEntry.COLUMN_NAME_FLUFF, fluff);

    return cv;
  }

  public String getSummary() {
    return summary;
  }

  public boolean isLocked() {
    return locked;
  }

  public String getTrack() {
    return track;
  }

  public int getLevel() {
    return level;
  }

  public String getRules() {
    return rules;
  }

  public String getFluff() {
    return fluff;
  }
}
