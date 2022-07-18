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
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {
    private LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void saveLog(Banner banner, List<Category> requestedCategories, HttpServletRequest request,
                        NoContentReason reason) {
        LogRecord logRecord = new LogRecord();
        logRecord.setIp(request.getRemoteAddr());
        logRecord.setUserAgent(request.getHeader("User-Agent"));
        logRecord.setSelectedBanner(banner);
        logRecord.setSelectedCategories(requestedCategories);
        if(banner != null) {
            logRecord.setSelectedBannerPrice(banner.getPrice());
        }
        else {
            logRecord.setSelectedBannerPrice(null);
        }
        logRecord.setRequestedAt(new Date());
        if(reason != null) {
            logRecord.setReason(reason.toString());
        }
        else {
            logRecord.setReason(null);
        }
        logRepository.save(logRecord);

    }

    @Override
    public List<Banner> getLastShownBanners(HttpServletRequest request, TimeZone timeZone) {
        String userAgent = request.getHeader("User-Agent");
        String ip = request.getRemoteAddr();
        List<LogRecord> todayUserHistory = logRepository.findAllTodayLogsRecordByUserAgentAndIp(userAgent, ip, timeZone.getID());
        return todayUserHistory.stream().map(LogRecord::getSelectedBanner).collect(Collectors.toList());
    }


    @Override
    public NoContentReason getNoContentReason(Banner chosenBanner, Set<Banner> acceptableBannersByRequest) {
        if(chosenBanner != null) return null;

        if(acceptableBannersByRequest.isEmpty()) return NoContentReason.NO_CONTENT_BY_REQUEST;
        else return NoContentReason.ALL_CONTENT_ALREADY_SHOWN;
    }
}
