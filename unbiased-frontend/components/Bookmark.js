import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";
import {getResourcePath} from "../constants";
import {useRouter} from "next/router";
import Link from "next/link";

export default function Bookmark(props) {
    const {loggedUser, I18n,  api} = useAppContext()
    const router = useRouter()

    const onSave = async (e) => {
        if(props.saved) {
            await api.deleteBookmark(props.id)
        } else {
            await api.addBookmark(props.id)
        }
        props.triggerEffect()
    }
    return <>
        {loggedUser ? <div className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
            <Tooltip onClickHandler={onSave} position="bottom" text={props.saved ? I18n("tooltip.articleUnsave") : I18n("tooltip.articleSave")}>
                <img className="w-25px svg-btn" id="bookmark"
                     src={getResourcePath(`/img/bookmark${props.saved ? '-clicked' : ''}.svg`)}
                     alt=""
                     aria-label="bookmark"
                />
            </Tooltip>
        </div> : <Link href="/login" className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
            <Tooltip position="bottom" text={props.saved ? I18n("tooltip.articleUnsave") : I18n("tooltip.articleSave")}>
                <img className="w-25px svg-btn" id="bookmark"
                     src={getResourcePath('/img/bookmark.svg')}
                     alt=""
                     aria-label="bookmark"
                />
            </Tooltip>
        </Link>
        }</>
}

Bookmark.propTypes = types.Bookmark