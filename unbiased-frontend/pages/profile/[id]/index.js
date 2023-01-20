import Head from "next/head";
import Tabs from "../../../components/Tabs";
import { useAppContext } from "../../../context";
import Article from "../../../components/Article";
import PositivityIndicator from "../../../components/PositivityIndicator";
import Modal from "../../../components/Modal";
import FollowButton from "../../../components/FollowButton";
import ProgressBar from "../../../components/ProgressBar";
import Tooltip from "../../../components/Tooltip";
import ModalTrigger from "../../../components/ModalTrigger";
import ProfilePic from "../../../components/ProfilePic";
import types from "../../../types";
import TopNewTabs from "../../../components/TopNewTabs";
import ProfileTabs from "../../../components/ProfileTabs";
import {users, news} from "../../../hardcoded"
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import EditProfileForm from "../../../components/EditProfileForm";
import UserPrivileges from "../../../components/UserPrivileges";
import {useLoggedParamsFiller, useTriggerEffect, useURLWithParams} from "../../../utils"
import Pagination from "../../../components/Pagination";
import {newsMapper, userMapper} from "../../../mappers";
import usePagination from "../../../pagination";
import baseURL from "../../back";
import axios from "axios";

export async function getServerSideProps(context) {
  const id = parseInt(context.query.id)
  const relativePath = new URL(`users/${id}`, baseURL)
  const props = {}
  const res = await axios.get(relativePath)
  const data = res.data
  if (data && data.newsStats) {
    const newsStatsRes = await axios.get(data.newsStats)
    data.newsStats = newsStatsRes.data
    props.userInfo = userMapper(data)
  } else {
    props.userInfo = res.data ? userMapper(res.data) : {}
  }
  props.news = news
  return {
    props: {...props, id}
  }
  // return {
  //   props: {
  //     isJournalist: true,
  //     email: 'email@email.com',
  //     news,
  //     id: parseInt(context.query.id),
  //     username: 'kevin',
  //     followers: 10,
  //     following: 5,
  //     tier: 'gold',
  //     description: 'this is my description',
  //     isLoggedUserFollowing: false,
  //     newsStatistics: [
  //       { title: "categories.tourism", progress: 0.2, i18n: true },
  //       { title:"categories.entertainment", progress: 0.2, i18n: true },
  //       { title: "categories.politics", progress: 0.2, i18n: true },
  //       { title: "categories.economics", progress: 0.2, i18n: true },
  //       { title: "categories.sports", progress: 0.2, i18n: true },
  //       {title: "categories.technology", progress: 0.2, i18n: true }
  //     ],
  //     stats: {interactions: 98,
  //       upvoted: 0.6,
  //       positivity: "positive"},
  //     mailOptions: ["mailOption.follow", "mailOption.comment"]
  //
  //   }, // will be passed to the page component as props
  // }
}




