package ar.edu.itba.paw.persistence;

import org.springframework.jdbc.core.RowMapper;

public class JdbcUtils {

    private JdbcUtils(){

    }
    public static final RowMapper<Integer> ROW_COUNT_MAPPER = (rs, rowNum) -> rs.getInt("row_count");
    public static int getPageCount(int rowsCount, double pageSize){
        int total = (int) Math.ceil(rowsCount/pageSize);
        return total==0?1:total;
    }
}
