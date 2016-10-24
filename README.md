# PPI Subnetwork Finder
This repository contains an algorithm for detecting the largest common subnetwork between two PPI networks, along with other miscellaneous files for creating datasets and assessing the results of the algorithm.

## Directory Structure
```
├── Isobase/ - Testing using the Isobase dataset
├── NAPABench/ - Testing using the NAPABench dataset
│   ├── Dataset Extraction - Generates datasets of subgraphs
│   │   ├── Additional Files/ - Includes the original dataset used for the ground truth
│   │   ├── DatasetGenerator.java
│   │   ├── Generate.java
│   │   ├── input.txt
│   │   ├── spinal_results.txt
│   │   ├── Output Files (eg. 41-50-50.txt)
│   ├── SPINAL Experiments - Ground truth for the datasets
│   ├── Tester - Assesses results using SPINAL's solution as a ground truth
│   │   ├── SolutionChecker.java
│   │   ├── spinal_results.txt
├── Runner.java - The PPI network alignment algorithm
```

## Running the algorithm
After compiling the files, run `java Runner` to execute the algorithm. The input is built in the program, and follows the following format:
```
(Number of Vertices) (Number of Edges) // First PPI Network
(vertex a) (vertex b) // Edge List
(vertex a) (vertex c)
....
(Number of Vertices) (Number of Edges) // Second PPI Network
(vertex a) (vertex b) // Edge List
(vertex a) (vertex c)
```

## Creating datasets
Navigate to a `Dataset Extraction/` folder and run `java Generate` on the command line. The program will then ask for the number of vertices to be mapped, along with the maximum vertex the output subgraph should accommodate.

## Assessing Results
Navigate to a `Tester/` folder and run `java SolutionChecker` on the command line. The file name containing the answers outputted by `Runner.java` will be requested and assessed once the program has been started.
