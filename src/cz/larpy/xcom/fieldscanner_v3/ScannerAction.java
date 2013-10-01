package cz.larpy.xcom.fieldscanner_v3;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public abstract class ScannerAction implements Parcelable {
  public abstract String getMessage();
  public void execute(Context context) {
    executed = true;
  }

  protected ScannerAction(Parcel parcel) {
    executed = (parcel.readInt() != 0);
  }

  protected ScannerAction() {}

  public static final Parcelable.Creator<ScannerAction> CREATOR = new Creator<ScannerAction>() {
    @Override
    public ScannerAction createFromParcel(Parcel source) {
      throw new AssertionError("ScannerAction should not be instantiated");
    }

    @Override
    public ScannerAction[] newArray(int size) {
      return new ScannerAction[size];
    }
  };

  private boolean executed = false;
  public boolean notYetExecuted() {
    return (executed == false);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(executed ? 1 : 0);
  }
}
