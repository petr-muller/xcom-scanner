package cz.larpy.xcom.fieldscanner_v3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.ResearchEntry;
import android.content.ContentValues;
import android.content.res.Resources;

public final class ResearchRecord {
  public static final ResearchRecord createFromResources(Resources pResources, String pPkgName, String pTrack, int pLevel, ResearchRecord pDiscovers) {
    String resourceName = "research_" + pTrack + "_" + String.valueOf(pLevel);
    int resourceId = pResources.getIdentifier(resourceName,"raw", pPkgName);

    InputStream inputStream = pResources.openRawResource(resourceId);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    String summary;
    String fluff;
    String rules;

    try {
      summary = reader.readLine();
      fluff = reader.readLine();
      rules = reader.readLine();
    } catch(IOException e) {
      return null;
    }

    ResearchRecord r = new ResearchRecord(summary, fluff, rules, pTrack, pLevel, pDiscovers);
    if (pLevel == 0) {
      r.show();
    }
    return r;
  }

  final private String summary;
  final private String track;
  final private int level;
  final private ResearchRecord makesVisible;
  private boolean visible = false;
  private boolean locked = true;
  final private String fluff;
  final private String rules;

  private ResearchRecord(String pSummary, String pFluff, String pRules, String pTrack, int pLevel, ResearchRecord pUnlocks) {
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
}
