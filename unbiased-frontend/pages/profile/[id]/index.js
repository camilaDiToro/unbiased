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
import TopNewTabs from "../../../components/TopNewTabs";
import ProfileTabs from "../../../components/ProfileTabs";
import {useEffect, useState} from "react";
import {useRouter} from "next/router";
import EditProfileForm from "../../../components/EditProfileForm";
import UserPrivileges from "../../../components/UserPrivileges";
import {useTriggerEffect} from "../../../utils"
import Pagination from "../../../components/Pagination";
import usePagination from "../../../pagination";
import {getResourcePath} from "../../../constants";





export default function Profile() {

  const {I18n, loggedUser, api}= useAppContext();
  const router = useRouter()
  const {id} = router.query
  const [profileEffectTrigger, profileTriggerEffect] = useTriggerEffect()
  const [newsEffectTrigger, newsTriggerEffect] = useTriggerEffect()

  const [useNews, setNews] = useState([])
  const [pagination, setPagination] = usePagination()
  const [profileInfo, setProfileInfo] = useState(undefined)
  const [pinned, setPinned] = useState(undefined)

  const queryParamMap = {
    MY_POSTS: 'PUBLISHED_BY',
    SAVED: 'SAVED_BY',
    UPVOTED: 'LIKED_BY',
    DOWNVOTED: 'DISLIKED_BY'
  }

  const getQueryParams = () => {
    const params = {order: router.query.order, page: router.query.page, id}

    params.filter = queryParamMap[router.query.cat] || queryParamMap.MY_POSTS

    return params
  }

  useEffect(() => {
    api.getArticles( getQueryParams()).then(res => {
      const {success, pagination, data} = res
      if (success) {
        setPagination(pagination)
        setNews(data)
      }
    })
  }, [router.query, newsEffectTrigger])

  useEffect(() => {
    if (!id)
      return
    api.getArticles({filter: 'PINNED_BY', id: id}).then(res => {
      const {data, success} = res

      if (success) {
        setPinned(data[0])
      }

    })

    api.getUsers({filter: 'FOLLOWING', id: loggedUser?.id}).then(res => {
      const {success, data} = res
      if (success) {
        const isLoggedUserFollowing = data.map(u => u.id).includes(id)
        setProfileInfo(i => ({...i, isLoggedUserFollowing}))
      }
    })
  }, [newsEffectTrigger, id])



  useEffect(() => {
    if (!id)
      return
    api.getUser(id).then(r => {
      const {success, data, error} = r

      if (success) {
        setProfileInfo(data)
      }
      else if (error?.response?.status === 404) {
        router.replace('/404')
      }
    })

  }, [profileEffectTrigger, id])


  let submitHandlerArray = []

  const RightSide = () => {
    if (!profileInfo)
      return <></>

    return <div
        className="d-flex flex-column w-30 justify-content-start pr-5">

      <div className="card right-card" id="right-card">

        <div className="profile">
          {loggedUser && loggedUser.id == id? <ModalTrigger modalId="profileModal">
          <span

              className="hover-hand pencil-edit badge-info badge-pill d-flex align-items-center justify-content-center"
              id="pencil_button">
                        <div className="position-relative img-container-profile mr-1 d-flex justify-content-center align-items-center">
                            <img className="position-relative object-fit-contain"
                                 src={getResourcePath("/img/pencil-edit.png")} alt="..."/>
                        </div>
            {I18n("profile.edit")}
                        </span>
          </ModalTrigger>: <></>}

          <ProfilePic image={profileInfo.image} hasImage={profileInfo.hasImage} tier={profileInfo.tier}/>
        </div>
        {profileInfo.hasPositivity ? <PositivityIndicator {...profileInfo.stats}></PositivityIndicator>
            : <></>}

        {loggedUser && loggedUser.id == id?          <ModalTrigger  modalId="infoModal">
            <Tooltip text={I18n("tooltip.info")} className="info-profile-btn bg-transparent">
            <button
                className="bg-transparent border-0 btn-size"
                style={{backgroundImage: `url(${getResourcePath("/img/info-svgrepo-com.svg")})`}}></button>
        </Tooltip>          </ModalTrigger>
          : <></>}
        <Modal id="infoModal" title={I18n("profile.modal.infoTitle")} >
          <UserPrivileges isJournalist={profileInfo.isJournalist}></UserPrivileges>
        </Modal>

        <img src={getResourcePath("/img/front-page-profile.png")} className="card-img-top" alt="..."/>

        <div className="card-body">
          <h4 className="mb-0 card-title text-center">
            {profileInfo.username}
          </h4>
          <div className="d-flex flex-row align-items-center justify-content-center m-2 gap-2">
            <span className="card-text text-muted d-block">{profileInfo.email}</span>
            {(loggedUser && !(loggedUser && loggedUser.id == id)) ?  <FollowButton userId={profileInfo.id} following={loggedUser && profileInfo.isLoggedUserFollowing}></FollowButton>
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
    </div>
  }

    const LeftSide = () => (<div className="d-flex flex-column w-70 align-items-start">
      <div className="tab">
        <TopNewTabs></TopNewTabs>
      </div>
      <div className="tab">
        <div className="container-fluid">
          <div className="row row-cols-1">
            {pinned ? <Article pinned triggerEffect={newsTriggerEffect} profileArticle {...pinned} key={pinned.id} id={pinned.id}></Article> : <></>}

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
      </Head>
      <ProfileTabs userId={parseInt(id)}></ProfileTabs>
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

