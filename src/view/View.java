package view;

import Model.Board;

public interface View {
    void showMessage(String msg);

    String getString(String msg);
    int getInt(String msg);
    boolean getBoolean(String msg);

    String[] getMove();

    void updateBoard(Board board);
}
