import PropTypes from "prop-types";

export default function ModalTrigger(props) {

    return <div data-toggle="modal" data-target={`#${props.modalId}`} className={`w-fit h-fit ${props.className}`}>
        {props.children}
    </div>
}

ModalTrigger.propTypes = {
    modalId: PropTypes.string.isRequired
}