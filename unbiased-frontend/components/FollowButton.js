import Link from "next/link";
import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";

export default function FollowButton(props) {
    const {I18n} = useAppContext()
    return <>
        {props.following ?  <Tooltip className="btn d-flex btn-sm border text-white align-items-center justify-content-center" text={I18n("profile.following")} position="bottom" >
                <Link
                      href={`/profile/${props.userId}/unfollow`}>
                    <img src="/img/following.svg" alt="..."/>
                </Link>
            </Tooltip> :
            <Link className="btn d-flex btn-info btn-sm text-white align-items-center justify-content-center custom-btn-follow"
                  href={`/profile/${props.userId}/follow`}>
                {I18n("profile.follow")}
            </Link>
        }

    </>
}

FollowButton.propTypes = types.FollowButton