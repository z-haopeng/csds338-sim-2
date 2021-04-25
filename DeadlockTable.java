public class DeadlockTable {
    private int numPhilosophers;
    private boolean[] chopsticks;
    private Philosopher[] philosophers;
    private int deadlockCount;

    public DeadlockTable(int n) {
        numPhilosophers = n;
        chopsticks = new boolean[n];
        philosophers = new Philosopher[n];
        for(int i = 0; i < n; i++) {
            chopsticks[i] = true;
            philosophers[i] = new Philosopher(i+1, 0.5, Math.floor(((i*0.25)/n+0.1)*100)/100);
        }
        deadlockCount = 0;
    }

    public int[][] run(int t) {
        for(int i = 1; i <= t; i++) {
            boolean deadlock = true;
            // System.out.println("Time = " + i + "\tHungry prob: 0.5");
            for(Philosopher p : philosophers)
                if(!p.holdExactlyOne()) deadlock = false;
            if(deadlock) {
                // System.out.println("DEADLOCK");
                for(Philosopher p : philosophers)
                    p.drop();
                deadlockCount++;
            } else {
                for(Philosopher p : philosophers)
                    p.step();
            }
            /*
            for(int j = 0; j < philosophers.length; j++) 
                System.out.println("["+(j+1)+"]: "+philosophers[j]);
            */
        }
        System.out.printf("Number of resolved deadlocks: %d\n", deadlockCount);
        int[][] result = new int[numPhilosophers][2];
        for(int i = 0; i < numPhilosophers; i++) {
            result[i][0] = philosophers[i].eat;
            result[i][1] = philosophers[i].wait;
        }
        return result;
    }

    private class Philosopher {
        private int number;
        private double[] hold, request;
        private int eat, wait;
        private double hungry, full;

        public Philosopher(int n, double h, double f) {
            number = n;
            hold = new double[2];
            request = new double[2];
            eat = wait = 0;
            hungry = h; full = f;
        }

        public boolean holdExactlyOne() {
            return hold[0]!=0 ^ hold[1]!=0;
        }

        public void step() {
            // If requesting, try to pick up a chopstick, and if successful, step is over
            if(request[0] != 0 || request[1] != 0)
                if(attemptPickup()) return;

            // If currently eating, eat for a step, then check if full
            if(hold[0] != 0 && hold[1] != 0) {
                eat++;
                if(Math.random() < full)
                    doneEating();
                /*
                else
                    System.out.printf("Philisopher %d is eating\n", number);
                */
            }
            // Check if requesting
            else if(((hold[0]==0 && request[0]==0) || (hold[1]==0 && request[1]==0)) && Math.random() < hungry) {
                request();
            }
        }

        // Attempt to pickup one chopstick
        private boolean attemptPickup() {
            // Two requests
            if(request[0] != 0 && request[1] != 0) {
                if(chopsticks[(int)Math.floor(request[0])]) {
                    chopsticks[(int)Math.floor(request[0])] = false;
                    hold[0] = request[0];
                    request[0] = 0;
                    // System.out.printf("Philosopher %d picked up Chopstick %.1f\n", number, hold[0]);
                    return true;
                } else {
                    if(chopsticks[(int)Math.floor(request[1])]) {
                        chopsticks[(int)Math.floor(request[1])] = false;
                        hold[1] = request[1];
                        request[1] = 0;
                        // System.out.printf("Philosopher %d picked up Chopstick %.1f\n", number, hold[1]);
                        return true;
                    } else {
                        wait++;
                        return false;
                    }
                }
            }
            // Left request only
            if(request[0] != 0 && request[1] == 0) {
                if(chopsticks[(int)Math.floor(request[0])]) {
                    chopsticks[(int)Math.floor(request[0])] = false;
                    hold[0] = request[0];
                    request[0] = 0;
                    // System.out.printf("Philosopher %d picked up Chopstick %.1f\n", number, hold[0]);
                    return true;
                } else {
                    wait++;
                    return false;
                }
            }
            // Right request only
            if(request[1] != 0) {
                if(chopsticks[(int)Math.floor(request[1])]) {
                    chopsticks[(int)Math.floor(request[1])] = false;
                    hold[1] = request[1];
                    request[1] = 0;
                    // System.out.printf("Philosopher %d picked up Chopstick %.1f\n", number, hold[1]);
                    return true;
                } else {
                    wait++;
                    return false;
                }
            }
            return false;
        }

        // Drop both chopsticks
        private void doneEating() {
            // System.out.printf("Philosopher %d finished eating\n", number);
            chopsticks[(int)Math.floor(hold[0])] = chopsticks[(int)Math.floor(hold[1])] = true;
            hold[0] = hold[1] = 0;
        }

        // Request a chopstick
        private void request() {
            // Special case: philosopher #3 can request any chopstick
            if(number == 3) {
                if(request[0]==0 && hold[0]==0) {
                    request[0] = Math.floor(Math.random()*numPhilosophers)+0.5;
                    // System.out.printf("Philosopher %d requested Chopstick %.1f\n", number, request[0]);
                } else {
                    double r = Math.floor(Math.random()*numPhilosophers)+0.5;
                    while(r == request[0] || r == hold[0])
                        r = Math.floor(Math.random()*numPhilosophers)+0.5;
                    request[1] = r;
                    // System.out.printf("Philosopher %d requested Chopstick %.1f\n", number, request[1]);
                }
            } else {
                if(request[0]==0 && hold[0]==0) {
                    request[0] = number - 0.5;
                    // System.out.printf("Philosopher %d requested Chopstick %.1f\n", number, request[0]);
                } else {
                    request[1] = number % numPhilosophers + 0.5;
                    // System.out.printf("Philosopher %d requested Chopstick %.1f\n", number, request[1]);
                }
            }
        }

        // Drop one held chopstick
        private void drop() {
            if(hold[0]!=0) {
                chopsticks[(int)Math.floor(hold[0])] = true;
                // System.out.printf("Philosopher %d dropped Chopstick %.1f\n", number, hold[0]);
                hold[0] = 0;
            } else if(hold[1]!=0) {
                chopsticks[(int)Math.floor(hold[1])] = true;
                // System.out.printf("Philosopher %d dropped Chopstick %.1f\n", number, hold[1]);
                hold[1] = 0;
            }
        }

        public String toString() {
            return "full prob: "+full+" \tholding: "+(hold[0]!=0 ? hold[0]:"   ")+"/"+(hold[1]!=0 ? hold[1]:"   ")+"\trequesting: "+(request[0]!=0 ? request[0]:"   ")+"/"+(request[1]!=0 ? request[1]:"   ")+"  \teating time: "+eat+"  \twaiting time: "+wait;
        }
    }
}
