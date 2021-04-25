public class Main {
    public static void main(String[] args) {
        // How long to run the simulation
        int timeSteps = 100000000;

        // Simulate random drop
        long dropStart = System.nanoTime();
        DropTable d = new DropTable(5);
        int[][] drop = d.run(timeSteps);
        long dropEnd = System.nanoTime();

        // Simulate deadlock reset
        long deadStart = System.nanoTime();
        DeadlockTable d2 = new DeadlockTable(5);
        int[][] deadlock = d2.run(timeSteps);
        long deadEnd = System.nanoTime();

        long dropTime = dropEnd - dropStart;
        long deadTime = deadEnd - deadStart;

        // Display information
        System.out.printf("%d time steps simulated\n", timeSteps);
        System.out.println("Random drop: " + dropTime/1000000 + "ms");
        System.out.println("Deadlock reset: " + deadTime/1000000 + "ms");

        System.out.println("Random drop statistics: (0.5 request prob, 0.3 drop prob)");
        for(int i = 0; i < drop.length; i++) {
            System.out.printf("Philosopher %d: %d eating time (%.2f%%), %d waiting time (%.2f%%), full prob: %.2f\n", i+1, drop[i][0], (double)drop[i][0]/timeSteps*100, drop[i][1], (double)drop[i][1]/timeSteps*100, Math.floor(((i*0.25)/drop.length+0.1)*100)/100);
        }

        System.out.println("Deadlock reset statistics: (0.5 request prob)");
        for(int i = 0; i < deadlock.length; i++) {
            System.out.printf("Philosopher %d: %d eating time (%.2f%%), %d waiting time (%.2f%%), full prob: %.2f\n", i+1, deadlock[i][0], (double)deadlock[i][0]/timeSteps*100, deadlock[i][1], (double)deadlock[i][1]/timeSteps*100, Math.floor(((i*0.25)/deadlock.length+0.1)*100)/100);
        }
    }
}
