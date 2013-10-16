package cz.larpy.xcom.fieldscanner_v3.ScannerPuzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;

public class ScannerPuzzleManager {
  private Pair<Integer, Integer> getHeader(String line) {
    String[] header = line.split(" ");
    Integer columns = Integer.valueOf(header[0]);
    Integer rows = Integer.valueOf(header[1]);

    return new Pair<Integer, Integer>(columns, rows);
  }

  private List<Item> getItems(BufferedReader reader, int rows, int cols) throws IOException {
    List<Item> to_return = new ArrayList<Item>();
    for (int i = 0; i < rows; i++) {
      String matrix_row_string = reader.readLine();
      for (int j = 0; j < cols; j++) {
        to_return.add(new Item(matrix_row_string.charAt(j)));
      }
    }

    return to_return;
  }

  private Pair<Code, Code> getCodes(BufferedReader reader) throws IOException {
    List<Coords> code1 = new ArrayList<Coords>();
    List<Coords> code2 = new ArrayList<Coords>();

    String line = reader.readLine();
    int lines = Integer.valueOf(line);
    for (int i = 0; i < lines; i++) {
      line = reader.readLine();
      String coordLine[] = line.split(",");
      String coordString1 = coordLine[0];
      String coordString2 = coordLine[1];

      String coordItems1[] = coordString1.split(" ");
      String coordItems2[] = coordString2.split(" ");

      Coords coord1 = new Coords(Integer.valueOf(coordItems1[0]), Integer.valueOf(coordItems1[1]));
      Coords coord2 = new Coords(Integer.valueOf(coordItems2[0]), Integer.valueOf(coordItems2[1]));

      code1.add(coord1);
      code2.add(coord2);
    }

    return new Pair<Code, Code>(new Code(code1), new Code(code2));
  }

  public ScannerPuzzle getPuzzle(Context context, int id) {
    Resources resources = context.getResources();

    String puzzle_identifier = "puzzle_" + id;
    int puzzleResource = resources.getIdentifier(puzzle_identifier, "raw", context.getPackageName());
    InputStream inputStream = resources.openRawResource(puzzleResource);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    int columns = 0;
    int rows = 0;

    List<Item> items = null;
    Pair<Code, Code> codes = null;
    Pair<String, String> passwords = null;

    try {
      String header_line = reader.readLine();
      Pair<Integer, Integer> row_and_col = getHeader(header_line);
      columns = row_and_col.first;
      rows = row_and_col.second;

      items = getItems(reader, rows, columns);
      codes = getCodes(reader);
      passwords = getPasswords(reader);
    } catch (IOException e) {
    }

    return new ScannerPuzzle(items, columns, rows, codes.first, codes.second, passwords.first, passwords.second);
  }

  private Pair<String, String> getPasswords(BufferedReader reader) throws IOException {
    String password1 = reader.readLine();
    String password2 = reader.readLine();
    return new Pair<String, String>(password1, password2);
  }
}
