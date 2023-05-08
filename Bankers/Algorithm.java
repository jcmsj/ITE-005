package Bankers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Algorithm {
    public Process[] ps;
    private LinkedList<Integer> sequence = new LinkedList<>();
    public int[] availableTemp;
    public int[][] available;
    public final int instanceCount;
    public final int processCount;

    public static class Process {
        public int[] allocation;
        public int[] maximum;
        public int[] need;
        public boolean isDone;
        public final int id;

        public Process(int id, int[] allocation, int[] max, int[] need) {
            this.id = id;
            this.isDone = false;
            this.allocation = allocation;
            this.maximum = max;
            this.need = need;
        }

        /* Fill arrays with empty values */
        public Process(int id, int instanceCount) {
            this(id,
                    new int[instanceCount],
                    new int[instanceCount],
                    new int[instanceCount]);
        }

        public static Process ask(Scanner sc, int id, int instanceCount) {
            // FOR ALLOCATION INPUTS
            System.out.println("\t==========================");
            System.out.println("\t\t PROCESS " + id);
            System.out.print("\t ALLOCATION INPUTS: ");
            Process p = new Process(id, instanceCount);

            for (int i = 0; i < instanceCount; i++) {
                p.allocation[i] = sc.nextInt();
            }

            // FOR MAXIMUM INPUTS
            System.out.print("\t MAXIMUM INPUTS: ");

            for (int i = 0; i < instanceCount; i++) {
                p.maximum[i] = sc.nextInt();
            }

            return p;
        }
    }

    public Algorithm(int instanceCount, int processCount) {
        this.instanceCount = instanceCount;
        this.processCount = processCount;
        ps = new Process[processCount];
        availableTemp = new int[instanceCount];
        available = new int[processCount+1][instanceCount];
    }

    public void compute() {
        // CREATING THE NEW "AVAILABLE" VALUES
        // ADDING ALL THE ALLOCATION IN EACH LETTER (A, B ,C)
        // LET I BE THE (A, B, C) AND J BE THE PROCESSORS
        for (int i = 0; i < instanceCount; i++) {
            int sumAllocation = 0;
            for (int j = 0; j < ps.length; j++) {
                sumAllocation += ps[j].allocation[i];
            }
            availableTemp[i] -= sumAllocation;
        }

        // CREATING THE "NEED" VALUES BY USING THE FORMULA (MAXIMUM - ALLOCATION)
        for (int i = 0; i < ps.length; i++) {
            var p = ps[i];
            for (int j = 0; j < instanceCount; j++) {
                p.need[j] = p.maximum[j] - p.allocation[j];
            }
        }

        // PASS THE AVAILABLE VALUES TO THE 2D AVAILABLE ARRAY TO DISPLAY IT LATER
        available[0] = availableTemp;
    }

    public boolean hasTodo() {
        return Arrays.stream(ps).anyMatch(p -> !p.isDone);
    }

    public void actual() {
        int next = 1;
        sequence = new LinkedList<>();
        do {
            for (int i = 0; i < ps.length; i++) {
                var p = ps[i];
                // TO COMPARE IF THE NEED IS LESS THAN OR EQUAL TO THE AVAILABLE (WORK) NEED <=
                // WORK
                if (!p.isDone && isLessThanOrEqual(p.need, availableTemp)) {
                    sequence.add(i);
                    for (int j = 0; j < availableTemp.length; j++) {
                        available[next][j] = availableTemp[j] + p.allocation[j];
                    }
                    // PASSES IT TO THE TEMP SO THAT WE CAN USE IT IN THE LOOP AGAIN
                    availableTemp = available[next++];
                    p.isDone = true;
                    break;
                }
            }
        } while (hasTodo());

        System.out.println("PROCESS \tALLOCATIONS  \t\t MAXIMUM\t     AVAILABLE\t\t\t NEED");
        System.out.println(
                "\t       A     B     C  \t      A     B     C   \t   A     B     C  \t    A     B     C  ");

        for (int i = 0; i < ps.length; i++) {
            var p = ps[i];
            System.out.print("  P" + i + "      ");
            showRows(p.allocation);
            System.out.print("     ");
            showRows(p.maximum);
            System.out.print("   ");
            showRows(available[i]);
            System.out.print("       ");
            showRows(p.need);
            System.out.println("");
        }
        System.out.print("\t\t\t\t\t\t      ");
        showRows(available[5]);
        System.out.print("\nSafe state: ");
        for (int n : sequence) {
            System.out.print("P" + n + " ");
        }
    }

    public static boolean isLessThanOrEqual(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] > b[i]) {
                return false;
            }
        }
        return true;
    }

    public static void showRows(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            System.out.print("     " + nums[i]);
        }
    }

    public static void main(String[] args) {
        final Scanner sc = new Scanner(System.in);
        System.out.print("Enter instance count: ");
        final int instanceCount = sc.nextInt();
        System.out.print("Enter process count: ");
        final int processCount = sc.nextInt();
        sc.nextLine();
        System.out.println();
        Algorithm a = new Algorithm(instanceCount, processCount);
        // ASK USER n INSTANCES
        System.out.println("\t==========================");
        System.out.print("\t INPUT " + instanceCount + " INSTANCES: ");
        for (int i = 0; i < 3; i++) {
            a.availableTemp[i] = sc.nextInt();
        }

        for (int i = 0; i < instanceCount; i++) {
            a.ps[i] = Process.ask(sc, i, instanceCount);
        }

        a.compute();
        a.actual();
    }
}