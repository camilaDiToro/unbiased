import {useAppContext} from "../context";
import {useRouter} from "next/router";
import {getResourcePath} from "../constants";
import types from "../types";

export default function UpvoteButtons(props) {
    const {loggedUser, api} = useAppContext()
    const router = useRouter()
    let upvoteClass = ''

    if (loggedUser) {
        if (props.rating > 0)
            upvoteClass = 'upvoted'
        else if (props.rating < 0)
            upvoteClass = 'downvoted'
    }

    const handleUpvoteOrDownvote = async (s, add, remove) => {
        if (!loggedUser) {
            await router.push('/login')
        } else {
            if (props.rating === 0 || props.rating > 0 && s === 'dislikes' || props.rating < 0 && s === 'likes') {
                const {success} = await add(props.id)
                success && props.triggerEffect()
            } else {
                const {success} = await remove(props.id)
                success && props.triggerEffect()

            }
        }
    }

    const handleUpvote = async () => handleUpvoteOrDownvote('likes', (a) => props.comment ? api.upvoteComment(a): api.upvoteArticle(a),(a) => props.comment ? api.upvoteCommentRemove(a): api.upvoteArticleRemove(a))


    const handleDownvote = async () => handleUpvoteOrDownvote('dislikes', (a) => props.comment ? api.downvoteComment(a): api.downvoteArticle(a),(a) => props.comment ? api.downvoteCommentRemove(a): api.downvoteArticleRemove(a))


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

UpvoteButtons.propTypes = types.UpvoteButtons