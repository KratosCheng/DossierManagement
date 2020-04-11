package edu.njusoftware.dossiermanagement.domain;

/**
 * 用于表示PDF中的一行文字
 */
public class PDFLine {
    private String text;

    // 从左下角在x轴方向上的平移距离
    private float tx;

    // 从左下角在y轴方向上的平移距离
    private float ty;

    private int fontSize;

    // 字体颜色，rgb三原色的值
    private int r;

    private int g;

    private int b;

    public PDFLine(String text, float tx, float ty, int fontSize) {
        this.text = text;
        this.tx = tx;
        this.ty = ty;
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTx() {
        return tx;
    }

    public void setTx(float tx) {
        this.tx = tx;
    }

    public float getTy() {
        return ty;
    }

    public void setTy(float ty) {
        this.ty = ty;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "PDFLine{" +
                "text='" + text + '\'' +
                ", x=" + tx +
                ", y=" + ty +
                ", fontSize=" + fontSize +
                '}';
    }
}
