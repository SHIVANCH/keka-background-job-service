package entity;

import java.time.LocalDateTime;

public class Job {

    private final String id;
    private final String payload;
    private final JobPriority priority;
    private JobStatus status;
    private String result;
    private final LocalDateTime createdAt;

    public Job(String id, String payload, JobPriority priority) {
        this.id = id;
        this.payload = payload;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
        this.status = JobStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }

    public JobPriority getPriority() {
        return priority;
    }

    public JobStatus getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setResult(String result) {
        this.result = result;
    }
}