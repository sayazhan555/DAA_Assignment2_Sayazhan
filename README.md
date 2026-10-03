# DAA Assignment 2 - Data Structures

This project implements three data structures from scratch for the Design and Analysis of Algorithms course:

- DynamicArray
- MyLinkedList
- MinHeap

The project also contains operation metrics, JUnit 5 tests and benchmarks for four workloads.

## Requirements

- Java 17+
- Maven
- Python 3 with matplotlib (only for generating plots)

## Project Structure

```text
src/main/java/
    structures/
        DynamicArray.java
        MyLinkedList.java
        MinHeap.java
    metrics/
        Metrics.java
    benchmark/
        Benchmark.java

src/test/java/
    structures/
        DynamicArrayTest.java
        MyLinkedListTest.java
        MinHeapTest.java

results/
    results.csv
    plots/

REPORT.md
plot_results.py
pom.xml
```

## Build

Run:

```bash
./mvnw clean package
```

## Run Tests

Run all JUnit tests:

```bash
./mvnw test
```

## Run Benchmark

Run `Benchmark.java` from IntelliJ IDEA.

The benchmark executes four workloads for:

```text
n = 100
n = 1,000
n = 10,000
n = 100,000
```

It uses `Random(42)`, a warm-up run and five measured runs. The median running time is saved.

The benchmark creates:

```text
results/results.csv
```

with the columns:

```text
workload,variant,structure,n,time_ms,steps,moves,comparisons
```

## Generate Plots

Run:

```bash
python3 plot_results.py
```

The generated PNG files are saved in:

```text
results/plots/
```

## Workloads

**W1 - Random Access:** 10,000 random `get(index)` operations on DynamicArray and MyLinkedList.

**W2 - Search:** 1,000 `contains(x)` operations. Half of the searched values are present and half are absent.

**W3 - Insert & Remove:** 1,000 insertions and 1,000 removals at the head and middle of DynamicArray and MyLinkedList.

**W4 - Priority Processing:** Inserts `n` values into MinHeap and extracts all values while checking non-decreasing order.

## Metrics

The implementation records:

- `steps` - array cell reads or moves to the next linked-list node;
- `moves` - shifted array elements or link updates;
- `comparisons` - comparisons between element values.

## Report

See [REPORT.md](REPORT.md) for complexity analysis, loop invariant proofs, benchmark plots and discussion.