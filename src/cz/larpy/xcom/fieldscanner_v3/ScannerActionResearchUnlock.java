package cz.larpy.xcom.fieldscanner_v3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

final public class ScannerActionResearchUnlock extends ScannerAction {

  private String recordIdentifier;
  private String message =null;

  public ScannerActionResearchUnlock(String pRecord) {
    super();
    recordIdentifier = pRecord;
  }

  public ScannerActionResearchUnlock(Parcel parcel) {
    super(parcel);
    recordIdentifier = parcel.readString();
    message = parcel.readString();
  }

  public static final Parcelable.Creator<ScannerActionResearchUnlock> CREATOR = new Creator<ScannerActionResearchUnlock>() {
    @Override
    public ScannerActionResearchUnlock createFromParcel(Parcel source) {
      return new ScannerActionResearchUnlock(source);
    }

    @Override
    public ScannerActionResearchUnlock[] newArray(int size) {
      return new ScannerActionResearchUnlock[size];
    }
  };

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void execute(Context context) {
    RecordManager manager = new RecordManager();
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
}
