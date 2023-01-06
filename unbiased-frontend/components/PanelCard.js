import {useAppContext} from "../context";
import Link from "next/link";
import Tooltip from "./Tooltip";

export default function PanelCard() {
    const ctx = useAppContext()

    return(
        <div className="tab">
            <div className="container-fluid">
                {/*TODO: chequear si existe al menos una noticia reportada, sino mostrar msg*/}
                <div className="row row-cols-1">
                    {/*TODO: <c:set var="maxLength" value="${100}"/>
                             <c:forEach var="reportedNews" items="${newsList}">
                             <c:set var="article" value="${reportedNews}"/>
                             <c:set var="newsId" value="${article.newsId}"/>
                             <c:set var="creator" value="${reportedNews.creator}"/>*/}

                    {/*TODO: Modal :)*/}
                    <div className="col mb-4">
                        <div className="card h-100 d-flex flex-row p-3 max-h-300px" id="left-card">
                            <Tooltip className="reports-indicator badge badge-pill badge-danger d-flex align-items-center justify-content-center report-count" position="bottom" text={ctx.I18n("moderation.reportsNum")}>
                                1
                            </Tooltip>

                            <div className="d-flex flex-column justify-content-between w-100">
                                <div className="d-flex w-100">
                                    <div className="card-body-home pt-0">
                                        <Link href="/" className="link mh-10">
                                            <h5 className="text-ellipsis link-text">TITULO</h5>
                                        </Link>
                                        <h6 className="card-subtitle py-1 text-ellipsis-2">SUBTITULO</h6>
                                        <span className="font-weight-light">
                                            timeAmount
                                        </span>
                                    </div>
                                </div>
                                <div className="d-flex justify-content-between w-100">
                                    <div className="d-flex align-items-center w-auto gap-1">
                                        <div className="img-container-article">
                                        {/*TODO: chequear si la persona tiene imagen*/}
                                            <img src="/img/profile-image.png" className="rounded-circle object-fit-cover mr-1" alt="..."/>
                                        </div>
                                        <Link href="/" className="link">
                                            <div className="card-name-text text-ellipsis-1 link-text">
                                                Nombre
                                            </div>
                                        </Link>
                                    </div>
                                    <div className="d-flex align-items-center mr-2" role="group">
                                        <Tooltip className="btn bin-modal" position="bottom" text={ctx.I18n("tooltip.deleteNews")}>
                                            <img src="/img/bin-svgrepo-com.svg" alt="..." className="bin-image"  />
                                        </Tooltip>
                                        <Link href="/" className="font-weight-bold hover-hand link">
                                            <div className="link-text">{ctx.I18n("moderation.details")}</div>
                                        </Link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}