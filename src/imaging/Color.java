package imaging;

public class Color {
    private int r, g, b, a;
    public Color(int r, int g, int b, int a){
        this.r = r;
        this.a = a;
        this.g = g;
        this.b = b;
    }
    public Color(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }

    public boolean equals(Color other){
        if (this.r == other.r && this.g == other.b && this.b == other.b && this.a == other.a){
            return true;
        }
        return false;
    }
    public int getR(){
        return r;
    }
    public int getG(){
        return g;
    }
    public int getB(){
        return b;
    }
    public int getA(){
        return a;
    }
    public String toString(){
        String str = "";
        str += "[" + r + "," + g + "," + b + "," + a + "]";
        return str;
    }
}
