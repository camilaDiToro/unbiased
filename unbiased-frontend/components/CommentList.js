import TopNewTabs from "./TopNewTabs";
import Comment from "./Comment";
import { useAppContext } from "../context";
import Pagination from "./Pagination";
import Link from "next/link";

export default function CommentList(props) {
  const {I18n, loggedUser}= useAppContext();
  return (
    <>
      <div className="d-flex flex-column w-75 align-items-center justify-content-center align-self-center"
           id="comments">
        <h2 className="align-self-start my-2 text-white">
          {I18n("showNews.comments")}
        </h2>
        <div className="d-flex flex-column w-100 mb-4">
          { loggedUser ?
            <div className="flex-grow-1 d-flex flex-column mt-4 mb-4">
              <div className="form-group w-100">
                  <textarea name="comment" className="form-control w-100 custom-comment-area text-white" rows="5"
                            id="comment-input"
                            placeholder={I18n("showNews.comment")} />
              </div>
              <button className="btn btn-primary flex-grow-0 align-self-end" type="submit">
                {I18n("showNews.comment.submit")}
              </button>
            </div> :
            <h6 className="m-2 align-self-center">
              <Link href={`/login`} className="link text-underline">Ingresa</Link>
              o
              <Link href={`/register`} className="link text-underline">Registrate</Link>
              {I18n("showNews.commentLogged")}
            </h6>
          }

          <TopNewTabs></TopNewTabs>

          {props.comments.length == 0 ?
            <h6 className="m-2 align-self-center">{I18n("showNews.emptyCommentsLogged")}</h6> :
            <>
              {props.comments.map(c => <Comment triggerEffect={props.triggerEffect} key={c.id} {...c}></Comment>)}
            </>
          }
        </div>
        {props.comments.length ? <Pagination currentPage={2} lastPage={4}></Pagination> : <></>}

      </div>
    </>
  );
}