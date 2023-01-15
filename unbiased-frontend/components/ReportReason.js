import FormattedDate from "./FormattedDate";

export default function ReportReason(props) {


    return (
        <tr>
            <td>{props.user}</td>
            <td>{props.reason}</td>
            <td><FormattedDate datetime={props.datetime}></FormattedDate></td>
        </tr>
    )
}