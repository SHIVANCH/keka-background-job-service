package repository;

import entity.Job;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JobRepository {
    private final Map<String, Job> jobs = new ConcurrentHashMap<>();

    public void save(Job job) {
        jobs.put(job.getId(), job);
    }

    public Optional<Job> findById(String id) {
        return Optional.ofNullable(jobs.get(id));
    }
}