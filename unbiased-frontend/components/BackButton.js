import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";

export default function BackButton(props) {
    const {I18n} = useAppContext()
    return <Tooltip onClickHandler={props.onClickHandler} className="back-button" position="bottom" text={I18n("tooltip.clickToGoBack")} >

                <img className="svg-btn hover-hand back-btn" src="/img/back-svgrepo-com.svg"
                     alt="..."/>

    </Tooltip>
}

BackButton.propTypes = types.BackButton