export default function Modal(props) {

    return <>
        <div className="modal fade" id={props.id} aria-hidden="true">
            <div className="modal-dialog modal-dialog-centered">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">
                            {props.title}
                        </h5>
                        <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div className="modal-body">
                        {props.body}
                    </div>
                    <div className="modal-footer">
                        <button type="submit" className="btn btn-primary" onClick={props.onSubmitHandler}>
                            {props.acceptText || "profile.modal.accept"}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </>
}