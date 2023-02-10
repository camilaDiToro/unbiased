import types from "../types";
import {useAppContext} from "../context";
import Tooltip from "./Tooltip";
import Modal from "./Modal";
import ReportForm from "./ReportForm";
import ModalTrigger from "./ModalTrigger";
import {useState} from "react";
import {getResourcePath} from "../constants";

export default function ReportFlag(props) {

    let handlerArray= []

    const {I18n } = useAppContext()
    const tooltipMap = !props.comment ? {true: I18n("tooltip.articleReported"), false: I18n("tooltip.reportArticle")} :
        {true: I18n("tooltip.commentReported"), false: I18n("tooltip.reportComment")}
    return <>
        <ModalTrigger modalId={`report${props.comment ? 'Comment' : 'Article'}${props.id}`}>
            <Tooltip text={tooltipMap[`${!!props.reported}`]} position="bottom" >
                <img id="save"
                     className={`icon-index ${props.reported ? '' : 'svg-btn'} svg-bookmark`}
                     src={getResourcePath(`/img/flag${props.reported ? '-clicked' : ''}.svg`)}/>
            </Tooltip>
        </ModalTrigger>
        {props.reported ? <></> : <Modal onClickHandlerArray={handlerArray} title={I18n(props.comment ? "showNews.reportCommentQuestion" : "showNews.reportNewsQuestion")} id={`report${props.comment ? 'Comment' : 'Article'}${props.id}`}>
            <ReportForm id={props.id} comment={props.comment} triggerEffect={props.triggerEffect} handlerArray={handlerArray}></ReportForm>
        </Modal>}
    </>
}

ReportFlag.propTypes =types.ReportFlag