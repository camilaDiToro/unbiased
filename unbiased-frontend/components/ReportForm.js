import {useState} from "react";
import {useAppContext} from "../context";

export default function ReportForm(props) {
    const {I18n} = useAppContext()

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

    const handler = (e) => {
        if (props.comment) {
            alert(`reported comment of id ${props.id} with ${reason}`)
        } else {
            alert(`reported news of id ${props.id} with ${reason}`)
        }
        props.triggerEffect()
    }

    props.handlerArray[0] = handler

    return <>
        <div className="input-group">


            {reportReasons.map(e => <div key={e} className="form-check w-100">

                <input onChange={handleChange} name="reason" className="form-check-input" type="radio" value={e} checked={reason === e}/><label
                >{I18n(e)}</label>

            </div>)}


        </div>
    </>
}