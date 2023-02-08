import {useAppContext} from "../context";
import {useRouter} from "next/router";
import {getResourcePath} from "../constants";

export default function UpvoteButtons(props) {
    const {loggedUser, axios} = useAppContext()
    const router = useRouter()
    let upvoteClass = ''

    if (loggedUser) {
        if (props.rating > 0)
            upvoteClass = 'upvoted'
        else if (props.rating < 0)
            upvoteClass = 'downvoted'
    }

    const handleUpvoteOrDownvote = async (s) => {
        if (!loggedUser) {
            await router.push('/login')
        } else {
            try {
                if (props.rating === 0 || props.rating > 0 && s === 'dislikes' || props.rating < 0 && s === 'likes') {
                    await axios.put(`/${props.comment ? 'comments' : 'news'}/${props.id}/${s}/${loggedUser.id}`)
                } else {
                    await axios.delete(`/${props.comment ? 'comments' : 'news'}/${props.id}/${s}/${loggedUser.id}`)
                }
                props.triggerEffect()
            } catch(e) {
                console.log(e)
            }
        }
    }

    const handleUpvote = async () => handleUpvoteOrDownvote('likes')


    const handleDownvote = async () => handleUpvoteOrDownvote('dislikes')


    return   <div className={props.comment ? "d-flex flex-row align-items-center gap-1" : "upvote-div-profile d-flex flex-column align-items-center m-3"}>
        <img id="upvote"
             className="svg-btn hover-hand"
             onClick={handleUpvote}
             src={getResourcePath(`/img/upvote${props.rating > 0 ? '-clicked' : ''}.svg`)}/>
        <div id="rating" className={upvoteClass}>
            {props.upvotes}
        </div>
        <img id="downvote"
             className="svg-btn hover-hand"
             onClick={handleDownvote}
             src={getResourcePath(`/img/downvote${props.rating < 0 ? '-clicked' : ''}.svg`)}/>
    </div>

}