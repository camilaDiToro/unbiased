import Head from "next/head";
import CommentList from "../../../components/CommentList";
import { useAppContext } from "../../../context";
import types from "../../../types";
import PositivityIndicator from "../../../components/PositivityIndicator";
import FormattedDate from "../../../components/FormattedDate";
import ProfilePic from "../../../components/ProfilePic";
import NewsCategoryPills from "../../../components/NewsCategoryPills";
import ProfileLink from "../../../components/ProfileLink";
import Bookmark from "../../../components/Bookmark";
import ReportFlag from "../../../components/ReportFlag";
import {useLoggedParamsFiller, useTriggerEffect} from "../../../utils";
import UpvoteButtons from "../../../components/UpvoteButtons";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import DeleteButton from "../../../components/DeleteButton";
import PinButton from "../../../components/PinButton";
import {commentsMapper, newsMapper} from "../../../mappers";
import axios from "axios";
import baseURL from "../../back";
import usePagination from "../../../pagination";

export async function getServerSideProps(context) {
  const id = context.query.id
  const newsPromise = axios.get(`${baseURL}news/${id}`).then((res) => newsMapper(res.data))
  const params = {order: context.query.order, newsId: id, page: context.query.page}
  const commentsPromise = axios.get(`${baseURL}comments`, {params}).then(res => (res.data || []).map(commentsMapper))

  const result = await Promise.all([newsPromise, commentsPromise])
  const props = {id, ...result[0], comments: result[1]}
  if (context.query.comment)
    props.comment = context.query.comment
  return {
    props
  }
}

export default function ShowNews(props) {
  const {I18n, loggedUser, axios}= useAppContext();
  const [articleEffectTrigger, articleTriggerEffect] = useTriggerEffect()
  const [commentsEffectTrigger, commentsTriggerEffect] = useTriggerEffect()
  const [article, setArticle] = useState(props)
  const {fillNewsLoggedParams, fillCommentsLoggedParams} = useLoggedParamsFiller()
  const [comments, setComments] = useState(props.comments)
  const router = useRouter()
  const [pagination, setPagination] = usePagination()
  const isMyArticle = loggedUser && loggedUser.id === props.creator.id

  useEffect(() => {
    axios.get(`news/${props.id}`).then(res => {
      const mappedNews = newsMapper(res.data)
      fillNewsLoggedParams([mappedNews]).then(n => setArticle(n[0]))
    })

  }, [])

  useEffect(() => {
    axios.get(`news/${props.id}`).then(res => {
      const mappedNews = newsMapper(res.data)
      fillNewsLoggedParams([mappedNews]).then(n => {
        setArticle(n[0])
        console.log(n[0])
      })
    })

  }, [articleEffectTrigger])

  useEffect(() => {
    const params = {order: router.query.order, newsId: props.id, page: router.query.page}
    axios.get(`comments`, {params}).then(res => {
      setPagination(res)
      const mappedComments = (res.data || []).map(commentsMapper)
      fillCommentsLoggedParams(mappedComments).then(n => {
        setComments(n)
      })
    })
  }, [commentsEffectTrigger, router.query])

  useEffect(() => {
    if (props.comment) {
      const section = document.querySelector( `#comment-${props.comment}` );
      if (section) {
        section.scrollIntoView( { behavior: 'smooth', block: 'start' } );
        const query = router.query
        delete query['comment']
        router.replace({pathname: router.pathname,query},undefined, { shallow: true })
      }
    }

  }, [])

  return(
    <>
      <Head>
        <title>unbiased - {article.title}</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/img/unbiased-logo-circle.png" />
      </Head>
      <div className="d-flex align-items-center justify-content-center w-100 py-4">
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
          {article.hasImage ? <img className="w-50 m-4 max-h-300px rounded mx-auto d-block img-thumbnail" src={article.image} alt=""/>
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
                {isMyArticle ? <DeleteButton creatorId={props.creator.id} id={props.id} ></DeleteButton> : <></>}
              </div>
              <div className="ml-2 d-flex justify-content-center align-items-center">
                {isMyArticle ? <PinButton creatorId={props.creator.id} id={props.id} pinned={props.pinned}></PinButton> : <></>}
              </div>
            </div>: <></>}
          </div>

          <p className="text-sm-left text-secondary mr-1">

            <FormattedDate datetime={article.datetime}></FormattedDate>{" • "}
            <img id="clock"
              className="read-clock mx-1 mb-1"
              src={"/img/clock-svgrepo-com.svg"} />
            {I18n("home.read", [article.readTime])}
          </p>

          <div className="w-fit">
            <div className="w-fit d-flex flex-row align-items-center p-2 gap-1">
              <div className="img-container-article">
                <ProfilePic {...article.creator}></ProfilePic>
              </div>

              <ProfileLink  bold {...article.creator}></ProfileLink>
            </div>
          </div>

          <div className="w-50 d-flex flex-wrap align-items-center gap-1 mt-3">
            <div className="text-sm-left font-weight-bold text-white w-100 d-flex gap-1">
              {article.categories.length ? <div className="w-fit text-sm-left font-weight-bold text-white">
                {I18n("showNews.categories")}
              </div> : <></>}
              <NewsCategoryPills categories={article.categories}></NewsCategoryPills>
            </div>
          </div>

          <div className="d-flex w-100 min-vh-65 align-items-center flex-column">
            <div className="article-body p-5" dangerouslySetInnerHTML={{__html: article.body}}>
            </div>
            <CommentList focusId={props.comment} newsId={props.id}  pagination={pagination} triggerEffect={commentsTriggerEffect} comments={comments}></CommentList>
        </div>
      </div>
      </div>
    </>
  )
}

ShowNews.propTypes = types.ShowNews