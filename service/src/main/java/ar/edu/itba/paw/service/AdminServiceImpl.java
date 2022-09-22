package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.persistence.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

    private final AdminDao adminDao;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public void reportNews(long newsId, long userId, ReportReason reportReason) {
        adminDao.reportNews(newsId,userId,reportReason);
    }

    @Override
    public Page<ReportedNews> getReportedNews(int page) {
        return adminDao.getReportedNews(page);
    }
}
