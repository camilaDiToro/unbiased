
import types from "../types";
import {useAppContext} from "../context";

export default function ProgressBar(props) {
    const {I18n} = useAppContext()
    return <>
        <div>
            {props.i18n ? I18n(props.title) : props.title}
        </div>
        <div className="progress m-2">
            <div className="progress-bar custom-style-progress-bar" role="progressbar"
                 style={{width: `${props.progress*100}%`}} aria-valuenow={props.progress*100}
                 aria-valuemin="0" aria-valuemax="100">{parseInt(props.progress*100)}%
            </div>
        </div></>
}

ProgressBar.propTypes = types.ProgressBar