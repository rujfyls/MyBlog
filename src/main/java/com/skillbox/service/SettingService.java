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

    public Setting getSettingsByStatistics() {
        return settingRepository.findSettingsByStatistics();
    }

    public Setting getSettingsByPostPremoderation() {
        return settingRepository.findSettingsByPostPremoderation();
    }

    public Setting getSettingsByMultiuserMode() {
        return settingRepository.findSettingsByMultiuserMode();
    }

    public void save(Setting setting) {
        settingRepository.save(setting);
    }
}
