package com.henry.string_05.data_compress_05.run_length_encoding_05.codes_execution;

/******************************************************************************
 *  Compilation:  javac PictureDump.java
 *  Execution:    java PictureDump width height < file
 *  Dependencies: BinaryStdIn.java Picture.java
 *  Data file:    http://introcs.cs.princeton.edu/stdlib/abra.txt
 *
 *  Reads in a binary file and writes out the bits as w-by-h picture,
 *  with the 1 bits in black and the 0 bits in white.
 *
 *  % more abra.txt
 *  ABRACADABRA!
 *
 *  % java PictureDump 16 6 < abra.txt
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.Picture;

import java.awt.*;


/**
 * The {@code PictureDump} class provides a client for displaying the contents
 * of a binary file as a black-and-white picture.
 * 以黑白图片的方式 来 展示二进制文件
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * <p>
 * See also {@link BinaryDump} and {@link HexDump}.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 作用：以黑白图片的方式 来 展示二进制文件
public class PictureDump {

    // Do not instantiate.
    private PictureDump() {
    }

    /**
     * 从标准输入中读取字节序列，
     * 并把它们 以一个 宽-高图片的方式 绘制到 标准绘制输出中，
     * 对于1使用黑色，对于0使用白色（剩下的像素 使用红色）
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        Picture picture = new Picture(width, height);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (!BinaryStdIn.isEmpty()) {
                    // 读取标准输入中的比特
                    boolean currentBitOfInput = BinaryStdIn.readBoolean();
                    // 如果比特为1，则 打印黑色像素
                    if (currentBitOfInput) picture.set(col, row, Color.BLACK);
                    // 如果比特为0，则 打印白色像素
                    else picture.set(col, row, Color.WHITE);
                } else { // 如果标准输入中的比特已经耗尽，则 打印红色像素
                    picture.set(col, row, Color.RED);
                }
            }
        }

        // 把图片绘制到 标准绘制输出中
        picture.show();
    }
}