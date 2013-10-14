package cz.larpy.xcom.fieldscanner_v3.ScannerRecords;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.text.format.Time;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.RecordEntry;

public class ScannerRecord {
  private final String identifier;
  private boolean locked;
  private final boolean has_text;
  private final boolean has_image;
  private final boolean has_sound;
  private final String summary;
  private final String message;
  private Time unlocked = null;

  public static final ScannerRecord createRecordFromResources(String pId, String pSummary, String pMessage,
      Resources r, String packageName) {
    String textResourceName = "record_" + pId + "_text";
    String imageResourceName = "record_" + pId + "_image";
    String soundResourceName = "record_" + pId + "_sound";
    int txtId = r.getIdentifier(textResourceName, "raw", packageName);
    int sndId = r.getIdentifier(soundResourceName, "raw", packageName);
    int imgId = r.getIdentifier(imageResourceName, "raw", packageName);

    return new ScannerRecord(pId, true, pMessage, txtId > 0, sndId > 0, imgId > 0, pSummary);
  }

  private ScannerRecord(String pIdentifier, boolean pLocked, String pMessage, boolean pTxt, boolean pSnd, boolean pImg,
      String pSummary) {
    identifier = pIdentifier;
    summary = pSummary;
    locked = pLocked;
    message = pMessage;
    has_text = pTxt;
    has_sound = pSnd;
    has_image = pImg;
  }

  private ScannerRecord(String pIdentifier, boolean pLocked, String pMessage, boolean pTxt, boolean pSnd, boolean pImg,
      String pSummary, String pUnlocked) {
    identifier = pIdentifier;
    summary = pSummary;
    locked = pLocked;
    message = pMessage;
    has_text = pTxt;
    has_sound = pSnd;
    has_image = pImg;
    if (pUnlocked != null) {
      unlocked = new Time(Time.getCurrentTimezone());
      unlocked.parse(pUnlocked);
    }
  }

  public ContentValues asContentValues() {
    ContentValues cb = new ContentValues();
    cb.put(RecordEntry.COLUMN_NAME_IDENTIFIER, identifier);
    cb.put(RecordEntry.COLUMN_NAME_LOCKED, locked);
    cb.put(RecordEntry.COLUMN_NAME_HASTEXT, has_text);
    cb.put(RecordEntry.COLUMN_NAME_HASSOUND, has_sound);
    cb.put(RecordEntry.COLUMN_NAME_HASIMAGE, has_image);
    cb.put(RecordEntry.COLUMN_NAME_SUMMARY, summary);
    cb.put(RecordEntry.COLUMN_NAME_MESSAGE, message);
    cb.put(RecordEntry.COLUMN_NAME_WHEN_UNLOCKED, unlocked == null ? null : unlocked.format2445());

    return cb;
  }

  public String getMessage() {
    return message;
  }

  public String getSummary() {
    return summary;
  }

  public void unlock() {
    locked = false;
    unlocked = new Time(Time.getCurrentTimezone());
    unlocked.setToNow();
  }

  public static ScannerRecord createFromCursor(Cursor c) {
    String identifier = c.getString(c.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_IDENTIFIER));
    String summary = c.getString(c.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_SUMMARY));
    String message = c.getString(c.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_MESSAGE));
    boolean locked = c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_LOCKED)) != 0 ? true : false;
    boolean has_text = c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_HASTEXT)) != 0 ? true : false;
    boolean has_image = c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_HASIMAGE)) != 0 ? true : false;
    boolean has_sound = c.getInt(c.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_HASSOUND)) != 0 ? true : false;
    String timeUnlocked = c.getString(c.getColumnIndexOrThrow(RecordEntry.COLUMN_NAME_WHEN_UNLOCKED));

    return new ScannerRecord(identifier, locked, message, has_text, has_sound, has_image, summary, timeUnlocked);
  }

  @Override
  public String toString() {
    return summary;
  }

  public String getIdentifier() {
    return identifier;
  }

  public boolean hasText() {
    return has_text;
  }

  public boolean hasImage() {
    return has_image;
  }

  public boolean hasSound() {
    return has_sound;
  }

  public String getText(Context context) {
    Resources r = context.getResources();

    String textResourceName = "record_" + identifier + "_text";
    int txtId = r.getIdentifier(textResourceName, "raw", context.getPackageName());

    Scanner scanner = new Scanner(r.openRawResource(txtId));
    scanner.useDelimiter("\\Z");

    String ret = scanner.next();
    return ret;
  }

  public Drawable getDrawable(Context context) {
    Resources r = context.getResources();

    String imageResourceName = "record_" + identifier + "_image";
    int imgId = r.getIdentifier(imageResourceName, "raw", context.getPackageName());

    Drawable d = null;
    InputStream ims = r.openRawResource(imgId);
    d = Drawable.createFromStream(ims, null);

    return d;
  }

  public MediaPlayer getSoundPlayer(Context context) {
    Resources r = context.getResources();

    String soundResourceName = "record_" + identifier + "_sound";
    int sndId = r.getIdentifier(soundResourceName, "raw", context.getPackageName());
    try {
      AssetFileDescriptor fd = r.openRawResourceFd(sndId);
      MediaPlayer player = new MediaPlayer();
      player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
      player.prepare();
      return player;
    } catch (IOException e) {
      return null;
    }
  }
}
