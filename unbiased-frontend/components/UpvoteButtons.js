import {useAppContext} from "../context";

export default function UpvoteButtons(props) {
    const {loggedUser} = useAppContext()
    let upvoteClass = ''

    if (loggedUser) {
        if (props.rating > 0)
            upvoteClass = 'upvoted'
        else if (props.rating < 0)
            upvoteClass = 'downvoted'
    }

    const handleUpvote = () => {
        props.triggerEffect()
        alert('toggled upvote')
    }

    const handleDownvote = () => {
        props.triggerEffect()
        alert('toggled downvote')
    }

    // if (props.comment) {
    //     return <div className="d-flex flex-row align-items-center gap-1">
    //         <img id="upvote"  className="svg-btn hover-hand" src={`/img/upvote.svg`} />
    //         <div id="rating" className="">5</div>
    //         <img id="downvote"  className="svg-btn hover-hand" src={`/img/downvote.svg`} />
    //     </div>
    // }

    return   <div className={props.comment ? "d-flex flex-row align-items-center gap-1" : "upvote-div-profile d-flex flex-column align-items-center m-3"}>
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

}