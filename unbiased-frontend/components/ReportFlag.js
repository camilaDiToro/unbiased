import types from "../types";
import {useAppContext} from "../context";
import Tooltip from "./Tooltip";
import Modal from "./Modal";
import ReportForm from "./ReportForm";
import ModalTrigger from "./ModalTrigger";

export default function ReportFlag(props) {
    const {I18n, loggedUser} = useAppContext()
    const tooltipMap = !props.comment ? {true: I18n("tooltip.articleReported"), false: I18n("tooltip.reportArticle")} :
        {true: I18n("tooltip.commentReported"), false: I18n("tooltip.reportComment")}
    return <>
        <ModalTrigger modalId={`report${props.comment ? 'Comment' : 'Article'}${props.id}`}>
            <Tooltip text={tooltipMap[`${!!props.reported}`]} position="bottom" >
                <img id="save"
                     className="icon-index svg-btn svg-bookmark bookmark"
                     src={`/img/flag${props.reported ? '-clicked' : ''}.svg`}/>
            </Tooltip>
        </ModalTrigger>
        {props.reported ? <></> : <Modal title={I18n(props.comment ? "showNews.reportCommentQuestion" : "showNews.reportNewsQuestion")} id={`report${props.comment ? 'Comment' : 'Article'}${props.id}`}>
            <ReportForm></ReportForm>
        </Modal>}
    </>
}

ReportFlag.propTypes =types.ReportFlag