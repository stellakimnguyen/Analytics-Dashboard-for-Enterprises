package com.soen390.erp.configuration.service;

import com.soen390.erp.configuration.model.Log;
import com.soen390.erp.configuration.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void addLog(String message, String category){
        Log log = new Log(message, category);
        if (log.getUser()!=null)
            logRepository.save(log);
    }

    public List<Log> getAllLogs(int pageNo, int pageSize){

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("timestamp").descending());
        Page<Log> pagedResult = logRepository.findAll(paging);

        if (pagedResult!=null)
            return pagedResult.getContent();
        else
            return new ArrayList<>();

    }

    public List<Log> getLogsForCategory(int pageNo, int pageSize, String category){

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("timestamp").descending());
        Page<Log> pagedResult = logRepository.findAllByCategory(category, paging);

        if (pagedResult!=null)
            return pagedResult.getContent();
        else
            return new ArrayList<>();

    }
}
