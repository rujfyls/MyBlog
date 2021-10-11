package com.skillbox.service;

import com.skillbox.entity.Setting;
import com.skillbox.repository.SettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public List<Setting> findAll() {
        return settingRepository.findAll();
    }
}
