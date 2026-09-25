from concurrent.futures import ThreadPoolExecutor
import threading
import time
import math


def worker_task(thread_id, team_size):
    native_tid = threading.get_native_id()

    result = 0.0

    # Artificial CPU workload
    for i in range(10_000_000):
        result += math.sqrt(i)

    print(
        f"Thread {thread_id} finished | "
        f"Native TID: {native_tid}"
    )


def run_team(num_threads):

    print(f"\n--- Running {num_threads} threads ---")

    start = time.perf_counter()

    with ThreadPoolExecutor(max_workers=num_threads) as executor:

        futures = [
            executor.submit(worker_task, i, num_threads)
            for i in range(num_threads)
        ]

        for future in futures:
            future.result()

    end = time.perf_counter()

    print(f"Execution time: {end - start:.3f} seconds")


if __name__ == "__main__":
    run_team(1)
    run_team(2)
    run_team(4)
    run_team(8)