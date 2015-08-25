package com.github.cneftali.job.schedule.rest.repository;

import com.github.cneftali.job.schedule.rest.domain.JobRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "job", path = "jobs")
public interface JobRequestRespository extends CrudRepository<JobRequest, Long> {
}
