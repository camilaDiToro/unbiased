export default function Tooltip(props) {
    return <div data-testid="tooltip" onClick={props.onClickHandler} className={`w-fit h-fit ${props.className}`} data-toggle="tooltip" data-placement={props.position || "top"} title={props.text}>
        {props.children}
    </div>
}