package com.li.oop.service.impl;

import com.li.oop.entity.Release;
import com.li.oop.repository.ReleaseRepository;
import com.li.oop.service.intf.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReleaseServiceImpl implements ReleaseService {
    @Autowired
    private ReleaseRepository releaseRepository;

    @Override
    public List<Release> listRelease() {
        return (List<Release>) releaseRepository.findAll();
    }
}
