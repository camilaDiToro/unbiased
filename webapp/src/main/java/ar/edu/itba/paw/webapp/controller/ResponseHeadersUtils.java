package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;

import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;

public final class ResponseHeadersUtils {

    private ResponseHeadersUtils(){

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

    public static Response conditionalCacheImageResponse(Image image, Request request){
        EntityTag eTag = new EntityTag(String.valueOf(image.getImageId()));
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);

        Response.ResponseBuilder response = request.evaluatePreconditions(eTag);
        if (response == null) {
            return Response
                    .ok(new ByteArrayInputStream(image.getBytes()))
                    .cacheControl(cacheControl)
                    .tag(eTag)
                    .type(image.getDataType())
                    .build();
        }

        return response.build();
    }
}
