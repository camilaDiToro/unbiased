import { useAppContext } from "../context";
import ProfilePic from "./ProfilePic";
import FormattedDate from "./FormattedDate";
import ProfileLink from "./ProfileLink";
import ReportFlag from "./ReportFlag";
import DeleteButton from "./DeleteButton";


export default function Comment(props) {
    const {I18n, loggedUser}= useAppContext();
    return(
      <>
        <div className="mb-4 w-100 p-4 bg-black rounded-comment">
          <div className="d-flex flex-row gap-1 align-items-center p-1 mb-1">
            <div className="img-container-comment">
              <div className="frame-navbar">
                <ProfilePic id={props.creator.id} hasImage={props.creator.hasImage}></ProfilePic>
              </div>
            </div>
            <ProfileLink {...props.creator}></ProfileLink>
          </div>
          <span className="font-weight-light mt-1 mb-2"><FormattedDate datetime={props.datetime}></FormattedDate></span>

          <div className="d-flex align-items-center w-auto gap-1">
            <p className="comment-text">{props.body}</p>
          </div>
          <div className="d-flex align-items-center justify-content-between float-sm-left gap-1">
            <div className="d-flex flex-row align-items-center gap-1">
              <img id="upvote"  className="svg-btn hover-hand" src={`/img/upvote.svg`} />
              <div id="rating" className="">5</div>
              <img id="downvote"  className="svg-btn hover-hand" src={`/img/downvote.svg`} />
            </div>
          </div>
          <div className="d-flex gap-1 align-items-center justify-content-between float-sm-right">

            <DeleteButton creatorId={props.creator.id} id={props.id} comment></DeleteButton>

            <ReportFlag id={props.id} reported={props.reported} comment></ReportFlag>
          </div>
        </div>
      </>
    );
}