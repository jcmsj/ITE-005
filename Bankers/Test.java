package Bankers;
public class Test {
    public static void main(String[] args) {
        var a = new Algorithm(3, 5);
        var allocation = new int[][] {
                { 0, 1, 0 },
                { 2, 0, 0 },
                { 3, 0, 2 },
                { 2, 1, 1 },
                { 0, 0, 2 }
        };
        var maximum = new int[][] {
                { 7, 5, 3 },
                { 3, 2, 2 },
                { 9, 0, 2 },
                { 2, 2, 2 },
                { 4, 3, 3 }
        };
        for (int i = 0; i < a.ps.length; i++) {
            var p = a.ps[i] = new Algorithm.Process(i, a.instanceCount);
                p.allocation = allocation[i];
                p.maximum = maximum[i];
        }
        a.availableTemp = new int[] { 10, 5, 7 };
        a.compute();
        a.actual();
        a.showResult();
    }
}
