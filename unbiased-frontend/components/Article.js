import {useAppContext} from "../context";
import Link from "next/link";
import Modal from "./Modal";
import PositivityIndicator from "./PositivityIndicator";
import Tooltip from "./Tooltip";
import types from "../types";
import ModalTrigger from "./ModalTrigger";
import {news} from "../hardcoded"
import TimeAgo from 'javascript-time-ago'


import es from 'javascript-time-ago/locale/es'
import en from 'javascript-time-ago/locale/en'
import {useEffect, useState} from "react";

TimeAgo.addLocale(es)
TimeAgo.addDefaultLocale(en)




export default function Article(props) {
    const {I18n, loggedUser} = useAppContext()
    const [useTimeAgo, setTimeAgo] = useState('')
    // const [useSaved, setSaved] = useState(props.saved)
    // const [usePinned, setPinned] = useState(props.pinned)

    useEffect(() => setTimeAgo(timeAgo.format(new Date(props.datetime))), [])

    const timeAgo = new TimeAgo('en-US')
    const isMyProfile = loggedUser && loggedUser.id === props.creator.id

    const handleUpvote = async (e) => {
        // if (useUpvote > 0) {
        //     setUpvote(0)
        // } else {
        //     setUpvote(1)
        // }
        props.setNews(news)
    }

    const handleDownvote = async (e) => {
        // if (useUpvote < 0) {
        //     setUpvote(0)
        // } else {
        //     setUpvote(-1)
        // }
    }

    const handleSave = async (e) => {
        // if (useSaved) {
        //     setSaved(false)
        // } else {
        //     setSaved(true)
        // }
    }

    const handlePin = async (e) => {
        // if (usePinned) {
        //     setPinned(false)
        // } else {
        //     setPinned(true)
        // }
    }

    let upvoteClass = ''

    if (props.rating > 0)
        upvoteClass = 'upvoted'
    else if (props.rating < 0)
        upvoteClass = 'downvoted'

    return <>
        <Modal id={`pingModal${props.id}`} title={I18n("profile.pin.question")} body={I18n("profile.pin.body")} onClickHandler={handlePin}/>
        <Modal id={`binModal${props.id}`} title={I18n("profile.modal.question")} body={I18n("profile.modal.msg")} />

        <div className="col mb-4">
            <div className="card h-100 d-flex flex-row max-h-300px">
                <PositivityIndicator {...props.stats}></PositivityIndicator>
                <div className={`d-flex flex-column justify-content-between ${props.image ? 'w-60' : 'w-100'}`}>
                    <div className="d-flex w-100">
                        <div className="upvote-div-profile d-flex flex-column align-items-center m-3">
                            <img id="upvote"
                                 className="svg-btn hover-hand"
                                 onClick={handleUpvote}
                                 src={`/img/upvote${props.rating > 0 ? '-clicked' : ''}.svg`}/>
                            <div id="rating" className={upvoteClass}>
                                {props.upvotes}
                            </div>
                            <img id="downvote"
                                 className="svg-btn hover-hand"
                                 onClick={handleDownvote}
                                 src={`/img/downvote${props.rating < 0 ? '-clicked' : ''}.svg`}/>
                        </div>
                        <div className="card-body-home">
                            <Link className="link max-h-10" href={`/news/${props.id}`}>
                                <h5 className="link-text text-ellipsis">
                                    {props.title}
                                </h5>
                            </Link>
                            <h6 className="  card-subtitle py-1 text-ellipsis-2">
                                {props.subtitle}
                            </h6>
                            <span className="font-weight-light">{useTimeAgo}</span>

                            <p className="text-sm-left text-secondary mb-0 d-flex justify-content-center align-content-center w-fit">
                                <img src="/img/clock-svgrepo-com.svg"
                                     className="read-clock mr-1"/>
                                {I18n("home.read", [props.readTime])}
                            </p>

                        </div>
                    </div>
                    <div className="d-flex justify-content-between p-2 w-100">
                        <div className="d-flex align-items-center w-auto gap-1">
                            <div className="img-container-article">
                                <img className="rounded-circle object-fit-cover mr-1"
                                     src="/img/profile-image.png" alt=""/>
                            </div>
                            <Link className="link" href={`/profile/${props.creator.id}`}>
                                <div className="link-text card-name-text text-ellipsis-1">{props.creator.nameOrEmail}</div>
                            </Link>
                        </div>
                        <div className="d-flex align-items-center" role="group">

                            {props.profileArticle && isMyProfile ? <button data-toggle="modal" data-target={`#binModal${props.id}`} className="btn bin-modal"
                                                  id="bin_button">
                                <Tooltip text={I18n("tooltip.deleteNews")} position="bottom">
                                    <img src="/img/bin-svgrepo-com.svg" alt="..."
                                         className="icon-profile"
                                    />
                                </Tooltip>
                            </button> : <></>}

                            {
                                props.profileArticle ? <ModalTrigger modalId={`pingModal${props.id}`}
                                                            className="svg-btn hover-hand">
                                    <Tooltip position="bottom" text={I18n("tooltip.pin")} >
                                        <img className="icon-profile svg-btn svg-bookmark"
                                             src={`/img/pin${props.pinned ? '-clicked' : ''}.svg`} alt="" />
                                    </Tooltip>
                                </ModalTrigger> : <></>
                            }






                            {loggedUser ? <div className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
                                <Tooltip onClickHandler={handleSave} position="bottom" text={props.saved ? I18n("tooltip.articleUnsave") : I18n("tooltip.articleSave")}>
                                    <img className="w-25px svg-btn" id="bookmark"
                                         src={`/img/bookmark${props.saved ? '-clicked' : ''}.svg`}
                                         alt=""   />
                                </Tooltip>
                            </div> : <></>}

                        </div>
                    </div>
                </div>
                {props.hasImage ? <div className="bg-secondary position-relative w-40 border-15px">


                    <img src={`/news/${props.id}/image`} className="object-fit-cover"
                         alt="..."/>

                </div> : <></>}

            </div>
        </div>
    </>
}

Article.propTypes = types.Article

