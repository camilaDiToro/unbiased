import Head from "next/head";
import Tabs from "../../../components/Tabs";
import { useAppContext } from "../../../context";
import ProfileArticle from "../../../components/ProfileArticle";
import PositivityIndicator from "../../../components/PositivityIndicator";
import Modal from "../../../components/Modal";
import FollowButton from "../../../components/FollowButton";
import ProgressBar from "../../../components/ProgressBar";


export default function Profile(props) {
  const items = [
    { text: "Hola", route: "/" },
    { text: "Como", route: "/" },
    { text: "Va", route: "/" },
  ];
  const ctx = useAppContext();
  const news = [{ id: 1 }, { id: 2 }];
  const selected = "Como";
  const topCreators = [{ name: "Juan" }, { name: "Lucio" }];
  const pinnedNews = undefined;

  const RightSide = () => (<div
      className="d-flex flex-column w-30 justify-content-start pr-5">
    <div className="card right-card" id="right-card">
      <div className="profile">
        {/*<c:if test="${isMyProfile}">*/}

        <span data-toggle="modal" data-target="#profileModal"
              className="hover-hand pencil-edit badge-info badge-pill d-flex align-items-center justify-content-center"
              id="pencil_button">
                        <div className="position-relative img-container-profile mr-1">
                            <img className="position-relative object-fit-contain"
                                 src="/img/pencil-edit.png" alt="..."/>
                        </div>
          {"profile.edit"}
                        </span>

        {/*</c:if>*/}
        {/*<c:if test="${profileUser.hasImage()}">*/}

        {/*  <c:if test="${profileFollowers >= 0 && profileFollowers < 1}">*/}
        {/*    <img id="default-frame-color" src="<c:url value="/profile/${profileUser.getUserId()}/image"/>"*/}
        {/*         className="rounded-circle object-fit-cover img-div" width="80">*/}
        {/*  </c:if>*/}
        {/*  <c:if test="${profileFollowers >=1 && profileFollowers < 2}">*/}
        {/*    <img id="gold-frame-color" src="<c:url value="/profile/${profileUser.getUserId()}/image"/>"*/}
        {/*         className="rounded-circle object-fit-cover img-div" width="80">*/}
        {/*  </c:if>*/}
        {/*  <c:if test="${profileFollowers >=2}">*/}
        {/*    <img id="platinum-frame-color" src="<c:url value="/profile/${profileUser.getUserId()}/image"/>"*/}
        {/*         className="rounded-circle object-fit-cover img-div" width="80">*/}
        {/*  </c:if>*/}
        {/*/!*</c:if>*!/*/}
        {/*<c:if test="${!profileUser.hasImage()}">*/}
        {/*  <img src="<c:url value="/resources/images/profile-image.png"/>"*/}
        {/*       className="rounded-circle object-fit-cover img-div" width="80">*/}
        {/*</c:if>*/}
      </div>
      {/*<c:if test="${profileUser.hasPositivityStats()}">*/}
        <PositivityIndicator></PositivityIndicator>

      {/*</c:if>*/}

      {/*<c:if test="${isMyProfile}">*/}
                <span data-toggle="tooltip" data-placement="top" title="tooltip.info"
                      className="info-profile-btn bg-transparent">
                <button data-toggle="modal" data-target="#infoModal"
                        className="info-profile-btn bg-transparent border-0"
                        style={{backgroundImage: 'url(/img/info-svgrepo-com.svg)'}}></button>
            </span>
      {/*</c:if>*/}
    <Modal title={"profile.modal.infoTitle"} body={"profile.modal.infoAllowedMsg"}></Modal>


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
                {"profile.followers"}
              </p>
            </div>

            <div className="d-flex flex-row">
              <p className="font-weight-bold">{props.following}</p>
              <p className="custom-follow-text">
                {"profile.following"}
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

    {/*<c:if test="${isJournalist}">*/}

    </div>
    <div className="card right-card">

      <div className="card-body">
        {/*<c:forEach var="cat" items="${newsCategories}">*/}
        <ProgressBar progress={0.2} title="Tech"></ProgressBar>
        {/*</c:forEach>*/}

      </div>
    </div>
    <Modal id="profileModal" title="profile.user.settings"></Modal>
  </div>)

    const LeftSide = () => (<div className="d-flex flex-column w-70 align-items-start">
      <div className="tab">
        <Tabs items={items} pill selected={selected}></Tabs>
      </div>
      <div className="tab">
        <div className="container-fluid">
          <div className="row row-cols-1">
            {news.map((n) => (
                <ProfileArticle key={n.id} id={n.id}></ProfileArticle>
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
      <div className="d-flex flex-column h-100">
        <div className="flex-grow-1 d-flex flex-row">
          <LeftSide></LeftSide>
          <RightSide></RightSide>
        </div>
      </div>
    </>
  );
}
