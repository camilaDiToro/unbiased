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

export async function getServerSideProps(context) {
  return {
    props: {
      isJournalist: true,
      email: 'email@email.com',
      news,
      id: parseInt(context.query.id),
      username: 'kevin',
      followers: 10,
      following: 5,
      tier: 'gold',
      description: 'this is my description',
      isLoggedUserFollowing: false,
      newsStatistics: [
        { title: "categories.tourism", progress: 0.2, i18n: true },
        { title:"categories.entertainment", progress: 0.2, i18n: true },
        { title: "categories.politics", progress: 0.2, i18n: true },
        { title: "categories.economics", progress: 0.2, i18n: true },
        { title: "categories.sports", progress: 0.2, i18n: true },
        {title: "categories.technology", progress: 0.2, i18n: true }
      ],
      stats: {interactions: 98,
        upvoted: 0.6,
        positivity: "positive"},
      mailOptions: ["mailOption.follow", "mailOption.comment"]

    }, // will be passed to the page component as props
  }
}


export default function Profile(props) {

  const {I18n, loggedUser}= useAppContext();
  const router = useRouter()
  const isMyProfile = loggedUser && loggedUser.id === props.id
  const [useNews, setNews] = useState(props.news)


  useEffect(() => {
    setNews(props.news)
  }, [router.query.order, router.query.cat])

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

        <ProfilePic tier={props.tier}/>

      </div>
      {props.isJournalist ?         <PositivityIndicator {...props.stats}></PositivityIndicator>
      : <></>}

      {isMyProfile ? <Tooltip text={I18n("tooltip.info")} className="info-profile-btn bg-transparent">
        <ModalTrigger  modalId="infoModal">
          <button
              className="bg-transparent border-0 btn-size"
              style={{backgroundImage: 'url(/img/info-svgrepo-com.svg)'}}></button>
        </ModalTrigger>
      </Tooltip> : <></>}
    <Modal id="infoModal" title={I18n("profile.modal.infoTitle")} >
      <h6>
        {I18n("profile.modal.infoAllowedMsg")}
      </h6>

      <div className="info-function d-flex flex-row mb-3">

        <div className="d-flex">
          1. {I18n("profile.modal.infoChangeUsername")}
        </div>

        <div className="d-flex info-enabled info-custom-box">
          {I18n("profile.modal.enabled")}
        </div>
      </div>

      <div className="info-function d-flex flex-row mb-3">

        <div className="d-flex">
          2. {I18n("profile.modal.infoChangeProfileimg")}
        </div>

        <div className="d-flex info-enabled info-custom-box">
          {I18n("profile.modal.enabled")}
        </div>
      </div>

      <div className="info-function d-flex flex-row">

        <div className="d-flex">
          3. {I18n("profile.modal.infoChangeAddDescription")}
        </div>
        {props.isJournalist ? <div className="d-flex info-enabled info-custom-box">
          {I18n("profile.modal.enabled")}
        </div> : <Tooltip className="d-flex info-disabled info-custom-box" position="bottom"
                          text={I18n("tooltip.infoDisabled")}>
          {I18n("profile.modal.disabled")}
        </Tooltip>}
      </div>
    </Modal>


      <img src="/img/front-page-profile.png" className="card-img-top" alt="..."/>

        <div className="card-body">
          <h4 className="mb-0 card-title text-center">
            {props.username}
          </h4>
          <div className="d-flex flex-row align-items-center justify-content-center m-2 gap-2">
            <span className="card-text text-muted d-block">{props.email}</span>
            {(loggedUser && !isMyProfile) ?  <FollowButton userId={props.id} following={loggedUser && props.isLoggedUserFollowing}></FollowButton>
             : <></>}
          </div>

          <div className="d-flex flex-row align-items-center justify-content-center">
            <div className="d-flex flex-row mr-5">
              <p className="font-weight-bold">{props.followers}</p>
              <p className="custom-follow-text">
                {I18n("profile.followers")}
              </p>
            </div>

            <div className="d-flex flex-row">
              <p className="font-weight-bold">{props.following}</p>
              <p className="custom-follow-text">
                {I18n("profile.following")}
              </p>
            </div>
          </div>

          <div className="d-flex justify-content-center align-items-center">
            {props.isJournalist ? <div className="text-center font-weight-light m-1 overflow-wrap w-85">
              {props.description}
            </div> : <></>}
          </div>
        </div>


    </div>
    {props.isJournalist ? <div className="card right-card">

      <div className="card-body">
        {props.newsStatistics.map(stats => <ProgressBar key={stats.title} {...stats}></ProgressBar>)}

      </div>
    </div> : <></>}
    <Modal id="profileModal" title={I18n("profile.user.settings")}>
      <EditProfileForm {...props}></EditProfileForm>
    </Modal>
  </div>)

    const LeftSide = () => (<div className="d-flex flex-column w-70 align-items-start">
      <div className="tab">
        <TopNewTabs></TopNewTabs>
      </div>
      <div className="tab">
        <div className="container-fluid">
          <div className="row row-cols-1">
            {useNews.map((n) => (
                <Article profileArticle {...n} key={n.id} id={n.id}></Article>
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
      <ProfileTabs userId={props.id}></ProfileTabs>
      <div className="d-flex flex-column">
        <div className="flex-grow-1 d-flex flex-row">
          <LeftSide></LeftSide>
          <RightSide></RightSide>
        </div>
      </div>
    </>
  );
}

Profile.propTypes = types.Profile