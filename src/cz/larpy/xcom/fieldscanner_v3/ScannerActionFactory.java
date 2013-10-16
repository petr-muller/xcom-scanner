package cz.larpy.xcom.fieldscanner_v3;

import cz.larpy.xcom.fieldscanner_v3.ScannerActions.ScannerActionDatabase;
import cz.larpy.xcom.fieldscanner_v3.ScannerActions.ScannerActionHack;
import cz.larpy.xcom.fieldscanner_v3.ScannerActions.ScannerActionRecordUnlock;
import cz.larpy.xcom.fieldscanner_v3.ScannerActions.ScannerActionResearchPoint;
import cz.larpy.xcom.fieldscanner_v3.ScannerActions.ScannerActionScannerUpgrade;
import cz.larpy.xcom.fieldscanner_v3.ScannerActions.ScannerActionUnknown;
import cz.larpy.xcom.fieldscanner_v3.ScannerActions.ScannerActionUpgradeClear;
import cz.larpy.xcom.fieldscanner_v3.ScannerUpgrade.ScannerUpgrade;
import cz.larpy.xcom.fieldscanner_v3.ScannerUpgrade.ScannerUpgradeSnifferSpeed;

final public class ScannerActionFactory {
  private static final String DATABASE_RESET = "RESET";
  private static final String DATABASE = "DB";

  private static final String RECORD_UNLOCK = "UNLOCK";

  private static final String UPGRADE = "UPGRADE";
  private static final String SNIFFERSPEED = "SNIFFERSPEED";
  private static final String UPGRADE_RESET = "RESET";

  private static final String RESEARCH = "RESEARCH";
  private static final String RESPOINT = "POINT";

  private static final String HACK = "HACK";

  public static ScannerAction createScannerAction(String pCommand) {
    String[] commandArray = pCommand.split(" ");
    String command = commandArray[0];

    ScannerAction action = null;

    if (command.equals(RECORD_UNLOCK)) {
      String identifier = commandArray[1];
      action = new ScannerActionRecordUnlock(identifier);
    } else if (command.equals(DATABASE)) {
      String db_action = commandArray[1];
      if (db_action.equals(DATABASE_RESET)) {
        action = ScannerActionDatabase.repopulate();
      }
    } else if (command.equals(UPGRADE)) {
      String upgrade_type = commandArray[1];

      if (upgrade_type.equals(SNIFFERSPEED)) {
        int upgrade_id = Integer.valueOf(commandArray[2]);
        float modifier = Float.valueOf(commandArray[3]);
        ScannerUpgrade upgrade = new ScannerUpgradeSnifferSpeed(upgrade_id, modifier);
        action = new ScannerActionScannerUpgrade(upgrade);
      } else if (upgrade_type.equals(UPGRADE_RESET)) {
        action = new ScannerActionUpgradeClear();
      }
    } else if (command.equals(RESEARCH)) {
      String research_command = commandArray[1];
      if (research_command.equals(RESPOINT)) {
        String research_point_key = commandArray[2];
        action = new ScannerActionResearchPoint(research_point_key);
      }
    } else if (command.equals(HACK)) {
      int hack_id = Integer.valueOf(commandArray[1]);
      String hack_side = commandArray[2];
      String hack_summary = commandArray[3];
      action = new ScannerActionHack(hack_id, hack_side, hack_summary);
    } else {
      action = new ScannerActionUnknown();
    }

    return action;
  }
}
