import {useAppContext} from "../context";
import Link from "next/link";
import Modal from "./Modal";
import PositivityIndicator from "./PositivityIndicator";
import Tooltip from "./Tooltip";
import types from "../types";
import ModalTrigger from "./ModalTrigger";
import {news} from "../hardcoded"
import FormattedDate from "./FormattedDate";
import ProfilePic from "./ProfilePic";
import ProfileLink from "./ProfileLink";
import Bookmark from "./Bookmark";
import DeleteButton from "./DeleteButton";
import PinButton from "./PinButton";




export default function Article(props) {
    const {I18n, loggedUser} = useAppContext()

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
                            <span className="font-weight-light"><FormattedDate timeAgo datetime={props.datetime}></FormattedDate></span>

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
                                <ProfilePic tier={props.creator.tier} id={props.creator.id} hasImage={props.creator.hasImage}></ProfilePic>
                            </div>
                            <ProfileLink {...props.creator}></ProfileLink>
                        </div>
                        <div className="d-flex align-items-center" role="group">

                            {props.profileArticle ? <DeleteButton creatorId={props.creator.id} id={props.id} ></DeleteButton> : <></>}

                            {
                                props.profileArticle ? <PinButton creatorId={props.creator.id} id={props.id} pinned={props.pinned}></PinButton> : <></>
                            }


                            <Bookmark saved={props.saved} onSave={handleSave}></Bookmark>

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

