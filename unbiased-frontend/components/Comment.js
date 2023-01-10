import { useAppContext } from "../context";
import Tooltip from "./Tooltip";


export default function Comment(props) {
    const {I18n, loggedUser}= useAppContext();

    return(
      <>
        <div className="mb-4 w-100 p-4 bg-black rounded-comment">
          <div className="d-flex flex-row gap-1 align-items-center p-1 mb-1">
            <div className="img-container-comment">
              <div className="frame-navbar">
                <img className="rounded-circle object-fit-cover mr-1"
                     src="/img/profile-image.png" />
              </div>
            </div>
            <h5 className="mb-0 link-text">Username</h5>
          </div>
          <span className="font-weight-light mt-1 mb-2">Hace 1 mes</span>

          <div className="d-flex align-items-center w-auto gap-1">
            <p className="comment-text">Mi comentario</p>
          </div>
          <div className="d-flex align-items-center justify-content-between float-sm-left gap-1">
            <div className="d-flex flex-row align-items-center gap-1">
              <img id="upvote" url="" className="svg-btn hover-hand" src={`/img/upvote.svg`} />
              <div id="rating" className="">5</div>
              <img id="downvote" url="" className="svg-btn hover-hand" src={`/img/downvote.svg`} />
            </div>
          </div>
          <div className="d-flex gap-1 align-items-center justify-content-between float-sm-right">
            <div className="svg-btn hover-hand h-fit">
              {props.profileArticle ? <button data-toggle="modal" data-target={`#binModal${props.id}`} className="btn bin-modal" id="bin_button">
                <Tooltip text={I18n("tooltip.deleteNews")} position="bottom">
                  <img src="/img/bin-svgrepo-com.svg" alt="..."
                       className="icon-comment svg-btn svg-bookmark bookmark"
                  />
                </Tooltip>
              </button> : <></>}
              <img src="/img/bin-svgrepo-com.svg" alt="..."
                   className="icon-comment svg-btn svg-bookmark bookmark"
              />
              <img id="save" className="icon-comment svg-btn svg-bookmark bookmark"
                   src={`/img/flag.svg`} />
            </div>
          </div>
        </div>
      </>
    );
}