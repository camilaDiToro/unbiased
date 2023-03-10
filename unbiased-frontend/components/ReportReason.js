import FormattedDate from "./FormattedDate";
import {useAppContext} from "../context";
import types from "../types";

export default function ReportReason(props) {

    const {I18n} = useAppContext()
    return (
        <tr>
            <td>{props.user}</td>
            <td>{I18n(props.reason)}</td>
            <td><FormattedDate datetime={props.datetime}></FormattedDate></td>
        </tr>
    )
}

ReportReason.propTypes = types.ReportReason