import {useAppContext} from "../context";
import PropTypes from "prop-types";
import types from "../types";


export default function Modal(props) {
    const {I18n} = useAppContext()
    function handleCloseModal(){
        document.getElementById(props.id).classList.remove("show", "d-block");
        document.querySelectorAll(".modal-backdrop")
            .forEach(el => el.classList.remove("modal-backdrop"));
    }

    const handler = (e) => {
        props.onClickHandler && props.onClickHandler(e);
        props.onClickHandlerArray && props.onClickHandlerArray[0] && props.onClickHandlerArray[0](e)
        handleCloseModal()
    }


    return <>
        <div className="modal fade" id={props.id} >
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">
                            {props.title}
                        </h5>
                        <button  type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        {props.body}
                        {props.children}
                    </div>
                    <div className="modal-footer">
                        <button data-dismiss="modal" type="submit" className="btn btn-primary" onClick={handler}>
                        {props.acceptText || I18n("profile.modal.accept")}
                    </button>
                    </div>
                </div>
            </div>
        </div>
    </>
}

Modal.propTypes = types.Modal