import Link from "next/link";
import Tooltip from "./Tooltip";
import {useAppContext} from "../context";
import types from "../types";
import {getResourcePath} from "../constants";

export default function FollowButton(props) {
    const {I18n, api} = useAppContext()

    const handleFollow = () => {
        api.followUser(props.userId).then(r => {
            const {success} = r
            if (success) {
                props.triggerEffect()
            }
        })
    }

    const handleUnfollow = () => {
        api.unfollowUser(props.userId).then(r => {
            const {success} = r
            if (success) {
                props.triggerEffect()
            }
        })
    }
    return <>
        {props.following ?                     <div className="btn d-flex btn-sm border text-white align-items-center justify-content-center">
        <Tooltip  text={I18n("profile.following")} position="bottom" >
                <div
                      onClick={handleUnfollow}>
                        <img src={getResourcePath("/img/following.svg")} alt="..."/>
                </div>

            </Tooltip>                     </div>
            :
            <div className="btn d-flex btn-info btn-sm text-white align-items-center justify-content-center custom-btn-follow"
                  onClick={handleFollow}>
                {I18n("profile.follow")}
            </div>
        }

    </>
}

FollowButton.propTypes = types.FollowButton