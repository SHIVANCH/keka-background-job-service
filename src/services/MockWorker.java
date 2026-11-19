package services;

import entity.Job;
import entity.JobStatus;
import org.springframework.stereotype.Component;

@Component
public class MockWorker {

    public void process(Job job) {
        try {
            job.setStatus(JobStatus.RUNNING);
            Thread.sleep(500);
            job.setResult("Processed payload for job " + job.getId());
            job.setStatus(JobStatus.COMPLETED);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            job.setStatus(JobStatus.FAILED);
            job.setResult("Interrupted");
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            job.setResult(e.getMessage());
        }
    }
}