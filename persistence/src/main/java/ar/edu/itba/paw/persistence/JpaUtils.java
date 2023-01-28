package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;

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
        final List<Long> ids = (List<Long>) query.setParameter("pageSize", pageSize)
                .setParameter("offset", pageSize*(page-1))
                .getResultList().stream().map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        return ids;
    }

    public static String escapeSqlLike(String query) {
        String s = query.replaceAll("%","\\\\%").replaceAll("_","\\\\%"); // TODO encontrar una forma mas oficial (no cubre todos los casos)
        return s;
    }


    public static <T> Page<T> getPage(int page, int totalPages, Query idsQuery, TypedQuery<T> objectQuery, Function<T, Long> idGetter){

        @SuppressWarnings("unchecked")
        final List<Long> ids = (List<Long>) idsQuery.getResultList().stream()
                .map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        if(ids.isEmpty()){
            return new Page<>(Collections.emptyList(),page,1);
        }

        List<T> unorderedElementList = objectQuery.setParameter("ids", ids).getResultList();

        final Map<Long, T> elementsMapById = new HashMap<>();
        for (T element : unorderedElementList) {
            elementsMapById.put(idGetter.apply(element), element);
        }

        // ordenamos la lista de elementos haciendo un mapeo de id a elemento a traves de elementsMapById
        List<T> orderedElementList = ids.stream().map(elementsMapById::get).collect(Collectors.toList());

        return new Page<>(orderedElementList,page,totalPages);
    }
}
