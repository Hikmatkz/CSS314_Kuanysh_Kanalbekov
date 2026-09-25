from concurrent.futures import ThreadPoolExecutor
import time


def worker_task(thread_id):
    pass


def measure_time(num_threads):
    start = time.perf_counter()

    with ThreadPoolExecutor(max_workers=num_threads) as executor:
        futures = [
            executor.submit(worker_task, i)
            for i in range(num_threads)
        ]

        for future in futures:
            future.result()

    end = time.perf_counter()

    return end - start


if __name__ == "__main__":

    thread_counts = [1, 2, 4, 8, 16, 32, 64]

    print("Thread Count | Execution Time (seconds)")
    print("----------------------------------------")

    for p in thread_counts:
        elapsed = measure_time(p)

        print(f"{p:12} | {elapsed:.6f}")