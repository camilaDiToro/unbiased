import {useState} from "react";
import {useAppContext} from "../context";
import types from "../types";

export default function ReportForm(props) {
    const {I18n, api} = useAppContext()

    const [reason, setReason] = useState('')

    const reportReasons = ["report.inappropiate", "report.notSerious", "report.violent", "report.lie"]


    const handleChange = (e) => {
        const el = e.target
        if (el.checked) {
            setReason(el.value)
        } else {
            setReason('')
        }

    }

    const formValid = () => {
        return !!reason
    }

    const handler = async (e) => {
        if (!formValid())
            return;
        if (props.comment) {
            const {success} = await api.reportComment(props.id, reason)
            if (success) {
                props.triggerEffect()
            }
        } else {
            const {success} = await api.reportArticle(props.id, reason)
            if (success) {
                props.triggerEffect()
            }
        }
    }


    return <>
        <div className="input-group">


            {reportReasons.map(e => <div key={e} className="form-check w-100">

                <input onChange={handleChange} name="reason" className="form-check-input" type="radio" value={e} checked={reason === e}/><label
                >{I18n(e)}</label>

            </div>)}


        </div>
        <div className="modal-footer">
            <button onClick={handler} data-dismiss="modal" type="submit" className={`btn btn-primary ${formValid() ? '' : 'disabled noHover '}`} >
                {props.acceptText || I18n("profile.modal.accept")}
            </button>
        </div>
    </>
}

ReportForm.propTypes = types.ReportForm