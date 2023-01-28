import {useState} from "react";
import {useAppContext} from "../context";

export default function ReportForm(props) {
    const {I18n, axios, loggedUser} = useAppContext()

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

    const handler = async (e) => {
        const json = {reason}
        if (props.comment) {
            const res = await axios.put(`comments/${props.id}/reports/${loggedUser.id}`, JSON.stringify(json),{
                headers: {
                    'Content-Type': 'application/json',
                }
            })
        } else {
            const json = {reason}
            const res = await axios.put(`news/${props.id}/reports/${loggedUser.id}`, JSON.stringify(json),{
                headers: {
                    'Content-Type': 'application/json',
                }
            })
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