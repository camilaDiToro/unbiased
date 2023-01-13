import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";
import ModalTrigger from "./ModalTrigger";
import Modal from "./Modal";

export default function DeleteButton(props) {
    const {I18n, loggedUser} = useAppContext()
    const isMyProfile = loggedUser && props.creatorId === loggedUser.id
    const showButton = props.admin || isMyProfile
    const onDelete = (e) => {
        if (props.admin) {
            alert(`Deleted ${props.comment ? 'comment' : 'article'} of id ${props.id} as admin`)
        } else {
            alert(`Deleted ${props.comment ? 'comment' : 'article'} of id ${props.id}`)
        }
        props.triggerEffect()
    }
    return showButton ? <>
        <Modal onClickHandler={onDelete} id={`binModal${props.id}`} title={I18n(props.comment ? "showNews.deleteCommentQuestion" : "profile.modal.question")} body={I18n(props.comment ? "showNews.deleteCommentBody" : "profile.modal.msg")}
        acceptText={I18n(props.comment ? "moderation.deleteComment" : "moderation.delete")}/>
        <ModalTrigger modalId={`binModal${props.id}`}>
            <div className="svg-btn hover-hand h-fit">
                <button  className="btn bin-modal"
                         id="bin_button">
                    <Tooltip text={I18n(props.comment ? "moderation.deleteComment" : "tooltip.deleteNews")} position="bottom">
                        <img src="/img/bin-svgrepo-com.svg" alt="..."
                             className="icon-profile"
                        />
                    </Tooltip>
                </button>
            </div>

        </ModalTrigger></> : <></>

}

DeleteButton.propTypes = types.DeleteButton