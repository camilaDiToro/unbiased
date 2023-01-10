import TopNewTabs from "./TopNewTabs";
import Comment from "./Comment";

export default function CommentList() {
  return (
    <>
      <div className="d-flex flex-column w-75 align-items-center justify-content-center align-self-center"
           id="comments">
        <h2 className="align-self-start my-2 text-white">
          Comentarios
        </h2>
        <div className="d-flex flex-column w-100 mb-4">
          <TopNewTabs></TopNewTabs>
          <div className="d-flex flex-column mt-4 mb-4">
            <div className="form-group w-100">
                  <textarea name="comment" className="form-control w-100 custom-comment-area text-white" rows="5"
                            id="comment-input"
                            placeholder="Comentario" />
            </div>
            <button className="btn btn-primary flex-grow-0 align-self-end" type="submit">
              Comentar
            </button>
          </div>
          <Comment></Comment>
        </div>
      </div>
    </>
  );
}