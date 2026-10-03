import csv
import os
import matplotlib.pyplot as plt

CSV_FILE = "results/results.csv"
PLOTS_DIR = "results/plots"

os.makedirs(PLOTS_DIR, exist_ok=True)

rows = []

with open(CSV_FILE, newline="") as file:
    reader = csv.DictReader(file)

    for row in reader:
        row["n"] = int(row["n"])
        row["time_ms"] = float(row["time_ms"])
        row["steps"] = int(row["steps"])
        row["moves"] = int(row["moves"])
        row["comparisons"] = int(row["comparisons"])
        rows.append(row)


def get_rows(workload, variant="-"):
    return [
        row for row in rows
        if row["workload"] == workload
        and row["variant"] == variant
    ]


def plot_time(workload, variant="-"):
    data = get_rows(workload, variant)

    structures = sorted(set(row["structure"] for row in data))

    for structure in structures:
        structure_rows = [
            row for row in data
            if row["structure"] == structure
        ]

        structure_rows.sort(key=lambda row: row["n"])

        x = [row["n"] for row in structure_rows]
        y = [row["time_ms"] for row in structure_rows]

        plt.plot(x, y, marker="o", label=structure)

    plt.xscale("log")
    plt.xlabel("n")
    plt.ylabel("Time (ms)")
    plt.title(workload + " " + variant + " - Time vs n")
    plt.legend()
    plt.grid(True)
    plt.tight_layout()

    name = workload + "_" + variant + "_time.png"
    plt.savefig(os.path.join(PLOTS_DIR, name))
    plt.close()


def plot_metrics(workload, variant="-"):
    data = get_rows(workload, variant)

    structures = sorted(set(row["structure"] for row in data))

    for metric in ["steps", "moves", "comparisons"]:

        for structure in structures:
            structure_rows = [
                row for row in data
                if row["structure"] == structure
            ]

            structure_rows.sort(key=lambda row: row["n"])

            x = [row["n"] for row in structure_rows]
            y = [row[metric] for row in structure_rows]

            plt.plot(x, y, marker="o", label=structure)

        plt.xscale("log")
        plt.xlabel("n")
        plt.ylabel(metric.capitalize())
        plt.title(
            workload + " " + variant +
            " - " + metric.capitalize() + " vs n"
        )
        plt.legend()
        plt.grid(True)
        plt.tight_layout()

        name = workload + "_" + variant + "_" + metric + ".png"
        plt.savefig(os.path.join(PLOTS_DIR, name))
        plt.close()


# W1 - Random Access
plot_time("W1")
plot_metrics("W1")

# W2 - Search
plot_time("W2")
plot_metrics("W2")

# W3 - Head
plot_time("W3", "head")
plot_metrics("W3", "head")

# W3 - Middle
plot_time("W3", "middle")
plot_metrics("W3", "middle")

# W4 - Priority Processing
plot_time("W4")
plot_metrics("W4")

print("Plots saved to results/plots/")