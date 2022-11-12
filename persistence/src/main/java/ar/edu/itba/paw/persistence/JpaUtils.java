package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    public static String escapeSqlLike(String query) {
        return query.replaceAll("_","[_]").replaceAll("%","[%]"); // TODO encontrar una forma mas oficial (no cubre todos los casos)
    }

    public static <T> Page<T> getPage(int page, int totalPages, Query idsQuery, TypedQuery<T> objectQuery, Function<T, Long> idGetter){

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) idsQuery.getResultList().stream()
                .map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        if(ids.isEmpty()){
            return new Page<>(Collections.emptyList(),page,1);
        }

        List<T> reportedComments = objectQuery.setParameter("ids", ids).getResultList();

        Map<Long, T> reportDetailMap = new HashMap<>();
        for (T reportedComment : reportedComments) {
            reportDetailMap.put(idGetter.apply(reportedComment), reportedComment);
        }
        // map id -> ReportDetail
        reportedComments =  ids.stream().map(reportDetailMap::get).collect(Collectors.toList());

        return new Page<>(reportedComments,page,totalPages);
    }
}
