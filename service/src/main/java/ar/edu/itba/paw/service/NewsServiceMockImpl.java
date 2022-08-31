package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsServiceMockImpl implements NewsService{

    @Autowired
    @Override
    public List<News> getNews() {
        List<News> news = new ArrayList<>();
        news.add(new News.NewsBuilder(0, 0, "BODY", "Title 1", "Subtitle 1").build());
        news.add(new News.NewsBuilder(0, 0, "This is a short card.", "Card title", "Subtitle 2").build());
        news.add(new News.NewsBuilder(0, 0, "This is a longer card with supporting text below as a natural lead-in to additional content.", "Card title 3", "Subtitle 3").build());
        news.add(new News.NewsBuilder(0, 0, "This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.", "Card title 4", "Subtitle 4").build());



        return news;
    }
}
