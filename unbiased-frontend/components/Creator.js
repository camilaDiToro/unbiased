import Link from "next/link";
import PositivityIndicator from "./PositivityIndicator";
import PropTypes from "prop-types";
import types from "../types";
import ModalTrigger from "./ModalTrigger";
import Modal from "./Modal";
import {useAppContext} from "../context";
import {getResourcePath} from "../constants";

export default function Creator(props) {
    const {I18n, api} = useAppContext()
    const onDeleteOrAdd = async () => {
        let successValue = false
        if (props.toAdd) {
            const {success} = await api.addRole(props.id, 'ROLE_ADMIN')
            successValue = success
        } else {
            const {success} = await api.removeRole(props.id, 'ROLE_ADMIN')
            successValue = success
        }
        if (successValue) {
            props.triggerEffect()
        }
    }

return <>
    <div className="col mb-4">
        {props.admin ?         <Modal title={I18n(`owner.${props.toAdd ? 'add': 'remove'}AdminTitle`)} body={I18n(`owner.${props.toAdd ? 'add': 'remove'}AdminMsg`)} onClickHandler={onDeleteOrAdd} id={`user${props.id}modal`}></Modal>
         : <></>}        <div className="link">

            <div className="card h-100 d-flex flex-row">
                {props.hasPositivity ? <PositivityIndicator {...props.stats}></PositivityIndicator> : <></>}
                {props.admin ?                 <ModalTrigger  modalId={`user${props.id}modal`} className="svg-btn w-fit h-fit flex-grow-0 py-1 px-2">{props.toAdd ? '+' : '✕'}</ModalTrigger>
                 : <></>}
                <div className="d-flex justify-content-between p-2 w-100">
                    <div className="d-flex align-items-center w-auto gap-1">
                        <div className="img-container-article">
                            <img className="rounded-circle object-fit-cover mr-1"
                                 src={props.hasImage ? props.image : getResourcePath("/img/profile-image.png")} alt="profile-image"/>
                        </div>
                        <Link href={`/profile?id=${props.id}`} className="link-text text-white card-name-text text-ellipsis-1">{props.nameOrEmail}</Link>
                    </div>
                </div>


            </div>
        </div>

    </div>
</>
}

Creator.propTypes = types.Creator