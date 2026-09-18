public class Part3 {

    static final long TOTAL_POINTS = 100_000_000;

    static long calculatePi(int threadCount) throws InterruptedException {

        Thread[] threads = new Thread[threadCount];
        long[] localHits = new long[threadCount];

        long pointsPerThread = TOTAL_POINTS / threadCount;

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;

            threads[i] = new Thread(() -> {

                long hits = 0;

                long start = threadId * pointsPerThread;
                long end = (threadId == threadCount - 1)
                        ? TOTAL_POINTS
                        : start + pointsPerThread;

                for (long j = start; j < end; j++) {

                    double x = Math.random();
                    double y = Math.random();

                    if (x * x + y * y <= 1.0) {
                        hits++;
                    }
                }

                // Each thread writes only to its own element
                localHits[threadId] = hits;
            });

            threads[i].start();
        }

        // Wait for all threads
        for (Thread thread : threads) {
            thread.join();
        }

        // Reduction: combine partial sums
        long totalHits = 0;

        for (long hits : localHits) {
            totalHits += hits;
        }

        return totalHits;
    }

    public static void main(String[] args) throws Exception {

        int[] threadCounts = {1, 2, 4, 8, 16, 32};

        System.out.println("OpenMP-Style Reduction Benchmark");
        System.out.println("Iterations: " + TOTAL_POINTS);
        System.out.println();

        for (int threads : threadCounts) {

            long startTime = System.nanoTime();

            long hits = calculatePi(threads);

            long endTime = System.nanoTime();

            double executionTime =
                    (endTime - startTime) / 1_000_000_000.0;

            double pi = 4.0 * hits / TOTAL_POINTS;

            System.out.printf(
                    "Threads: %2d | Pi: %.6f | Time: %.4f seconds%n",
                    threads,
                    pi,
                    executionTime
            );
        }
    }
}