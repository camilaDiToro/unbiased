import { useAppContext } from "../context";
import ProfilePic from "./ProfilePic";
import FormattedDate from "./FormattedDate";
import ProfileLink from "./ProfileLink";
import ReportFlag from "./ReportFlag";
import DeleteButton from "./DeleteButton";
import UpvoteButtons from "./UpvoteButtons";
import PropTypes from "prop-types";
import types from "../types";

export default function Comment(props) {
  const { I18n, loggedUser } = useAppContext();
  return (
    <>
      <div className="mb-4 w-100 p-4 bg-black rounded-comment">
        <div className="d-flex flex-row gap-1 align-items-center p-1 mb-1">
          <div className="img-container-comment">
            <div className="frame-navbar img-container">
              <ProfilePic
                  {...props.creator}
              ></ProfilePic>
            </div>
          </div>
          <ProfileLink bold {...props.creator}></ProfileLink>
        </div>
        <span className="font-weight-light mt-1 mb-2">
          <FormattedDate datetime={props.datetime}></FormattedDate>
        </span>

        <div id={`comment-${props.id}`} className={`d-flex align-items-center w-auto gap-1 ${props.deleted ? 'font-italic' : ''}`}>
          <p className="comment-text">{ props.deleted ? I18n('showNews.deletedComment') : props.body}</p>
        </div>
        <div className="d-flex align-items-center justify-content-between float-sm-left gap-1">
          <UpvoteButtons comment
                         id={props.id}
            upvotes={props.upvotes}
            triggerEffect={props.triggerEffect}
            rating={props.rating}
          ></UpvoteButtons>
        </div>
        <div className="d-flex gap-1 align-items-center justify-content-between float-sm-right">
          <DeleteButton
            creatorId={props.creator.id}
            id={props.id}
            comment
            triggerEffect={props.triggerEffect}
          ></DeleteButton>

          <ReportFlag
            id={props.id}
            reported={props.reported}
            comment
            triggerEffect={props.triggerEffect}
          ></ReportFlag>
        </div>
      </div>
    </>
  );
}

Comment.propTypes = types.Comment
