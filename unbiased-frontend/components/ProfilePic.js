
import types from "../types";

export default function ProfilePic(props) {
    const tier = props.tier

    return <img id={`${tier}-frame-color`} src={props.hasImage ? `/profile/${props.userId}/image` : "/img/profile-image.png"}
                className="rounded-circle object-fit-cover img-div" width="80"/>
}

ProfilePic.propTypes = types.ProfilePic

