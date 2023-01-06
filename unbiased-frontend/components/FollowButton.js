import Link from "next/link";

export default function FollowButton(props) {
    return <>
        {props.following ?  <Link className="btn d-flex btn-sm border text-white align-items-center justify-content-center"
                  href={`/profile/${props.userId}/unfollow`} data-toggle="tooltip"
                  data-placement="bottom" title="profile.following">
            <img src="/img/following.svg" alt="..."/>
        </Link> : <Link className="btn d-flex btn-info btn-sm text-white align-items-center justify-content-center custom-btn-follow"
            href={`/profile/${props.userId}/follow`}>
            {"profile.follow"}
            </Link>}

    </>
}