import Head from "next/head";
import CommentList from "../../../components/CommentList";

export default function ShowNews(props) {

  return(
    <>
      <Head>
        <title>unbiased - Home</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/img/unbiased-logo-circle.png" />
      </Head>
      <div className="d-flex align-items-center justify-content-center w-100 py-4">
        <div className="h-auto w-75 d-flex flex-column ">
          <div className="d-flex align-items-center  ">
            <div className="upvote-div-home d-flex flex-column align-items-center">
              <img id="upvote"
                   className="svg-btn hover-hand"
                   src={`/img/upvote.svg`}/>
              <div id="rating" className="${rating.toString()}">
                123
              </div>
              <img id="downvote"
                   className="svg-btn hover-hand"
                   src={`/img/downvote.svg`}/>
            </div>
            <h2 className="text-xl-center mx-auto max-w-75 m-3 text-white overflow-wrap">
              TITLE
            </h2>
            <div className="upvote-div-home d-flex flex-column align-items-center m-3">
              <img id="reaction"
                   className="svg-btn hover-hand"
                   src={`/img/looking-positivity.svg`}/>
            </div>
          </div>
          <hr/>
          <img className="w-50 m-4 rounded mx-auto d-block img-thumbnail" src="/img/front-page-profile.png" alt=""/>

          <div className="d-flex align-items-center justify-content-between">
            <div className="d-flex gap-1 align-items-center justify-content-between w-100">
              <h4 className="text-lg-left mb-0 text-white">
                SUBTITTLE
              </h4>
            </div>
            <div className="d-flex flex-row align-items-center gap-4px">
              <div className="ml-2 d-flex justify-content-center align-items-center">
                <img id="save"
                     className="icon-index svg-btn svg-bookmark bookmark"
                     src={`/img/bookmark.svg`}/>
              </div>
              <div className="ml-2 d-flex justify-content-center align-items-center">
                <img id="save"
                     className="icon-index svg-btn svg-bookmark bookmark"
                     src={`/img/flag.svg`}/>
              </div>
            </div>
          </div>

          <p className="text-sm-left text-secondary">
            Jueves 5 de enero
            <img id="clock"
              className="read-clock"
              src={"/img/clock-svgrepo-com.svg"} />
            1 min para leer
          </p>

          <div className="w-fit">
            <div className="w-fit d-flex flex-row align-items-center p-2 gap-1">
              <div className="img-container-article">
                <img
                  className="rounded-circle object-fit-cover mr-1"
                  src="/img/profile-image.png" />
              </div>
              <b>Username</b>
            </div>
          </div>

          <div className="w-50 d-flex flex-wrap align-items-center gap-1 mt-3">
            <div className="text-sm-left font-weight-bold text-white">
              <h5 className="text-white">
                Categorias
              </h5>
            </div>
          </div>

          <div className="d-flex w-100 min-vh-65 align-items-center flex-column">
            <div className="article-body">
              TEXTO
            </div>
            <CommentList></CommentList>
        </div>
      </div>
      </div>
    </>
  )
}