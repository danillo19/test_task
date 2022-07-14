package com.nsu.danilllo.services.impls;

import com.nsu.danilllo.repositories.LogRepository;
import com.nsu.danilllo.model.Banner;
import com.nsu.danilllo.model.Category;
import com.nsu.danilllo.model.LogRecord;
import com.nsu.danilllo.model.NoContentReason;
import com.nsu.danilllo.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class LogServiceImpl implements LogService {
    private LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void saveLog(Banner banner, List<Category> requestedCategories, HttpServletRequest request) {
        LogRecord logRecord = new LogRecord();
        logRecord.setIp(request.getRemoteAddr());
        logRecord.setUserAgent(request.getHeader("User-Agent"));
        logRecord.setSelectedBanner(banner);
        logRecord.setSelectedCategories(requestedCategories);
        logRecord.setRequestedAt(new Date());
        if(requestedCategories.isEmpty() && banner == null) {
            logRecord.setReason(NoContentReason.NO_CONTENT_BY_REQUEST.name());
        }
        else if (!requestedCategories.isEmpty() && banner == null){
             logRecord.setReason(NoContentReason.ONLY_ONE_EXISTED_BANNER_ALREADY_SHOWN.name());
        }
        else {
            logRecord.setReason(null);
        }
        logRepository.save(logRecord);

    }

    @Override
    public Banner getLastShownBanner(HttpServletRequest request, TimeZone timeZone) {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        List<LogRecord> todayUserHistory = logRepository.findAllTodayLogsRecordByUserAgentAndIp(userAgent,ip,timeZone.getID());
        if(todayUserHistory.isEmpty()) return null;
        return todayUserHistory.get(0).getSelectedBanner();
    }
}
