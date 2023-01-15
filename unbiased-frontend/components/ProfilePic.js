
import types from "../types";

export default function ProfilePic(props) {
    const tier = props.tier

    return <img className={`${tier}-frame-color rounded-circle object-fit-cover img-div`} src={props.hasImage ? `/profile/${props.userId}/image` : "/img/profile-image.png"}
                 width="80"/>
}

ProfilePic.propTypes = types.ProfilePic

