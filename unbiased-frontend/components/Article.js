import Link from "next/link";

export default function Article(props) {

    return <>
        <div className="col mb-4 max-h-300px">
            <div className="card h-100 d-flex flex-row h-100">
                <img src="/img/happy-positivity.svg" alt="..."
                     className="quality-indicator "
                     data-toggle="tooltip" data-placement="top" title="58% upvoted" />
                <div
                    className={`d-flex flex-column justify-content-between ${props.image ? 'w-60' : 'w-100'}`}>
                    <div className="d-flex w-100">
                        <div className="upvote-div-home d-flex flex-column align-items-center m-3">

                            {/*<c:if test="${loggedUser != null}">*/}
                                <img id="upvote"
                                     className="svg-btn hover-hand"
                                     src={`/img/upvote${props.rating === 'upvoted' ? '-clicked' : ''}.svg`}/>
                                <div id="rating" className="${rating.toString()}">
                                    123
                                </div>
                                <img id="downvote"
                                     className="svg-btn hover-hand"
                                     src={`/img/downvote${props.rating === 'downvoted' ? '-clicked' : ''}.svg`}/>
                            {/*</c:if>*/}
                        </div>
                        <div className="card-body-home">
                            <Link className="link title-principal-card"
                               href={`/news/${props.newsId}`}>
                                <h5 className="link-text text-ellipsis-3">
                                    TITLE
                                </h5>
                            </Link>
                            <h6 className="card-subtitle py-1 text-ellipsis-2 text-white">
                                SUBTITLE
                            </h6>
                            <span className="font-weight-light">2 months ago</span>

                            <div>
                                <p className="text-sm-left text-secondary mb-0 text-white d-flex align-content-center gap-1 op-09">
                                    <img
                                        src="/img/clock-svgrepo-com.svg"
                                        alt="..." className="read-clock"/>
                                    1 min read
                                </p>
                            </div>


                        </div>
                    </div>
                    <div className="d-flex justify-content-between p-2 w-100">
                        <div className="d-flex align-items-center w-auto gap-1">
                            <div className="img-container-article">
                                {props.image ? <img className="rounded-circle object-fit-cover mr-1"
                                                    src=""
                                                    alt=""/> : <img className="rounded-circle object-fit-cover mr-1"
                                                                   src="/img/profile-image.png"
                                                                   alt=""/>}
                            </div>
                            <Link className="link"
                               href={`/profile/${props.creator}`}>
                                <div id="profile_name_card"
                                     className="card-name-text text-ellipsis-1">USER</div>

                            </Link>
                        </div>
                        <div className="d-flex align-items-center" role="group">
                            <div
                                className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
                                <Link href="">
                                <img className="icon-index svg-btn svg-bookmark bookmark"
                                     src="/img/comment.svg" alt=""
                                     data-toggle="tooltip" data-placement="bottom"
                                     title="Comentar"/>

                                </Link>
                            </div>
                            {/*<c:if test="${loggedUser != null}">*/}
                                <div
                                    className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
                                    <img
                                         className="icon-index svg-btn svg-bookmark bookmark"
                                         src={`/img/bookmark${props.saved ? '-clicked' : ''}.svg`}
                                     data-toggle="tooltip" data-placement="bottom" title="Guardarrrr"/>
                                </div>
                            {/*</c:if>*/}

                        </div>
                    </div>
                </div>
                {props.image ? <div className="position-relative w-40 custom-rounded-corners ml-2">
                    <img src=""
                         className="object-fit-cover" alt="..."/>
                </div> : <></>}

            </div>
        </div>
    </>
}