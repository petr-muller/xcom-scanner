package cz.larpy.xcom.fieldscanner_v3.ScannerPuzzle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cz.larpy.xcom.fieldscanner_v3.R;

public class ScannerPuzzleActivity extends Activity {

  public static final String HACKID = "HACKID";
  public static final String HACKSIDE = "HACKSIDE";
  public static final String HACKSUMMARY = "HACKSUMMARY";
  private static final String SAVED_CODE = "SAVED_CODE";

  private static ScannerPuzzle puzzle = null;
  private ScannerPuzzleManager manager = null;
  private final Code code_entered = new Code();
  private Item[][] myMatrix = null;
  private TextView theirCodeText;
  private TextView passwordText = null;
  private Button resetButton = null;
  private final List<CoordButton> buttons = new ArrayList<CoordButton>();
  private String side = null;

  private class CoordButton extends Button {
    private final int row;
    private final int col;

    public CoordButton(Context context, int pRow, int pCol) {
      super(context);
      row = pRow;
      col = pCol;
    }

  }

  private TextView createSummaryLabel(String summary, String side) {
    TextView summaryLabel = new TextView(this);
    summaryLabel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    summaryLabel.setText("Hack terminálu: " + summary + "[" + side + "]");
    summaryLabel.setTextSize(24.0f);

    return summaryLabel;
  }

  private void createButtonMatrix(String side, LinearLayout ll) {
    for (int row = 0; row < puzzle.getHeight(); row++) {
      LinearLayout llRow = new LinearLayout(this);
      llRow.setOrientation(LinearLayout.HORIZONTAL);
      llRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
      for (int col = 0; col < puzzle.getWidth(); col++) {
        CoordButton button = new CoordButton(this, row, col);
        button.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f));
        button.setText(myMatrix[row][col].getLabel());
        button.setOnClickListener(matrixButtonAction);
        llRow.addView(button);
        buttons.add(button);
      }
      ll.addView(llRow);
    }
  }

  private LinearLayout createOurCode(String side) {
    LinearLayout llOurCode = new LinearLayout(this);
    llOurCode.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    llOurCode.setOrientation(LinearLayout.HORIZONTAL);

    TextView ourCodeLabel = new TextView(this);
    ourCodeLabel.setText("Vstup: ");
    ourCodeLabel.setTextSize(18.0f);
    llOurCode.addView(ourCodeLabel);

    TextView ourCodeView = new TextView(this);

    StringBuilder ourCode = new StringBuilder();
    List<Item> code = null;
    if (side.equals("A")) {
      code = puzzle.getCode2();
    } else {
      code = puzzle.getCode1();
    }

    for (Item item : code) {
      ourCode.append(item.getLabel());
    }
    ourCodeView.setText(ourCode.toString());
    ourCodeView.setTextSize(18.0f);
    llOurCode.addView(ourCodeView);

    return llOurCode;
  }

  private LinearLayout createTheirCode(String side) {
    LinearLayout llTheirCode = new LinearLayout(this);
    TextView theirCodeLabel = new TextView(this);
    theirCodeLabel.setText("Kód: ");
    theirCodeLabel.setTextSize(18.0f);
    llTheirCode.addView(theirCodeLabel);

    theirCodeText = new TextView(this);
    theirCodeText.setTextSize(18.0f);
    theirCodeText.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f));
    llTheirCode.addView(theirCodeText);

    resetButton = new Button(this);
    resetButton.setText("RESET");
    resetButton.setOnClickListener(resetButtonAction);
    llTheirCode.addView(resetButton);

    return llTheirCode;

  }

  private LinearLayout createPassword() {
    LinearLayout llPassword = new LinearLayout(this);

    TextView passwordLabel = new TextView(this);
    passwordLabel.setText("Heslo: ");
    passwordLabel.setTextSize(18.0f);
    llPassword.addView(passwordLabel);

    passwordText = new TextView(this);
    passwordText.setTextSize(18.0f);
    llPassword.addView(passwordText);

    return llPassword;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    manager = new ScannerPuzzleManager();

    Intent i = getIntent();
    int id = i.getExtras().getInt(HACKID);
    side = i.getExtras().getString(HACKSIDE);
    String summary = i.getExtras().getString(HACKSUMMARY);

    puzzle = manager.getPuzzle(this, id);

    if (side.equals("A")) {
      myMatrix = puzzle.getMatrix1();
    } else {
      myMatrix = puzzle.getMatrix2();
    }

    ScrollView sv = new ScrollView(this);
    sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    LinearLayout ll = new LinearLayout(this);
    ll.setOrientation(LinearLayout.VERTICAL);
    ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    sv.addView(ll);

    ll.addView(createSummaryLabel(summary, side));
    createButtonMatrix(side, ll);
    ll.addView(createOurCode(side));
    ll.addView(createTheirCode(side));
    ll.addView(createPassword());

    if (savedInstanceState != null) {
      List<Coords> bundledCode = savedInstanceState.getParcelableArrayList(SAVED_CODE);
      for (Coords coord : bundledCode) {
        code_entered.append(coord);
      }
    }

    this.setContentView(sv);
  }

  @Override
  protected void onResume() {
    super.onResume();
    update();
  }

  private final Button.OnClickListener matrixButtonAction = new Button.OnClickListener() {

    @Override
    public void onClick(View v) {
      CoordButton button = (CoordButton) v;
      ScannerPuzzleActivity a = ScannerPuzzleActivity.this;
      if (a.code_entered.getLength() < ScannerPuzzleActivity.puzzle.getCode1().size()) {
        Coords coord = new Coords(button.row, button.col);
        a.code_entered.append(coord);
        a.update();
      }
    }
  };

  private final Button.OnClickListener resetButtonAction = new Button.OnClickListener() {

    @Override
    public void onClick(View v) {
      code_entered.clear();
      update();
    }

  };

  protected void update() {
    StringBuilder sb = new StringBuilder();
    for (Coords coords : code_entered.getCode()) {
      sb.append(myMatrix[coords.getX()][coords.getY()].getLabel());
    }
    theirCodeText.setText(sb.toString());

    if (code_entered.getLength() == ScannerPuzzleActivity.puzzle.getCode1().size()) {
      disableButtons();
      checkCode();
    } else {
      enableButtons();
      theirCodeText.setBackgroundColor(getResources().getColor(android.R.color.background_light));
    }
  }

  private void checkCode() {
    if (side.equals("A")) {
      if (puzzle.matchCode2(code_entered)) {
        theirCodeText.setBackgroundColor(getResources().getColor(R.color.green));
        passwordText.setText(puzzle.getPassword1());
        resetButton.setEnabled(false);
      } else {
        theirCodeText.setBackgroundColor(getResources().getColor(R.color.red));
      }
    } else {
      if (puzzle.matchCode1(code_entered)) {
        theirCodeText.setBackgroundColor(getResources().getColor(R.color.green));
        passwordText.setText(puzzle.getPassword2());
        resetButton.setEnabled(false);
      } else {
        theirCodeText.setBackgroundColor(getResources().getColor(R.color.red));
      }
    }
  }

  private void disableButtons() {
    for (CoordButton button : buttons) {
      button.setEnabled(false);
    }
  }

  private void enableButtons() {
    for (CoordButton button : buttons) {
      button.setEnabled(true);
    }
  }

  @Override
  public void onSaveInstanceState(Bundle pState) {
    super.onSaveInstanceState(pState);
    ArrayList<Coords> code = new ArrayList<Coords>();
    List<Coords> old_code = code_entered.getCode();
    code.addAll(old_code);

    pState.putParcelableArrayList(SAVED_CODE, code);
  }
}
