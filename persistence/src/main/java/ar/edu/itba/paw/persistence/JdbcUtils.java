package ar.edu.itba.paw.persistence;


public final class JdbcUtils {

    private JdbcUtils(){

    }
    public static int getPageCount(int rowsCount, double pageSize){
        int total = (int) Math.ceil(rowsCount/pageSize);
        return total==0?1:total;
    }
}
