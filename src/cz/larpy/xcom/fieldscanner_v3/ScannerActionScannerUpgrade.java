package cz.larpy.xcom.fieldscanner_v3;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

final public class ScannerActionScannerUpgrade extends ScannerAction {
  private String message =null;

  public ScannerActionScannerUpgrade() {
    super();
  }

  public ScannerActionScannerUpgrade(Parcel parcel) {
    super(parcel);
    message = parcel.readString();
  }

  public static final Parcelable.Creator<ScannerActionScannerUpgrade> CREATOR = new Creator<ScannerActionScannerUpgrade>() {
    @Override
    public ScannerActionScannerUpgrade createFromParcel(Parcel source) {
      return new ScannerActionScannerUpgrade(source);
    }

    @Override
    public ScannerActionScannerUpgrade[] newArray(int size) {
      return new ScannerActionScannerUpgrade[size];
    }
  };

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void execute(Context context) {
    super.execute(context);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeString(message);
  }
}
