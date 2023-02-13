import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";
import ModalTrigger from "./ModalTrigger";
import Modal from "./Modal";
import {getResourcePath} from "../constants";

export default function PinButton(props) {
    const {I18n, loggedUser, api} = useAppContext()
    const isMyProfile = loggedUser && props.creatorId === loggedUser.id
    const onPinOrUnpin = async (e) => {
        if (props.pinned) {
            const {success} = await api.deletePin()
            if (success) {
                props.triggerEffect()
            }
        } else {
            const {success} = await api.addPin(props.id)
            if (success) {
                props.triggerEffect()
            }
        }
    }
    return isMyProfile ? <>
        <Modal data-testid="modal" onClickHandler={onPinOrUnpin} id={`pingModal${props.id}`} title={props.pinned ? I18n("profile.unpin.question") : I18n("profile.pin.question")} body={props.pinned ? I18n("profile.unpin.body"): I18n("profile.pin.body")}/>

        <ModalTrigger data-testid="modal-trigger" modalId={`pingModal${props.id}`}>
            <div className="svg-btn hover-hand h-fit">

                        <Tooltip position="bottom" text={props.pinned ?  I18n("tooltip.unpin"): I18n("tooltip.pin")} >
                            <img data-testid="pin-img" className="icon-profile svg-btn svg-bookmark"
                                 src={getResourcePath(`/img/pin${props.pinned ? '-clicked' : ''}.svg`)} alt="" />
                        </Tooltip>

            </div>

        </ModalTrigger></> : <></>

}

PinButton.propTypes = types.PinButton