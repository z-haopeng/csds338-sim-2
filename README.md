# Dining Philosophers Simulation for CSDS 338
Samuel Bachelor, Michael Seese, Kevin Zhou

## Program Files
- Main.java: runs the simulation
- DropTable.java: random drop method to combat deadlock
- DeadlockTable.java: once deadlock is reached, resets the philosophers

## Output Samples
- dropSample.txt: verbose output for 10000 steps of random drop simulation
- deadlockSample.txt: verbose output for 10000 steps of deadlock recovery simulation
- 100.txt, 1000.txt, ... 100000000.txt: output from the main program for 100 steps, 1000 steps, etc.

## Summary of Results
##### One run of the simulation for 10 million time steps produced the following statistics:
- 135,604 resolved deadlocks
- Random drop runtime: 1619ms
- Deadlock resolve runtime: 1183ms
##### Random Drop
Philosopher | Eating Time | Waiting Time
----------- | ----------- | ------------
1 | 3403711 (34.04%) | 4095749 (40.96%)
2 | 3089181 (30.89%) | 3920793 (39.21%)
3 | 1713623 (17.14%) | 5780774 (57.81%)
4 | 2505164 (25.05%) | 2607420 (26.07%)
5 | 1491452 (14.91%) | 5161226 (51.61%)
##### Deadlock Resolve
Philosopher | Eating Time | Waiting Time
----------- | ----------- | ------------
1 | 3315806 (33.16%) | 4641637 (46.42%)
2 | 2866385 (28.66%) | 4780487 (47.80%)
3 | 1448835 (14.49%) | 6809499 (68.09%)
4 | 1207889 (12.08%) | 6670855 (66.71%)
5 | 963536 (9.64%) | 7307836 (73.08%)
