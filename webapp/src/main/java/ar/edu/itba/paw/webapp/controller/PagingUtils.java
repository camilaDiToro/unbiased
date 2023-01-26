package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public final class PagingUtils {

    private PagingUtils(){

    }

    public static Response pagedResponse(Page<?> page, Response.ResponseBuilder responseBuilder, UriInfo uriInfo){

        responseBuilder
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", page.getTotalPages()).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");

        int currentPage = page.getCurrentPage();

        if(currentPage != 1){
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", currentPage-1).build(), "prev");
        }

        if(currentPage != page.getTotalPages()){
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", currentPage+1).build(), "next");
        }

        return responseBuilder.build();
    }
}
