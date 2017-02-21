public class RectangleArea {
    public int computeArea(int A, int B, int C, int D, int E, int F, int G, int H) {
        int area1 = (C - A) * (D - B);
        int area2 = (G - E) * (H - F);
        
        if (C <= E || A >= G || D <= F || B >= H) {
            return area1 + area2;
        }
        
        int length = Math.min(C, G) - Math.max(A, E);
        int height = Math.min(D, H) - Math.max(B, F);
        
        return area1 + area2 - length * height;
    }
}