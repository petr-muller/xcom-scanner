package cz.larpy.xcom.fieldscanner_v3;

import android.os.Parcel;
import android.os.Parcelable;

final public class RecordUnlockScannerAction implements ScannerAction,
    Parcelable {

  private String record;

  public RecordUnlockScannerAction(String pRecord) {
    record = pRecord;
  }

  public RecordUnlockScannerAction(Parcel parcel) {
    readFromParcel(parcel);
  }

  public static final Parcelable.Creator<RecordUnlockScannerAction> CREATOR = new Creator<RecordUnlockScannerAction>() {
    @Override
    public RecordUnlockScannerAction createFromParcel(Parcel source) {
      return new RecordUnlockScannerAction(source);
    }

    @Override
    public RecordUnlockScannerAction[] newArray(int size) {
      return new RecordUnlockScannerAction[size];
    }
  };

  @Override
  public String getMessage() {
    return "This action will unlock the record: " + record;
  }

  @Override
  public void execute() {
    // TODO Auto-generated method stub
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(record);
  }

  private void readFromParcel(Parcel parcel) {
    record = parcel.readString();
  }
}
