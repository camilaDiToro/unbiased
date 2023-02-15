
import types from "../types";
import {getResourcePath} from "../constants";

export default function ProfilePic(props) {
    const tier = props.tier

    return <img data-testid="img-profile" aria-label="profileImage" className={`${tier}-frame-color rounded-circle object-fit-cover img-div`} src={props.image ? props.image : getResourcePath("/img/profile-image.png")}
                 width="80" alt="profile-pic"/>
}

ProfilePic.propTypes = types.ProfilePic

