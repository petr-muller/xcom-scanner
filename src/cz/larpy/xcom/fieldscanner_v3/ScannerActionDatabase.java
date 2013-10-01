package cz.larpy.xcom.fieldscanner_v3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public final class ScannerActionDatabase extends ScannerAction {
  private static enum COMMANDS {
    REPOPULATE
  }

  public static final ScannerActionDatabase repopulate() {
    return new ScannerActionDatabase(COMMANDS.REPOPULATE);
  }

  private final COMMANDS command;

  private ScannerActionDatabase(COMMANDS pCommand) {
    super();
    command = pCommand;
  }
  
  public static final Parcelable.Creator<ScannerActionDatabase> CREATOR = new Creator<ScannerActionDatabase>() {
    @Override
    public ScannerActionDatabase createFromParcel(Parcel source) {
      return new ScannerActionDatabase((COMMANDS) source.readSerializable());
    }

    @Override
    public ScannerActionDatabase[] newArray(int size) {
      return new ScannerActionDatabase[size];
    }
  };

  @Override
  public String getMessage() {
    switch(command) {
    case REPOPULATE:
      return "The database will be repopulated to the initial state";
    }
    return null;
  }

  @Override
  public void execute(Context context) {
    super.execute(context);
    switch(command) {
    case REPOPULATE:
      ScannerDbHelper dbHelper = new ScannerDbHelper(context);
      SQLiteDatabase db = dbHelper.getWritableDatabase();
      RecordManager recordManager = new RecordManager();
      recordManager.clear(db);
      recordManager.populate(db, context);
      db.close();
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    super.writeToParcel(dest, flags);
    dest.writeSerializable(command);
  }
}
