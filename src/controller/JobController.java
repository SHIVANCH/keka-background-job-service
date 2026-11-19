package controller;

import entity.CreateJobRequest;
import entity.Job;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.JobRepository;
import services.JobService;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private final JobRepository jobRepository;

    public JobController(JobService jobService, JobRepository jobRepository) {
        this.jobService = jobService;
        this.jobRepository = jobRepository;
    }

    @PostMapping
    public ResponseEntity<Job> submitJob(@RequestBody CreateJobRequest request) {
        Job job = jobService.submitJob(request.getPayload(), request.getPriority());
        return ResponseEntity.accepted().body(job);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJob(@PathVariable String jobId) {
        return jobRepository
                .findById(jobId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}