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
import PropTypes from "prop-types";

export async function getServerSideProps(context) {
  return {
    props: {
      isJournalist: true,
      email: 'email@email.com',
      username: 'kevin',
      followers: 10,
      following: 5,
      description: 'this is my description',
      interactions: 98,
      upvoted: 0.6,
      positivity: "positive",
      mailOptions: [{identifier: "mailOption.follow", checked: true}, {identifier: "mailOption.comment", checked: true}, {identifier: "mailOption.folowingPublished", checked: false}, {identifier: "mailOption.positivityChanged", checked: false}]
    }, // will be passed to the page component as props
  }
}


export default function Profile(props) {
  const items = [
    { text: "Hola", route: "/" },
    { text: "Como", route: "/" },
    { text: "Va", route: "/" },
  ];
  const {I18n}= useAppContext();
  const news = [
    {
      title: "Title",
      subtitle: "Subtitle",
      body: "asjkbas jkas askj aksj asjk as",
      readTime: 3,
      saved: true,
      hasImage: false,
      creator: {
        name: "username",
        id: 4,
      },
      id: 5,
    },
    {
      title: "Title",
      subtitle: "Subtitle",
      body: "asjkbas jkas askj aksj asjk as",
      readTime: 3,
      saved: true,
      hasImage: false,
      creator: {
        name: "username",
        id: 4,
        hasImage: false
      },
      id: 5,
    },
    {
      title: "Title",
      subtitle: "Subtitle",
      body: "asjkbas jkas askj aksj asjk as",
      readTime: 3,
      saved: true,
      hasImage: false,
      creator: {
        name: "username",
        id: 4,
        hasImage: false
      },
      id: 5,
    },
  ];
  const selected = "Como";



  const RightSide = () => (<div
      className="d-flex flex-column w-30 justify-content-start pr-5">
    <div className="card right-card" id="right-card">
      <div className="profile">
        {/*<c:if test="${isMyProfile}">*/}

        <ModalTrigger modalId="profileModal">
          <span
                className="hover-hand pencil-edit badge-info badge-pill d-flex align-items-center justify-content-center"
                id="pencil_button">
                        <div className="position-relative img-container-profile mr-1 d-flex justify-content-center align-items-center">
                            <img className="position-relative object-fit-contain"
                                 src="/img/pencil-edit.png" alt="..."/>
                        </div>
            {I18n("profile.edit")}
                        </span>
        </ModalTrigger>

        <ProfilePic tier="gold"/>





      </div>
      {props.isJournalist ?         <PositivityIndicator interactions={props.interactions} positivity={props.positivity} upvoted={props.upvoted}></PositivityIndicator>
      : <></>}

      {/*<c:if test="${isMyProfile}">*/}
      <Tooltip text={I18n("tooltip.info")} className="info-profile-btn bg-transparent">
        <ModalTrigger modalId="infoModal">
          <button
                  className="bg-transparent border-0 btn-size"
                  style={{backgroundImage: 'url(/img/info-svgrepo-com.svg)'}}></button>
        </ModalTrigger>
      </Tooltip>

      {/*</c:if>*/}
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
            {/*<c:if test="${loggedUser != null && !isMyProfile}">*/}
              <FollowButton following={true}></FollowButton>
            {/*</c:if>*/}
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
            {/*<c:if test="${isJournalist}">*/}
              <div className="text-center font-weight-light m-1 overflow-wrap w-85">
                {props.description}
              </div>
            {/*</c:if>*/}
          </div>
        </div>


    </div>
    {props.isJournalist ? <div className="card right-card">

      <div className="card-body">
        {/*<c:forEach var="cat" items="${newsCategories}">*/}
        <ProgressBar progress={0.2} title="Tech"></ProgressBar>
        {/*</c:forEach>*/}

      </div>
    </div> : <></>}
    <Modal id="profileModal" title={I18n("profile.user.settings")}>
      <label >
        {I18n("profile.modal.changeUsername")}
      </label>
      <div className="input-group mb-3">
        <div className="input-group-prepend">
          <span className="input-group-text" id="basic-addon1">@</span>
        </div>
        <input type="text" className="form-control" id="username-input"
                    placeholder={I18n("profile.modal.changeUsername")} value={props.username}/>

      </div>

      <label >
        {I18n("profile.modal.changeProfilePicture")}
      </label>
      <div className="input-group mb-3">
        <div className="custom-file">
          <input id="file-input" type="file" accept="image/png, image/jpeg"
                      className="custom-file-input"/>
          <label id="file-input-label" className="custom-file-label"
                      htmlFor="inputGroupFile01"></label>

        </div>

      </div>


      {props.isJournalist ?
          <>
            <label>
              {I18n("profile.modal.changeDescription")}
            </label>
            <div className="input-group mb-3">
              <input type="text" className="form-control" id="description-input"
                     placeholder={I18n("profile.modal.changeDescription")} value={props.description}/>

            </div>
          </> : <></>}

      <div className="input-group mb-3">
        <label>
          {I18n("profile.modal.changeMailOptions")}
        </label>
        {props.mailOptions.map(op => <div key={op.identifier} className="form-check  w-100">

          <input className="mr-1" checked={op.checked} type="checkbox" value={I18n(op.identifier)}/>
          <label className="form-check-label" htmlFor="${option.interCode}">
            {I18n(op.identifier)}
          </label>
        </div>)}
        <div className="w-100">
          <p className="text-danger">error</p>
        </div>
      </div>

    </Modal>
  </div>)

    const LeftSide = () => (<div className="d-flex flex-column w-70 align-items-start">
      <div className="tab">
        <Tabs items={items} pill selected={selected}></Tabs>
      </div>
      <div className="tab">
        <div className="container-fluid">
          <div className="row row-cols-1">
            {news.map((n) => (
                <Article {...n} key={n.id} id={n.id}></Article>
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
      <Tabs items={items} selected={selected}></Tabs>
      <div className="d-flex flex-column">
        <div className="flex-grow-1 d-flex flex-row">
          <LeftSide></LeftSide>
          <RightSide></RightSide>
        </div>
      </div>
    </>
  );
}

Profile.propTypes = {
  isJournalist: PropTypes.bool.isRequired,
  email: PropTypes.string.isRequired,
  username: PropTypes.string,
  followers: PropTypes.bigint.isRequired,
  following: PropTypes.bigint.isRequired,
  description: PropTypes.string,
  interactions: PropTypes.bigint,
  upvoted: PropTypes.number,
  positivity: PropTypes.string,
  mailOptions: PropTypes.arrayOf(PropTypes.shape({
    identifier: PropTypes.string.isRequired,
    checked: PropTypes.bool.isRequired
  }))
}
