package PageReplacement;

import java.util.*;

public class OptimalPageReplacement {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter number of frames: ");
        int frameSize = input.nextInt();
        System.out.print("Enter number of pages: ");
        int pageSize = input.nextInt();
        Integer[][] frames = new Integer[frameSize][pageSize];
        LinkedList<Integer> page = new LinkedList<>();
        LinkedList<Integer> refs = new LinkedList<>();
        System.out.print("Enter reference string (" + pageSize + " inputs): ");
        for (int i = 0; i < pageSize; i++) {
            refs.offer(input.nextInt());
        }
        int faults = 0, hits = 0;
        for (int i = 0; i < refs.size(); i++) {
            Integer n = refs.get(i);
            if (page.indexOf(n) > -1) {
                hits++;
            } else {
                faults++;
                if (page.size() < frameSize) {
                    page.offer(n);
                } else {
                    int replace = -1;
                    LinkedList<Integer> clone = new LinkedList<>();
                    for (int p:page) {
                        clone.add(p);
                    }

                    // Find all NEXT appearances of page elements
                    // If a `p` does not appear anymore, it should be replaced by `n`
                    // Else, replace the furthest element in between all the NEXT appearances with `n`.
                    outer:
                    for (int k = i + 1; k < refs.size(); k++) {
                        for (int j = 0; j < page.size(); j++) {
                            Integer p = page.get(j);
                            if (p == refs.get(k)) {
                                replace = j;
                                clone.remove(p);
                                if (clone.isEmpty()) {
                                    break outer;
                                }
                            }
                        }
                    }
                    if (!clone.isEmpty()) {
                        replace = page.indexOf(clone.pollFirst());
                    }
                    if (replace > -1) { 
                        page.set(replace, n);
                    }
                }
            }

            // Copy latest state
            for (int k = 0; k < page.size(); k++) {
                frames[k][i] = page.get(k);
            }
        }

        System.out.println("\nOPT Page Replacement Process:");
        //Show digit to be replaced as 1st row
        System.out.print("   ");
        for (int n : refs) {
            System.out.print(n + "  ");
        }
        System.out.println();
        for (int i = 0; i < frameSize; i++) {
            System.out.print("F" + (i + 1) + " ");
            for (int j = 0; j < pageSize; j++) {
                //Print empty space when null
                if (frames[i][j] == null) {
                    System.out.print(" ");
                } else {
                    System.out.print(frames[i][j]);
                }
                System.out.print("  ");
            }
            System.out.println("");
        }
        System.out.println("Hits = " + hits + "\nFaults = " + faults);
    }
}