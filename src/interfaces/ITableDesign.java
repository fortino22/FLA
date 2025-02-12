package interfaces;

public interface ITableDesign<T> {
    String formatHeader();
    String formatRow(T item);
    String formatEmpty();
    String formatBorder();
    int getColumnWidth();
}

