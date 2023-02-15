import types from "../types";
import {Tooltip as MTooltip} from "@mui/material";

export default function Tooltip(props) {
    return <div data-testid="tooltip" onClick={props.onClickHandler} className={props.className || ''}>
        <MTooltip title={props.text} placement={props.position}>
            {/*<div onClick={props.onClickHandler} className={`w-fit h-fit ${props.className}`} data-toggle="tooltip" data-placement={props.position || "top"} title={props.text}>*/}
            <div className="w-fit h-fit">
                {props.children}
            </div>
            {/*</div>*/}
        </MTooltip>
    </div>
}
Tooltip.propTypes = types.Tooltip