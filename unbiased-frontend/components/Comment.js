export default function Comment(props) {
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
          <div className="d-flex gap-1 align-items-center justify-content-between">
            <div className="svg-btn hover-hand h-fit">
              <img id="upvote" url="" className="svg-btn hover-hand" src={`/img/upvote.svg`} />
              <div id="rating" className="">5</div>
              <img id="downvote" url="" className="svg-btn hover-hand" src={`/img/downvote.svg`} />
            </div>
          </div>
          <div className="d-flex align-items-center justify-content-between float-sm-right gap-1">
            <div className="d-flex flex-row align-items-center gap-1">
              <img src="/img/bin-svgrepo-com.svg"
                   className="icon-comment svg-bookmark" />
              <img id="save" className="icon-comment svg-btn svg-bookmark bookmark"
                   src={`/img/flag.svg`} />
            </div>
          </div>
        </div>
      </>
    );
}