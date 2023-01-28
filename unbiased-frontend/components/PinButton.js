import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";
import ModalTrigger from "./ModalTrigger";
import Modal from "./Modal";

export default function PinButton(props) {
    const {I18n, loggedUser, axios} = useAppContext()
    const isMyProfile = loggedUser && props.creatorId === loggedUser.id
    const onPinOrUnpin = async (e) => {
        if (props.pinned) {
            await axios.delete(`/news/${props.creatorId}/pinnedNews`)
        } else {
            await axios.put(`/users/${props.creatorId}/pinnedNews`,null, {params: {newsId: props.id}})
        }
        props.triggerEffect()
    }
    return isMyProfile ? <>
        <Modal onClickHandler={onPinOrUnpin} id={`pingModal${props.id}`} title={props.pinned ? I18n("profile.unpin.question") : I18n("profile.pin.question")} body={props.pinned ? I18n("profile.unpin.body"): I18n("profile.pin.body")}/>

        <ModalTrigger modalId={`pingModal${props.id}`}>
            <div className="svg-btn hover-hand h-fit">

                        <Tooltip position="bottom" text={props.pinned ?  I18n("tooltip.unpin"): I18n("tooltip.pin")} >
                            <img className="icon-profile svg-btn svg-bookmark"
                                 src={`/img/pin${props.pinned ? '-clicked' : ''}.svg`} alt="" />
                        </Tooltip>

            </div>

        </ModalTrigger></> : <></>

}

PinButton.propTypes = types.PinButton