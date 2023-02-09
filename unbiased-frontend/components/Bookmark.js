import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";
import {getResourcePath} from "../constants";

export default function Bookmark(props) {
    const {loggedUser, I18n, axios} = useAppContext()

    const onSave = async (e) => {
        if(props.saved) {
            await axios.delete(`/news/${props.id}/bookmarks/${loggedUser.id}`)
        } else {
            await axios.put(`/news/${props.id}/bookmarks/${loggedUser.id}`)
        }
        props.triggerEffect()
    }
    return <>
        {loggedUser ? <div className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
            <Tooltip onClickHandler={onSave} position="bottom" text={props.saved ? I18n("tooltip.articleUnsave") : I18n("tooltip.articleSave")}>
                <img className="w-25px svg-btn" id="bookmark"
                     src={getResourcePath(`/img/bookmark${props.saved ? '-clicked' : ''}.svg`)}
                     alt=""   />
            </Tooltip>
        </div> : <></>}</>
}

Bookmark.propTypes = types.Bookmark