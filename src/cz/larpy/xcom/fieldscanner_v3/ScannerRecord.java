package cz.larpy.xcom.fieldscanner_v3;

import cz.larpy.xcom.fieldscanner_v3.ScannerDbContract.RecordEntry;
import android.content.ContentValues;
import android.content.res.Resources;

public class ScannerRecord {
  private Integer db_id = null;
  private final String identifier;
  private final boolean locked;
  private final boolean has_text;
  private final boolean has_image;
  private final boolean has_sound;
  private final String message;

  public static final ScannerRecord createRecordFromResources(Resources r, String pId, String packageName) {
    String textResourceName = "record_" + pId + "_text";
    String imageResourceName = "record_" + pId + "_image";
    String soundResourceName = "record_" + pId + "_sound";
    int txtId = r.getIdentifier(textResourceName, "raw", packageName);
    int sndId = r.getIdentifier(soundResourceName, "raw", packageName);
    int imgId = r.getIdentifier(imageResourceName, "raw", packageName);

    return new ScannerRecord(pId, true, "Record " + pId + "was unlocked", txtId>0, sndId>0, imgId>0);
  }

  private ScannerRecord(String pIdentifier, boolean pLocked, String pMessage,
                        boolean pTxt, boolean pSnd, boolean pImg) {
    identifier = pIdentifier;
    locked = pLocked;
    message = pMessage;
    has_text = pTxt;
    has_sound = pSnd;
    has_image = pImg;
  }

  public ContentValues asContentValues() {
    ContentValues cb = new ContentValues();
    cb.put(RecordEntry.COLUMN_NAME_IDENTIFIER, identifier);
    cb.put(RecordEntry.COLUMN_NAME_LOCKED, locked);
    cb.put(RecordEntry.COLUMN_NAME_HASTEXT, has_text);
    cb.put(RecordEntry.COLUMN_NAME_HASSOUND, has_sound);
    cb.put(RecordEntry.COLUMN_NAME_HASIMAGE,  has_image);

    return cb;
  }

  public String getMessage() {
    return message;
  }
}
