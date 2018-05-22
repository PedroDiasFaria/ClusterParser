import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileDoubleMatrix implements Closeable {

    private final int rows;
    private final int cols;
    private final long rowSize;
    private final RandomAccessFile raf;

    public FileDoubleMatrix(File f, int rows, int cols) throws IOException {
        if (rows < 0 || cols < 0)
            throw new IllegalArgumentException(
                    "Rows and cols cannot be negative!");
        this.rows = rows;
        this.cols = cols;
        rowSize = cols * 8;
        raf = new RandomAccessFile(f, "rw");
        raf.setLength(rowSize * cols);
    }

    /**
     * Absolute get method.
     */
    public int get(int row, int col) throws IOException {
        pos(row, col);
        return get();
    }

    /**
     * Absolute set method.
     */
    public void set(int row, int col, int value) throws IOException {
        pos(row, col);
        set(value);
    }

    public void pos(int row, int col) throws IOException {
        if (row < 0 || col < 0 || row >= rows || col >= cols)
            throw new IllegalArgumentException("Invalid row or col!");
        raf.seek(row * rowSize + col * 8);
    }

    /**
     * Relative get method. Useful if you want to go though the whole array or
     * though a continuous part, use {@link #pos(int, int)} to position.
     */
    public int get() throws IOException {
        return (int) raf.readDouble();
    }

    /**
     * Relative set method. Useful if you want to go though the whole array or
     * though a continuous part, use {@link #pos(int, int)} to position.
     */
    public void set(int value) throws IOException {
        raf.writeDouble(value);
    }

    public int getRows() { return rows; }

    public int getCols() { return cols; }

    @Override
    public void close() throws IOException {
        raf.close();
    }

}