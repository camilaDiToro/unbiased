import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";
import ModalTrigger from "./ModalTrigger";
import Modal from "./Modal";
import {useRouter} from "next/router";
import {getResourcePath} from "../constants";

export default function DeleteButton(props) {
    const {I18n, loggedUser, axios} = useAppContext()
    const router = useRouter()
    const isMyProfile = loggedUser && props.creatorId === loggedUser.id
    const showButton = props.admin || isMyProfile
    const onDelete = async (e) => {
        await axios.delete(`${props.comment ? 'comments' : 'news'}/${props.id}`)
        let splitted = router.pathname.split('/')
        if (splitted[1] === 'news' && !props.comment) {
            await router.push('/', undefined, {shallow:true})
        } else {
            props.triggerEffect()
        }
    }
    return showButton ? <>
        <Modal onClickHandler={onDelete} id={`binModal${props.id}`} title={I18n(props.comment ? "showNews.deleteCommentQuestion" : "profile.modal.question")} body={I18n(props.comment ? "showNews.deleteCommentBody" : "profile.modal.msg")}
        acceptText={I18n(props.comment ? "moderation.deleteComment" : "moderation.delete")}/>
        <ModalTrigger modalId={`binModal${props.id}`}>
            <div className="svg-btn hover-hand h-fit">
                <button  className="btn bin-modal"
                         id="bin_button">
                    <Tooltip text={I18n(props.comment ? "moderation.deleteComment" : "tooltip.deleteNews")} position="bottom">
                        <img src={getResourcePath("/img/bin-svgrepo-com.svg")} alt="..."
                             className="icon-profile"
                        />
                    </Tooltip>
                </button>
            </div>

        </ModalTrigger></> : <></>

}

DeleteButton.propTypes = types.DeleteButton