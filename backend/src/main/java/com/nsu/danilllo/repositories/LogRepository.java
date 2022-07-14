package com.nsu.danilllo.repositories;

import com.nsu.danilllo.model.LogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<LogRecord, Long> {
    Optional<LogRecord> findById(Long id);

    Optional<LogRecord> findByUserAgentAndIp(String userAgent, String ip);

    @Query(value = "Select * from log_record where ip=:ip and user_agent=:userAgent and requested_at >=  " +
            "DATE (CONVERT_TZ(CURTIME(),@@global.time_zone,:timezone)) " +
            "and requested_at < DATE (CONVERT_TZ(CURTIME(),@@global.time_zone,:timezone)) + " +
            "interval 1 day order by requested_at desc", nativeQuery = true)
    List<LogRecord> findAllTodayLogsRecordByUserAgentAndIp(@Param("userAgent") String userAgent,
                                                           @Param("ip") String Ip,
                                                           @Param("timezone") String timezone);
}
