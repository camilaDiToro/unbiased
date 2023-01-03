import {useAppContext} from "../context";
import Link from "next/link";
import Modal from "./Modal";

export default function ProfileArticle(props) {
    const ctx = useAppContext()
    return <>
        <Modal id={`pingModal${props.id}`} title={ctx.I18n("profile.pin.question")} body={ctx.I18n("profile.pin.body")} onSubmitHandler={() => alert('hola')}/>
        <Modal id={`binModal${props.id}`} title={ctx.I18n("profile.modal.question")} body={ctx.I18n("profile.modal.msg")} onSubmitHandler={() => alert('hola')}/>

        <div className="col mb-4">
            <div className="card h-100 d-flex flex-row max-h-300px">
                <img src="/img/happy-positivity.svg" alt="..."
                     className="quality-indicator" data-toggle="tooltip"
                     data-placement="top" title="home.upvotes + home.interactions"/>
                <div className={`d-flex flex-column justify-content-between ${props.image ? 'w-60' : 'w-100'}`}>
                    <div className="d-flex w-100">
                        <div className="upvote-div-profile d-flex flex-column align-items-center m-3">

                            <img id="upvote"
                                 className="svg-btn hover-hand"
                                 src={`/img/upvote${props.upvoted ? '-clicked' : ''}.svg`}/>
                            <div id="rating" className="upvote">
                                5
                            </div>
                            <img id="downvote"
                                 className="svg-btn hover-hand"
                                 src={`/img/downvote${props.downvoted ? '-clicked' : ''}.svg`}/>
                        </div>
                        <div className="card-body-home">
                            <Link className="link max-h-10" href="/">
                                <h5 className="link-text text-ellipsis">
                                    TITLE
                                </h5>
                            </Link>
                            <h6 className="  card-subtitle py-1 text-ellipsis-2">
                                SUBTITLE
                            </h6>
                            <span className="font-weight-light">0 minutes ago</span>

                            <p className="text-sm-left text-secondary mb-0">
                                <img src="/img/clock-svgrepo-com.svg"
                                     className="read-clock"/>
                                {"home.read"}
                            </p>

                        </div>
                    </div>
                    <div className="d-flex justify-content-between p-2 w-100">
                        <div className="d-flex align-items-center w-auto gap-1">
                            <div className="img-container-article">
                                <img className="rounded-circle object-fit-cover mr-1"
                                     src="/img/profile-image.png" alt=""/>
                            </div>
                            <Link className="link" href="/">
                                <div className="link-text card-name-text text-ellipsis-1">USER</div>
                            </Link>
                        </div>
                        <div className="d-flex align-items-center" role="group">

                            {/*<c:if test="${isMyProfile && loggedUser == article.user}">*/}
                                <button data-toggle="modal" data-target={`#binModal${props.id}`} className="btn bin-modal"
                                        id="bin_button">
                                    <img src="/img/bin-svgrepo-com.svg" alt="..."
                                         className="icon-profile" data-toggle="tooltip" data-placement="bottom"
                                         title="tooltip.deleteNews"/>
                                </button>

                                <div data-toggle="modal" data-target={`#pingModal${props.id}`}
                                     className="svg-btn hover-hand">
                                    <img className="icon-profile svg-btn svg-bookmark"
                                         src="/img/pin.svg" alt="" data-toggle="tooltip"
                                         data-placement="bottom" title="tooltip.pin"/>
                                </div>


                            {/*</c:if>*/}

                            {/*<c:if test="${loggedUser != null}">*/}
                                <div className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
                                    <img className="w-25px svg-btn" id="bookmark"
                                         src={`/img/bookmark${props.saved ? '-clicked' : ''}.svg`}
                                    alt=""  data-toggle="tooltip" data-placement="bottom" title="tooltip.articleSave"/>
                                </div>
                            {/*</c:if>*/}
                        </div>
                    </div>
                </div>
                {props.image ? <div className="bg-secondary position-relative w-40 border-15px">


                    <img src="" className="object-fit-cover"
                         alt="..."/>

                </div> : <></>}

            </div>
        </div>
    </>
}

