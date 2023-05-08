package HDD;

import java.util.Scanner;

public class HDDFCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Asking for number of addresses to be serviced
        System.out.print("Number of Addresses: ");
        int addressCount = sc.nextInt();
        sc.nextLine();
        System.out.println();

        // Asking addresses
        int[] addresses = new int[addressCount];
        for (int i = 0; i < addressCount; i++) {
            System.out.print("Address " + (i + 1) + ": ");
            addresses[i] = sc.nextInt();
        }

        System.out.println();
        int headMoveCount = 0;
        // Servicing addresses
        for (int i = 1; i < addresses.length; i++) {
            System.out.println("Servicing " + addresses[i] + "...");
            if (addresses[i] > addresses[i - 1])
                headMoveCount += (addresses[i] - addresses[i - 1]);
            else
                headMoveCount += (addresses[i - 1] - addresses[i]);
        }

        System.out.println("\nTotal Head Movement / Seek Count: " + headMoveCount + " cylinders");

    }
}
