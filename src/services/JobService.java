package services;

import entity.Job;
import entity.JobPriority;
import entity.JobStatus;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;
import repository.JobRepository;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class JobService {

    private final JobRepository repository;
    private final MockWorker worker;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final BlockingQueue<Job> mediumQueue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public JobService(JobRepository repository, MockWorker worker) {
        this.repository = repository;
        this.worker = worker;
    }

    @PostConstruct
    void startMediumConsumer() {
        executor.submit(this::consumeMediumQueue);
    }

    @PreDestroy
    void shutdown() {
        running = false;
        executor.shutdownNow();
    }

    /**
     * HIGH — process immediately with mock worker.
     * MEDIUM — persist and enqueue for later background processing.
     * LOW — persist only; no worker triggered.
     */
    public Job submitJob(String payload, JobPriority priority) {
        if (payload == null || payload.isBlank()) {
            throw new IllegalArgumentException("payload is required");
        }
        if (priority == null) {
            throw new IllegalArgumentException("priority is required");
        }

        String jobId = UUID.randomUUID().toString();
        Job job = new Job(jobId, payload, priority);
        repository.save(job);

        switch (priority) {
            case HIGH -> {
                job.setStatus(JobStatus.QUEUED);
                executor.submit(() -> worker.process(job));
            }
            case MEDIUM -> {
                job.setStatus(JobStatus.QUEUED);
                mediumQueue.offer(job);
            }
            case LOW -> job.setStatus(JobStatus.CREATED);
        }

        return job;
    }

    private void consumeMediumQueue() {
        while (running) {
            try {
                Job job = mediumQueue.poll(1, TimeUnit.SECONDS);
                if (job != null) {
                    worker.process(job);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}