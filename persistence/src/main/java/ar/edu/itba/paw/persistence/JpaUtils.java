package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.news.News;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JpaUtils {

    private JpaUtils(){

    }

    public static List<Long> getIdsOfPage(Query query, int page, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) query.setParameter("pageSize", pageSize)
                .setParameter("offset", pageSize*(page-1))
                .getResultList().stream().map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        return ids;
    }
}
