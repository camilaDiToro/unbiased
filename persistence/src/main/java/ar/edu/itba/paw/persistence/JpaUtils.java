package ar.edu.itba.paw.persistence;

import javax.persistence.Query;
import java.util.List;
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
}
