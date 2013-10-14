package cz.larpy.xcom.fieldscanner_v3.ScannerPuzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public final class ScannerPuzzle {
  private int width;
  private int heigth;
  private Code code1;
  private Code code2;
  private Item[][] matrix1;
  private Item[][] matrix2;

  private final SortedSet<Item> itemset = new TreeSet<Item>();

  public ScannerPuzzle(ArrayList<Item> items, int pW, int pH, Code pCode1, Code pCode2) {
    itemset.addAll(items);
    if ((pW * pH) > items.size()) {
      throw new AssertionError("ScannerPuzzle: resulting matrix contains more elements than item set");
    }

    width = pW;
    heigth = pH;

    SortedSet<Item> matrixItems = new TreeSet<Item>();
    matrixItems.addAll(itemset);
    for (int row = 0; row < pH; row++) {
      for (int col = 0; col < pW; col++) {
        matrix1[row][col] = matrixItems.first();
        matrixItems.remove(matrixItems.first());
      }
    }
    matrixItems.clear();
    matrixItems.addAll(itemset);
    for (int row = 0; row < pH; row++) {
      for (int col = 0; col < pW; col++) {
        matrix2[row][col] = matrixItems.last();
        matrixItems.remove(matrixItems.last());
      }
    }
    code1 = new Code(pCode1.getCode());
    code2 = new Code(pCode2.getCode());
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return heigth;
  }

  public Item[][] getMatrix1() {
    return matrix1;
  }

  public Item[][] getMatrix2() {
    return matrix2;
  }

  private List<Item> getCode(Code code, Item[][] matrix) {
    List<Item> codeItems = new ArrayList<Item>();
    for (Coords coords : code.getCode()) {
      codeItems.add(matrix[coords.getX()][coords.getY()]);
    }
    return Collections.unmodifiableList(codeItems);
  }

  public List<Item> getCode1() {
    return getCode(code1, matrix1);
  }

  public List<Item> getCode2() {
    return getCode(code2, matrix2);
  }

  public boolean matchCode1(Code pCode) {
    return code1.matches(pCode);
  }

  public boolean matchCode2(Code pCode) {
    return code2.matches(pCode);
  }
}

final class Code {
  private List<Coords> code = new ArrayList<Coords>();

  public Code(List <Coords> pCode) {
    code.addAll(pCode);
  }

  public List<Coords> getCode() {
    return Collections.unmodifiableList(code);
  }

  public boolean matches(Code other) {
    if (code.size() != other.code.size()) {
      return false;
    }

    for (int i = 0; i < code.size(); i++) {
      if (code.get(i) != other.code.get(i)){
        return false;
      }
    }
    return true;
  }
}

final class Coords {
  private final int X;
  private final int Y;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + X;
    result = prime * result + Y;
    return result;
  }

  public int getY() {
    return Y;
  }

  public int getX() {
    return X;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Coords other = (Coords) obj;
    if (X != other.X)
      return false;
    if (Y != other.Y)
      return false;
    return true;
  }

  public Coords(int x, int y) {
    X = x;
    Y = y;
  }
}

final class Item {
  private final char C;

  public Item(char c) {
    C = c;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + C;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Item other = (Item) obj;
    if (C != other.C)
      return false;
    return true;
  }
}
