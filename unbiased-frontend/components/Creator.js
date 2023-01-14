import Link from "next/link";
import PositivityIndicator from "./PositivityIndicator";
import PropTypes from "prop-types";
import types from "../types";
import ModalTrigger from "./ModalTrigger";
import Modal from "./Modal";
import {useAppContext} from "../context";

export default function Creator(props) {
    const {I18n} = useAppContext()
    const onDelete = () => {
        alert(`deleted user ${props.id} from admin`)
        props.triggerEffect()
    }

return <>
    <div className="col mb-4">
        {props.admin ?         <Modal title={I18n("owner.removeAdminTitle")} body={I18n("owner.removeAdminMsg")} onClickHandler={onDelete} id={`user${props.id}modal`}></Modal>
         : <></>}        <div className="link">

            <div className="card h-100 d-flex flex-row">
                {props.hasPositivity ? <PositivityIndicator {...props.stats}></PositivityIndicator> : <></>}
                {props.admin ?                 <ModalTrigger  modalId={`user${props.id}modal`} className="svg-btn w-fit h-fit flex-grow-0 py-1 px-2">✕</ModalTrigger>
                 : <></>}
                <div className="d-flex justify-content-between p-2 w-100">
                    <div className="d-flex align-items-center w-auto gap-1">
                        <div className="img-container-article">
                            <img className="rounded-circle object-fit-cover mr-1"
                                 src={props.hasImage ? props.image : "/img/profile-image.png"} alt=""/>
                        </div>
                        <Link href={`/profile/${props.id}`} className="link-text text-white card-name-text text-ellipsis-1">{props.nameOrEmail}</Link>

                    </div>
                </div>


            </div>
        </div>

    </div>
</>
}

Creator.propTypes = types.Creator