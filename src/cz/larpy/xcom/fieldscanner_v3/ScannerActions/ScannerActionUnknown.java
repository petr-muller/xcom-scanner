package cz.larpy.xcom.fieldscanner_v3.ScannerActions;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import cz.larpy.xcom.fieldscanner_v3.HubActivity;
import cz.larpy.xcom.fieldscanner_v3.ScannerAction;

public final class ScannerActionUnknown extends ScannerAction {

  public static final Parcelable.Creator<ScannerActionUnknown> CREATOR = new Creator<ScannerActionUnknown>() {
    @Override
    public ScannerActionUnknown createFromParcel(Parcel source) {
      return new ScannerActionUnknown(source);
    }

    @Override
    public ScannerActionUnknown[] newArray(int size) {
      return new ScannerActionUnknown[size];
    }
  };

  public ScannerActionUnknown(Parcel source) {
    super(source);
  }

  public ScannerActionUnknown() {
    super();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public String getMessage() {
    return "Neznámá operace (bug)";
  }

  @Override
  public Intent getIntent(Context context) {
    return new Intent(context, HubActivity.class);
  }

}
