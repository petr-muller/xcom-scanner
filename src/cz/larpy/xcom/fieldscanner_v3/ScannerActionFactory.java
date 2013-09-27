package cz.larpy.xcom.fieldscanner_v3;

final public class ScannerActionFactory {
  private static String RECORD_UNLOCK = "UNLOCK";

  public static ScannerAction createScannerAction(String pCommand) {
    String[] commandArray = pCommand.split(" ");
    String command = commandArray[0];

    ScannerAction action = null;

    if (command.equals(RECORD_UNLOCK)) {
      String identifier = commandArray[1];
      action = new RecordUnlockScannerAction(identifier);
    }

    return action;
  }
}
