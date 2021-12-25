package com.skillbox.repository;

import com.skillbox.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SettingRepository extends JpaRepository<Setting, Integer> {

    @Query("SELECT s FROM Setting s WHERE s.code = 'STATISTICS_IS_PUBLIC'")
    Setting findSettingsByStatistics();

    @Query("SELECT s FROM Setting s WHERE s.code = 'POST_PREMODERATION'")
    Setting findSettingsByPostPremoderation();

    @Query("SELECT s FROM Setting s WHERE s.code = 'MULTIUSER_MODE'")
    Setting findSettingsByMultiuserMode();
}
