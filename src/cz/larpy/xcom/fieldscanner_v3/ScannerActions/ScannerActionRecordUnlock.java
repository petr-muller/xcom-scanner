package cz.larpy.xcom.fieldscanner_v3.ScannerActions;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import cz.larpy.xcom.fieldscanner_v3.ScannerAction;
import cz.larpy.xcom.fieldscanner_v3.ScannerDbHelper;
import cz.larpy.xcom.fieldscanner_v3.ScannerRecords.RecordListActivity;
import cz.larpy.xcom.fieldscanner_v3.ScannerRecords.RecordManager;
import cz.larpy.xcom.fieldscanner_v3.ScannerRecords.ScannerRecord;

final public class ScannerActionRecordUnlock extends ScannerAction {

  private final String recordIdentifier;
  private String message = null;

  public ScannerActionRecordUnlock(String pRecord) {
    super();
    recordIdentifier = pRecord;
  }

  public ScannerActionRecordUnlock(Parcel parcel) {
    super(parcel);
    recordIdentifier = parcel.readString();
    message = parcel.readString();
  }

  public static final Parcelable.Creator<ScannerActionRecordUnlock> CREATOR = new Creator<ScannerActionRecordUnlock>() {
    @Override
    public ScannerActionRecordUnlock createFromParcel(Parcel source) {
      return new ScannerActionRecordUnlock(source);
    }

    @Override
    public ScannerActionRecordUnlock[] newArray(int size) {
      return new ScannerActionRecordUnlock[size];
    }
  };

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void execute(Context context) {
    RecordManager manager = new RecordManager(context);
    ScannerDbHelper helper = new ScannerDbHelper(context);
    SQLiteDatabase db = helper.getWritableDatabase();

    ScannerRecord record = manager.unlockRecord(db, recordIdentifier);
    message = record.getMessage();
    super.execute(context);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(recordIdentifier);
    dest.writeString(message);
  }

  @Override
  public Intent getIntent(Context context) {
    Intent i = new Intent(context, RecordListActivity.class);
    return i;
  }
}
