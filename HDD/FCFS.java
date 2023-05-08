package HDD;
import java.util.Scanner;

public class FCFS {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        // Asking for number of addresses to be serviced
        System.out.print("Number of Addresses: ");
        int addressCount = sc.nextInt();
        System.out.print("Starting head location: ");
        int head = sc.nextInt();
        sc.nextLine();
        System.out.println();

        // Asking addresses
        int[] addresses = new int[addressCount];
        for (int i = 0; i < addressCount; i++) {
            System.out.print("Address " + (i + 1) + ": ");
            addresses[i] = sc.nextInt();
        }
        System.out.println();

        int seekCount = 0;
        // Servicing addresses
        for (int i = 0; i < addresses.length; i++) {
            int currentTrack = addresses[i];
            System.out.println("Servicing " + currentTrack + "...");
            seekCount += Math.abs(currentTrack-head);
            head = currentTrack;
        }

        System.out.println("\nTotal Head Movement: " + addresses.length + "  \nSeek Count: " + seekCount);
    }
}