package Bankers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

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
        available = new int[processCount + 1][instanceCount];
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
    }

    public void createHeaders(StringBuilder code) {
        char label = 'A';
        code.append("<tr>");
        for (int i = 0; i < availableTemp.length; i++) {
            code.append("<th>" + label + ("</th>"));
            label++;
        }
        code.append("</tr>");
    }

    public static enum Column {
        alloc,
        max,
        free,
        need
    }

    public String printSub(Column c) {
        var code = new StringBuilder("<table>");
        createHeaders(code);
        for (int i = 0; i < ps.length; i++) {
            int[] row = {};
            var p = ps[i];
            switch (c) {
                case alloc:
                    row = p.allocation;
                    break;
                case max:
                    row = p.maximum;
                    break;
                case free:
                    row = available[i];
                    break;
                default:
                    row = p.need;
                    break;
            }
            code.append("<tr>");
            for (int n : row) {
                code.append("<td>" + n + "</td>");
            }
            code.append("</tr>");
        }
        code.append("<tr>");
        for (int n : available[ps.length]) {
            code.append("<td>" + (c == Column.free ? n : "") + "</td>");
        }
        code.append("</tr>");

        code.append("</table>");
        return code.toString();
    }

    public String printSubTable() {
        var code = new StringBuilder("");
        code.append("<td>");
        code.append(printSub(Column.alloc));
        code.append("</td>");

        code.append("<td>");
        code.append(printSub(Column.max));
        code.append("</td>");

        code.append("<td>");
        code.append(printSub(Column.free));
        code.append("</td>");

        code.append("<td>");
        code.append(printSub(Column.need));
        code.append("</td>");
        return code.toString();
    }

    public StringBuilder showJTable() {
        StringBuilder code = new StringBuilder("""
                <html>
                <style>
                #main, th, td {
                    text-align: center;
                }
                </style>
                <table id='main'>
                <tr>
                    <th>PROCESS</th>
                    <th>ALLOCATIONS</th>
                    <th>MAXIMUM</th>
                    <th>AVAILABLE</th>
                    <th>NEED</th>
                </tr>""");
        code.append("<tr>");
        code.append(showProcesses());
        code.append(printSubTable());
        code.append("</tr>");
        code.append("</table></html>");
        return code;
    }

    public void showResult() {
        UIManager.put("Label.font", new Font("arial", Font.PLAIN, 40));
        JFrame frame = new JFrame("Banker's Algorithm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        var main = new JPanel();
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        var code = showJTable();
        var tabl = new JLabel(code.toString());
        var seqBuilder = new StringBuilder("Safe State: ");
        for (int i = 0; i < sequence.size(); i++) {
            seqBuilder.append("P" + sequence.get(i) + (i == sequence.size() - 1 ? "" : "  → "));
        }
        var seq = new JLabel(seqBuilder.toString());
        main.add(tabl);
        main.add(seq);
        frame.add(main);
        frame.pack();
        frame.setVisible(true);
    }

    public String showProcesses() {
        var code = new StringBuilder();
        // Skip 1st column in 1st row
        code.append("<td><table id='sub'><tr><th></th></tr>");
        for (var p : ps) {
            code.append("<tr>");
            code.append("<td>P" + p.id + "</td>");
            code.append("</tr>");
        }
        code.append("<tr></tr></table></td>");
        return code.toString();
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