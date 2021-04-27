package com.li.oop.service.impl;

import com.li.oop.entity.Application;
import com.li.oop.exception.ApplicationNotFoundException;
import com.li.oop.repository.ApplicationRepository;
import com.li.oop.service.intf.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationRepository appRepository;

    @Override
    public List<Application> listApplications() {
        return (List<Application>) appRepository.findAll();
    }

    @Override
    public Application findApplication(long id) {
        Optional<Application> optionalApp = appRepository.findById(id);
        if (optionalApp.isPresent()) {
            return optionalApp.get();
        } else {
            throw new ApplicationNotFoundException("Application not found");
        }
    }

    @Override
    public Application addApplication(Application application) {
        return appRepository.save(application);
    }
}
