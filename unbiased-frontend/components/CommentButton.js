import { useAppContext } from "../context";
import Tooltip from "./Tooltip";
import Link from "next/link";
import {getResourcePath} from "../constants";
import types from "../types";

export default function CommentButton(props) {
  const {I18n} = useAppContext()

  return <>
    <div className=" m-1 h-50 max-h-40px d-flex justify-content-center align-items-center">
      <Link href={`/news/${props.id}?comment=true`}>
        <Tooltip position="bottom" text={I18n("tooltip.commentArticle")}>
          <img className="w-25px svg-btn" id="comment"
               src={getResourcePath("/img/comment.svg")}
               alt=""/>
        </Tooltip>
      </Link>
    </div>
  </>

}

CommentButton.propTypes = types.CommentButton
