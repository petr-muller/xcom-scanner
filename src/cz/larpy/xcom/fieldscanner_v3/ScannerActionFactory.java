package cz.larpy.xcom.fieldscanner_v3;

final public class ScannerActionFactory {
  private static final String DATABASE_RESET = "RESET";
  private static final String DATABASE = "DB";

  private static final String RECORD_UNLOCK = "UNLOCK";

  public static ScannerAction createScannerAction(String pCommand) {
    String[] commandArray = pCommand.split(" ");
    String command = commandArray[0];

    ScannerAction action = null;

    if (command.equals(RECORD_UNLOCK)) {
      String identifier = commandArray[1];
      action = new ScannerActionRecordUnlock(identifier);
    } else if (command.equals(DATABASE)) {
      String db_action = commandArray[1];
      if (db_action.equals(DATABASE_RESET)){
        action = ScannerActionDatabase.repopulate();
      }
    }

    return action;
  }
}
