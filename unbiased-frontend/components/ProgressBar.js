export default function ProgressBar(props) {
    return <>
        <div>
            {props.title}
        </div>
        <div className="progress m-2">
            <div className="progress-bar custom-style-progress-bar" role="progressbar"
                 style={{width: `${props.progress*100}%`}} aria-valuenow={props.progress*100}
                 aria-valuemin="0" aria-valuemax="100">{props.progress*100}%
            </div>
        </div></>
}