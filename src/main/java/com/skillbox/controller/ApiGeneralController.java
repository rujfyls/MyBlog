package com.skillbox.controller;

import com.skillbox.controller.dto.InitResponseDTO;
import com.skillbox.controller.dto.SettingResponseDTO;
import com.skillbox.service.SettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponseDTO initResponseDTO;
    private final SettingService settingService;

    public ApiGeneralController(InitResponseDTO initResponseDTO,
                                SettingService settingService) {
        this.initResponseDTO = initResponseDTO;
        this.settingService = settingService;
    }

    @GetMapping("/init")
    public InitResponseDTO init() {
        return initResponseDTO;
    }

    @GetMapping("/settings")
    public SettingResponseDTO settings() {
        SettingResponseDTO setting = new SettingResponseDTO();
        settingService.findAll().forEach(s -> {
            if (s.getCode().equals("MULTIUSER_MODE")) {
                setting.setMultiuserMode(s.getValue().equals("YES"));
            } else if (s.getCode().equals("POST_PREMODERATION")) {
                setting.setPostPremoderation(s.getValue().equals("YES"));
            } else if (s.getCode().equals("STATISTICS_IS_PUBLIC")) {
                setting.setStatisticsIsPublic(s.getValue().equals("YES"));
            }
        });
        return setting;
    }
}
