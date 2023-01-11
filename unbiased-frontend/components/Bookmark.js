import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";

export default function Bookmark(props) {
    const {loggedUser, I18n} = useAppContext()
    return <>
        {loggedUser ? <div className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
            <Tooltip onClickHandler={props.onSave} position="bottom" text={props.saved ? I18n("tooltip.articleUnsave") : I18n("tooltip.articleSave")}>
                <img className="w-25px svg-btn" id="bookmark"
                     src={`/img/bookmark${props.saved ? '-clicked' : ''}.svg`}
                     alt=""   />
            </Tooltip>
        </div> : <></>}</>
}

Bookmark.propTypes = types.Bookmark