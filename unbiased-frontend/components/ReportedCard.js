import {useAppContext} from "../context";
import Link from "next/link";
import Tooltip from "./Tooltip";
import PropTypes from "prop-types";
import types from "../types";
import ProfilePic from "./ProfilePic";
import DeleteButton from "./DeleteButton";
import ProfileLink from "./ProfileLink";
import {useRouter} from "next/router";
import FormattedDate from "./FormattedDate";

export default function ReportedCard(props) {
    const ctx = useAppContext()
    const router = useRouter()
    return(
        <div className="tab">
            <div className="container-fluid">
                {/*TODO: chequear si existe al menos una noticia reportada, sino mostrar msg*/}
                <div className="row row-cols-1">
                    <div className="col mb-4">
                        <div className="card h-100 d-flex flex-row p-3 max-h-300px" id="left-card">
                            <Tooltip className="reports-indicator badge badge-pill badge-danger d-flex align-items-center justify-content-center report-count" position="bottom" text={ctx.I18n("moderation.reportsNum")}>
                                {props.reportsCount}
                            </Tooltip>

                            <div className="d-flex flex-column justify-content-between w-100">
                                <div className="d-flex w-100">
                                    <div className="card-body-home pt-0">
                                        <Link href={`/news/${props.id}`} className="link mh-10">
                                            <h5 className="text-ellipsis link-text">{props.comment ? `\"${props.body}\"` : props.title}</h5>
                                        </Link>
                                        {props.comment ? <></> : <h6 className="card-subtitle py-1 text-ellipsis-2">{props.subtitle}</h6>}
                                        <span className="font-weight-light">
                                            <FormattedDate timeAgo datetime={props.datetime}></FormattedDate>
                                        </span>
                                    </div>
                                </div>
                                <div className="d-flex justify-content-between w-100">
                                    <div className="d-flex align-items-center w-auto gap-1">
                                        <div className="img-container-article">
                                            <ProfilePic hasImage={props.creator.hasImage} tier="platinum"></ProfilePic>
                                        </div>
                                        <ProfileLink {...props.creator}></ProfileLink>
                                    </div>
                                    <div className="d-flex align-items-center mr-2" role="group">
                                        <DeleteButton comment={props.comment} triggerEffect={props.triggerEffect} admin id={props.id}></DeleteButton>
                                        <Link href={`${router.pathname}/${props.id}`} className="font-weight-bold hover-hand link">
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

ReportedCard.propTypes = types.ReportedArticle