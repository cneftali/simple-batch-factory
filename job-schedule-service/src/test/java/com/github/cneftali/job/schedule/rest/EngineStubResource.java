package com.github.cneftali.job.schedule.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;

import com.github.cneftali.job.commons.batch.JobLaunchRequest;
import com.github.cneftali.job.commons.batch.JobLaunchingResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batchImport")
public class EngineStubResource {

    @RequestMapping(method = POST, consumes = "application/json")
    public JobLaunchingResponse create(@RequestBody final JobLaunchRequest request) {
        return new JobLaunchingResponse(request.getCreateTime().toDate().getTime(),
                                        new Date().getTime(),
                                        1,
                                        request.getJobName());
    }
}
