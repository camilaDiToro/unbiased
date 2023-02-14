import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";
import {getResourcePath} from "../constants";

export default function BackButton(props) {
    const {I18n} = useAppContext()

    return <div className="back-button">
        <Tooltip onClickHandler={props.onClickHandler}  position="bottom" text={I18n("tooltip.clickToGoBack")} >
            <img className="svg-btn hover-hand back-btn" src={getResourcePath("/img/back-svgrepo-com.svg")}
                 alt="..."/>
        </Tooltip>
    </div>
}

BackButton.propTypes = types.BackButton