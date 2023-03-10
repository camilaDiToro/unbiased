import Head from "next/head";
import CommentList from "../../components/CommentList";
import { useAppContext } from "../../context";
import PositivityIndicator from "../../components/PositivityIndicator";
import ProfilePic from "../../components/ProfilePic";
import NewsCategoryPills from "../../components/NewsCategoryPills";
import ProfileLink from "../../components/ProfileLink";
import Bookmark from "../../components/Bookmark";
import ReportFlag from "../../components/ReportFlag";
import {useTriggerEffect} from "../../utils";
import UpvoteButtons from "../../components/UpvoteButtons";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import DeleteButton from "../../components/DeleteButton";
import PinButton from "../../components/PinButton";
import usePagination from "../../pagination";
import {getResourcePath} from "../../constants";


export default function ShowNews() {
  const {I18n, loggedUser,  api}= useAppContext();
  const [articleEffectTrigger, articleTriggerEffect] = useTriggerEffect()
  const [commentsEffectTrigger, commentsTriggerEffect] = useTriggerEffect()
  const [article, setArticle] = useState(null)
  const [comments, setComments] = useState([])
  const router = useRouter()
  const [pagination, setPagination] = usePagination()
  const {comment, id} = router.query


  useEffect(() => {
    if (!id)
      return

    api.getArticle(id).then(r => {
      const {success, data, error} = r
      if (success)
        setArticle(data)
      else if (error?.response?.status === 404) {
        router.replace('/404')
      }
    })

  }, [id, articleEffectTrigger])

  useEffect(() => {
    if (!id)
      return
    const params = {order: router.query.order, id, filter:'NEWS_COMMENTS', page: router.query.page}

    api.getComments(params).then(r => {
      const {success, data, pagination} = r
      if (success) {
        setPagination(pagination)
        setComments(data)
      }
    })
  }, [commentsEffectTrigger, router.query])

  useEffect(() => {
    if (comment) {
      const section = document.querySelector( `#comment-${typeof comment === 'number' ? comment : 'section'}` );
      if (section) {
        section.scrollIntoView( { behavior: 'smooth', block: 'start' } );
        const query = router.query
        delete query['comment']
        router.replace({pathname: router.pathname,query},undefined, { shallow: true })
      }
    }

  }, [id, article])


  return(
    <>
      <Head>
        <title>unbiased - {article ? article.title : ''}</title>
        <meta name="description" content="Generated by create next app" />
      </Head>
      {id && article ? <div className="d-flex align-items-center justify-content-center w-100 py-4">
        <div className="h-auto w-75 d-flex flex-column ">
          <div className="d-flex align-items-center  ">
            <UpvoteButtons id={article.id} triggerEffect={articleTriggerEffect} upvotes={article.upvotes} rating={article.rating} ></UpvoteButtons>
            <h2 className="text-xl-center mx-auto max-w-75 m-3 text-white overflow-wrap">
              {article.title}
            </h2>
            <div className="upvote-div-home d-flex flex-column align-items-center m-3">
              <PositivityIndicator showNews {...article.stats}></PositivityIndicator>
            </div>
          </div>
          <hr/>
          {article.hasImage ? <img className="max-w-50 m-4 max-h-600px rounded mx-auto d-block img-thumbnail" src={article.image} alt=""/>
              : <></>}
          <div className="d-flex align-items-center justify-content-between">
            <div className="d-flex gap-1 align-items-center justify-content-between w-100">
              <h4 className="text-lg-left mb-0 text-white">
                {article.subtitle}
              </h4>
            </div>
            {loggedUser ? <div className="d-flex flex-row align-items-center gap-4px">
              <div className="ml-2 d-flex justify-content-center align-items-center">
                <Bookmark id={article.id} triggerEffect={articleTriggerEffect} saved={article.saved} ></Bookmark>
              </div>
              <div className="ml-2 d-flex justify-content-center align-items-center">
                <ReportFlag reported={article.reported} triggerEffect={articleTriggerEffect} id={article.id}></ReportFlag>
              </div>
              <div className="ml-2 d-flex justify-content-center align-items-center">
                {   loggedUser && loggedUser.id === article.creator.id
                    ? <DeleteButton triggerEffect={articleTriggerEffect} creatorId={article.creator?.id} id={parseInt(id)} ></DeleteButton> : <></>}
              </div>
              <div className="ml-2 d-flex justify-content-center align-items-center">
                {loggedUser && loggedUser.id === article.creator.id ? <PinButton triggerEffect={articleTriggerEffect} creatorId={article.creator?.id} id={parseInt(id)} pinned={article.pinned}></PinButton> : <></>}
              </div>
            </div>: <></>}
          </div>

          <p className="text-sm-left text-secondary mr-1">

            <img id="clock"
                 className="read-clock mx-1 mb-1"
                 src={getResourcePath("/img/clock-svgrepo-com.svg")} />
            {I18n("home.read", [article.readTime])}
          </p>

          <div className="w-fit">
            <div className="w-fit d-flex flex-row align-items-center p-2 gap-1">
              <div className="img-container-article">
                <ProfilePic {...article.creator } image={article.userImage}></ProfilePic>
              </div>

              <ProfileLink  bold {...article.creator}></ProfileLink>
            </div>
          </div>

          <div className="w-50 d-flex flex-wrap align-items-center gap-1 mt-3">
            <div className="text-sm-left font-weight-bold text-white w-100 d-flex gap-1">
              {article.categories?.length ? <div className="w-fit text-sm-left font-weight-bold text-white">
                {I18n("showNews.categories")}
              </div> : <></>}
              <NewsCategoryPills categories={article.categories}></NewsCategoryPills>
            </div>
          </div>

          <div className="d-flex w-100 min-vh-65 align-items-center flex-column">
            <div className="article-body p-5" dangerouslySetInnerHTML={{__html: article.body}}>
            </div>
            <CommentList focusId={comment} newsId={id}  pagination={pagination} triggerEffect={commentsTriggerEffect} comments={comments}></CommentList>
          </div>
        </div>
      </div> : <></>}
    </>
  )
}