export default function Profile(props) {

  const {I18n, loggedUser, axios}= useAppContext();
  const router = useRouter()
  const isMyProfile = loggedUser && loggedUser.id === props.id
  const [profileEffectTrigger, profileTriggerEffect] = useTriggerEffect()
  const [newsEffectTrigger, newsTriggerEffect] = useTriggerEffect()

  const [useNews, setNews] = useState(props.news)
  const [pagination, setPagination] = usePagination()
  const [profileInfo, setProfileInfo] = useState(props.userInfo)
  const fillNewsLoggedParams = useLoggedParamsFiller()

  const queryParamMap = {
    MY_POSTS: 'publishedBy',
    SAVED: 'savedBy',
    UPVOTED: 'likedBy',
    DOWNVOTED: 'dislikedBy'
  }

  const getQueryParams = () => {
    const params = {order: router.query.order, page: router.query.page}

    if (loggedUser) {
      params[queryParamMap[router.query.cat] || queryParamMap.MY_POSTS] = loggedUser.id
    }
    return {params}
  }

  useEffect(() => {
    axios.get('news', getQueryParams()).then(res => {
      console.log(res)
      setPagination(res)
      const mappedNews = (res.data || []).map(newsMapper)
      fillNewsLoggedParams(mappedNews).then(n => setNews(n))
    })
  }, [router.query, newsEffectTrigger])

  useEffect(() => {
    // setParams(urlBase, ['type'])
    const relativePath = `users/${props.id}`
    axios.get(relativePath).then(res => {
      const data = res.data
      if (data && data.newsStats) {
        axios.get(data.newsStats).then(newsStats =>  {
          data.newsStats = newsStats.data
          setProfileInfo(userMapper(res.data))
        })
      } else {
        setProfileInfo(res.data ? userMapper(res.data) : {})
      }
    })

  }, [profileEffectTrigger])


  let submitHandlerArray = []

  const RightSide = () => (<div
      className="d-flex flex-column w-30 justify-content-start pr-5">
    <div className="card right-card" id="right-card">
      <div className="profile">
        {isMyProfile ? <ModalTrigger modalId="profileModal">
          <span
              className="hover-hand pencil-edit badge-info badge-pill d-flex align-items-center justify-content-center"
              id="pencil_button">
                        <div className="position-relative img-container-profile mr-1 d-flex justify-content-center align-items-center">
                            <img className="position-relative object-fit-contain"
                                 src="/img/pencil-edit.png" alt="..."/>
                        </div>
            {I18n("profile.edit")}
                        </span>
        </ModalTrigger>: <></>}

        <ProfilePic image={profileInfo.image} hasImage={profileInfo.hasImage} tier={profileInfo.tier}/>

      </div>
      {profileInfo.hasPositivity ? <PositivityIndicator {...profileInfo.stats}></PositivityIndicator>
      : <></>}

      {isMyProfile ? <Tooltip text={I18n("tooltip.info")} className="info-profile-btn bg-transparent">
        <ModalTrigger  modalId="infoModal">
          <button
              className="bg-transparent border-0 btn-size"
              style={{backgroundImage: 'url(/img/info-svgrepo-com.svg)'}}></button>
        </ModalTrigger>
      </Tooltip> : <></>}
    <Modal id="infoModal" title={I18n("profile.modal.infoTitle")} >
     <UserPrivileges isJournalist={profileInfo.isJournalist}></UserPrivileges>
    </Modal>

      <img src="/img/front-page-profile.png" className="card-img-top" alt="..."/>

        <div className="card-body">
          <h4 className="mb-0 card-title text-center">
            {profileInfo.username}
          </h4>
          <div className="d-flex flex-row align-items-center justify-content-center m-2 gap-2">
            <span className="card-text text-muted d-block">{profileInfo.email}</span>
            {(loggedUser && !isMyProfile) ?  <FollowButton userId={profileInfo.id} following={loggedUser && profileInfo.isLoggedUserFollowing}></FollowButton>
             : <></>}
          </div>

          <div className="d-flex flex-row align-items-center justify-content-center">
            <div className="d-flex flex-row mr-5">
              <p className="font-weight-bold">{profileInfo.followers}</p>
              <p className="custom-follow-text">
                {I18n("profile.followers")}
              </p>
            </div>

            <div className="d-flex flex-row">
              <p className="font-weight-bold">{profileInfo.following}</p>
              <p className="custom-follow-text">
                {I18n("profile.following")}
              </p>
            </div>
          </div>

          <div className="d-flex justify-content-center align-items-center">
            {profileInfo.isJournalist ? <div className="text-center font-weight-light m-1 overflow-wrap w-85">
              {profileInfo.description}
            </div> : <></>}
          </div>
        </div>


    </div>
    {profileInfo.isJournalist ? <div className="card right-card">

      <div className="card-body">
        {profileInfo.newsStatistics.map(stats => <ProgressBar key={stats.title} {...stats}></ProgressBar>)}

      </div>
    </div> : <></>}
    <Modal  onClickHandlerArray={submitHandlerArray} id="profileModal" title={I18n("profile.user.settings")}>
      <EditProfileForm triggerEffect={profileTriggerEffect} handlerArray={submitHandlerArray} {...profileInfo}></EditProfileForm>
    </Modal>
  </div>)

    const LeftSide = () => (<div className="d-flex flex-column w-70 align-items-start">
      <div className="tab">
        <TopNewTabs></TopNewTabs>
      </div>
      <div className="tab">
        <div className="container-fluid">
          <div className="row row-cols-1">
            {/*{JSON.stringify(profileInfo)}*/}

            {useNews.map((n) => (
                <Article triggerEffect={newsTriggerEffect} profileArticle {...n} key={n.id} id={n.id}></Article>
            ))}
          </div>
        </div>
      </div>
    </div>)

  return (
    <>
      <Head>
        <title>unbiased - Profile</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/img/unbiased-logo-circle.png" />
      </Head>
      <ProfileTabs userId={profileInfo.id}></ProfileTabs>
      <div className="d-flex flex-column h-100">
        <div className="flex-grow-1 d-flex flex-row">
          <LeftSide></LeftSide>
          <RightSide></RightSide>
        </div>
        <Pagination {...pagination}></Pagination>

      </div>

    </>
  );
}

Profile.propTypes = types.Profile
