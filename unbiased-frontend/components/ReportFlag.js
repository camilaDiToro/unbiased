import types from "../types";
import {useAppContext} from "../context";
import Tooltip from "./Tooltip";
import Modal from "./Modal";
import ReportForm from "./ReportForm";
import ModalTrigger from "./ModalTrigger";
import {getResourcePath} from "../constants";
import {useRouter} from "next/router";
import Link from "next/link";

export default function ReportFlag(props) {


    const {I18n, loggedUser} = useAppContext()
    const tooltipMap = !props.comment ? {true: I18n("tooltip.articleReported"), false: I18n("tooltip.reportArticle")} :
        {true: I18n("tooltip.commentReported"), false: I18n("tooltip.reportComment")}

    return <>

        {loggedUser ? <div>
            <ModalTrigger modalId={`report${props.comment ? 'Comment' : 'Article'}${props.id}`}>
                <Tooltip text={tooltipMap[`${!!props.reported}`]} position="bottom" >
                    <img data-testid="flag-img" id="save"
                         className={`icon-index ${props.reported ? '' : 'svg-btn'} svg-bookmark`}
                         src={getResourcePath(`/img/flag${props.reported ? '-clicked' : ''}.svg`)}
                         alt="..."
                    />
                </Tooltip>
            </ModalTrigger>
            {props.reported ? <></> : <Modal noFooter title={I18n(props.comment ? "showNews.reportCommentQuestion" : "showNews.reportNewsQuestion")} id={`report${props.comment ? 'Comment' : 'Article'}${props.id}`}>
                <ReportForm id={props.id} comment={props.comment} triggerEffect={props.triggerEffect} ></ReportForm>
            </Modal>}
        </div>
            :
            <Link href="/login">
                <Tooltip text={tooltipMap[`${!!props.reported}`]} position="bottom" >
                    <img data-testid="flag-img" id="save"
                         className={`icon-index ${props.reported ? '' : 'svg-btn'} svg-bookmark`}
                         src={getResourcePath('/img/flag.svg')}
                         alt="..."
                    />
                </Tooltip>
            </Link>

        }

    </>
}

ReportFlag.propTypes =types.ReportFlag