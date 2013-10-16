package cz.larpy.xcom.fieldscanner_v3.ScannerActions;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import cz.larpy.xcom.fieldscanner_v3.ScannerAction;
import cz.larpy.xcom.fieldscanner_v3.ScannerPuzzle.ScannerPuzzleActivity;

public final class ScannerActionHack extends ScannerAction {
  private final int id;
  private final String side;
  private final String summary;

  public ScannerActionHack(int hack_id, String hack_side, String hack_summary) {
    super();
    id = hack_id;
    side = hack_side;
    summary = hack_summary;
  }

  private ScannerActionHack(Parcel parcel) {
    super(parcel);
    id = parcel.readInt();
    side = parcel.readString();
    summary = parcel.readString();
  }

  public static final Parcelable.Creator<ScannerActionHack> CREATOR = new Creator<ScannerActionHack>() {
    @Override
    public ScannerActionHack createFromParcel(Parcel source) {
      return new ScannerActionHack(source);
    }

    @Override
    public ScannerActionHack[] newArray(int size) {
      return new ScannerActionHack[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeInt(id);
    dest.writeString(side);
    dest.writeString(summary);
  }

  @Override
  public String getMessage() {
    return "Probíhá hack do systému: " + summary + "(terminál=" + side + ")";
  }

  @Override
  public Intent getIntent(Context context) {
    Intent i = new Intent(context, ScannerPuzzleActivity.class);
    i.putExtra(ScannerPuzzleActivity.HACKID, id);
    i.putExtra(ScannerPuzzleActivity.HACKSIDE, side);
    i.putExtra(ScannerPuzzleActivity.HACKSUMMARY, summary);

    return i;
  }
}
